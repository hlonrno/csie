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

  public void set(Gate gate) {
    gate(gate);
  }

  public void gate(Gate gate) {
    Vec2 point = gate.position;
    while (root.isNotInBounds(point)) {
      var newPosition = root.getPosition(point);
      var newRoot = new QTree(root.size.clone().mul(2), newPosition);
      var childIndex = newRoot.getChildIndex(point);
      newRoot.children[childIndex] = root;
      root = newRoot;
      System.out.print(root.position);
      System.out.println(" " + root.size);
    }

    root.add(gate);
  }

  public void del(Vec2 point) {
    if (root.isNotInBounds(point))
      return;
    root.remove(point);
  }
}
