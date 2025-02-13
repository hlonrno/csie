package sim.world;

import java.util.Optional;

import sim.gates.Gate;
import sim.Vec2;

public class World {
  QuadTree root;

  public World() {
    root = new QuadTree(new Vec2(16, 16), new Vec2(0, 0));
  }

  public Optional<Gate> get(Vec2 point) {
    if (root.isNotInBounds(point))
      return Optional.empty();
    return root.query(point).map(n -> n.value);
  }

  public void set(Gate gate) {
    add(gate);
  }

  public void add(Gate gate) {
    Vec2 point = gate.position;
    while (root.isNotInBounds(point)) {
      var newPosition = root.getPosition(point);
      var newRoot = new QuadTree(root.size.clone().mul(2), newPosition);
      var childIndex = newRoot.getChildIndex(point);
      newRoot.children[childIndex] = root;
      root = newRoot;
    }

    root.add(gate);
  }

  public void del(Vec2 point) {
    if (root.isNotInBounds(point))
      return;
    var gate = root.remove(point);
    gate.map(g -> {
      g.outputs.parallelStream()
          .forEach(out -> {
            for (int i = 0; i < out.inputs.size(); i++)
              if (out.inputs.get(i).position.equals(gate.get().position)) {
                out.inputs.remove(i);
                return;
              }
          });
      return g;
    });
  }
}
