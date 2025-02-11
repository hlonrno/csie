package sim.world;

import java.util.Optional;

import sim.gates.Gate;
import sim.Vec2;

public class World {
  QTree root;

  public World() {
    root = new QTree(new Vec2(16, 16), new Vec2(0, 0));
  }

  public Optional<Gate> get(Vec2 point) {
    if (root.isNotInBounds(point))
      return Optional.empty();
    return root.query(point).map(n -> n.value);
  }

  public void set(Gate value) {
    add(value);
  }

  public void add(Gate value) {
    Vec2 point = value.position;
    while (root.isNotInBounds(point)) {
      var newRoot = new QTree(root.size.clone().mul(2), root.getPosition(point));
      var childIndex = newRoot.getChildIndex(point);
      newRoot.children[childIndex] = root;
      root = newRoot;
    }

    root.add(value);
  }

  public void del(Vec2 point) {
    if (root.isNotInBounds(point))
      return;
    root.remove(point);
  }
}
