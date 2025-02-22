package sim.world;

import java.util.Optional;
import java.util.Iterator;
import java.util.function.Consumer;

import sim.gates.Gate;
import sim.Range;
import sim.Vec2;

public class World implements Iterable<Gate>{
    private QuadTree<Gate> root;

    public World() {
        root = new QuadTree<>(16, new Vec2(0, 0));
    }

    public Optional<Gate> get(Vec2 point) {
        return root.query(point);
    }

    public void set(Gate gate) {
        root.add(gate.position, gate);
    }

    public void delete(Vec2 point, Consumer<Gate> consumer) {
        root.remove(point, consumer);
    }

    public void delete(Range range, Consumer<Gate> consumer) {
        for (var gate : root.iterable(range))  {
            root.remove(gate.position, consumer);
        }
    }

    public Iterable<Gate> iterable(Range range) {
        return root.iterable(range);
    }

    @Override
    public Iterator<Gate> iterator() {
        return root.iterator();
    }
}
