package sim.world;

import java.util.Optional;
import java.util.Stack;
import sim.Vec2;

public class QuadTree<T> {
    public T value = null;
    private QuadTree<T> topLeft = null,
            topRight = null,
            bottomLeft = null,
            bottomRight = null;
    private final Vec2 size, position;

    public QuadTree(Vec2 initialSize, Vec2 position) {
        size = initialSize.clone();
        this.position = position.clone();
    }

    /**
     * @param point (Vec2) - the position of the value.
     * @param value (T) - the value to place at {@code position}.
     * @return the new root if {@code point} is outside of this node
     * otherwise {@code this}.
     */
    public QuadTree<T> add(Vec2 point, T value) {
        var root = this;
        while (isNotInBounds(point)) {
            var position = root.getPosition(point);
            var newNode = new QuadTree<T>(root.size.mul(2), position);
            root.size.div(2);
            root.setChild(point, newNode);
        }

        var node = root;
        while (!node.size.equals(1, 1)) {
            var next = node.getChild(point);
            if (next.value == null) {
                node = next;
                continue;
            }
            var position = next.getPosition(point);
            var newNode = new QuadTree<T>(next.size.div(2), position);
            next.size.mul(2);
            next.setChild(point, newNode);
            node = newNode;
        }

        node.value = value;
        return root;
    }

    /**
     * @param point (Vec2) - the point to query data from.
     * @return {@code Optional.empty()} if there's no value at {@code point}
     * otherwise returns {@code Optional.<T>of()}.
     */
    public Optional<T> query(Vec2 point) {
        var node = this;
        while (!node.size.equals(1, 1)) {
            var next = node.getChild(point);
            if (next == null) {
                return Optional.empty();
            }
            node = next;
        }
        
        return Optional.of(node.value);
    }

    /**
     * @param point (Vec2) - the point to remove the data from.
     * @return {@code Optional.empty()} if there's no value at {@code point}
     * otherwise returns {@code Optional.<T>of()}.
     */
    public Optional<T> remove(Vec2 point) {
        var stack = new Stack<QuadTree<T>>();
        var node = this;
        stack.push(node);
        while (!node.size.equals(1, 1)) {
            var next = node.getChild(point);
            if (next == null) {
                return Optional.empty();
            }
            stack.push(next);
            node = next;
        }

        var value = stack.pop().value;
        while (!stack.isEmpty()) {
            var parent = stack.pop();
            var child = parent.getChild(point);
            parent.setChild(point, null);
            if (!parent.isLeaf()) {
                parent.setChild(point, child);
                break;
            }
        }

        return Optional.of(value);
    }

    private Vec2 getPosition(Vec2 point) {
        return point
            .clone()
            .map(x -> (point.x == x ? position.x : position.y) > x ? -1 : 0)
            .mul(size)
            .add(position);
    }

    private void setChild(Vec2 point, QuadTree<T> node) {
        switch (getChildIndex(point)) {
            case 0 -> topLeft = node;
            case 1 -> topRight = node;
            case 2 -> bottomLeft = node;
            case 3 -> bottomRight = node;
            default -> throw new IllegalStateException("QuadTree.getChildIndex(Vec2) must return integers 0-3.");
        }
    }

    private QuadTree<T> getChild(Vec2 point) {
        return switch (getChildIndex(point)) {
            case 0 -> topLeft;
            case 1 -> topRight;
            case 2 -> bottomLeft;
            case 3 -> bottomRight;
            default -> throw new IllegalStateException("QuadTree.getChildIndex(Vec2) must return integers 0-3.");
        };
    }

    private int getChildIndex(Vec2 point) {
        if (point.x < position.x + size.x >> 1) {
            if (point.y < position.y + size.y >> 1)
                return 0;
            return 2;
        }
        if (point.y < position.y + size.y >> 1)
            return 1;
        return 3;
    }

    public boolean isNotInBounds(Vec2 point) {
        return point.x < position.x
            || point.y < position.y
            || point.x > position.x + size.x
            || point.y > position.y + size.y;
    }

    public boolean isLeaf() {
        return topLeft == null
            && topRight == null
            && bottomLeft == null
            && bottomRight == null;
    }
}
