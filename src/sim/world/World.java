package sim.world;

import java.util.Optional;
import sim.gates.Gate;
import sim.Vec2;

public class World {
    private QuadTree<Gate> root;

    public World() {
        root = new QuadTree<>(new Vec2(16, 16), new Vec2(0, 0));
    }

    public Optional<Gate> get(Vec2 point) {
        return root.query(point);
    }

    public void set(Gate gate) {
        root = root.add(gate.position, gate);
    }

    public Optional<Gate> delete(Vec2 point) {
        return root.remove(point);
    }
}
