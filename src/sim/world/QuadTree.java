package sim.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;

import sim.Vec2;
import sim.Range;

public class QuadTree<T> implements Iterable<T> {
    public T value = null;
    private Vec2 position;
    private Vec2 size;
    private Range range;
    private QuadTree<T> topLeft = null,
            topRight = null,
            bottomLeft = null,
            bottomRight = null;

    public QuadTree(int initialSize, Vec2 position) {
        range = new Range(position, new Vec2(initialSize));
        this.position = range.position;
        this.size = range.size;
    }

    /**
     * @param point (Vec2) - the position of the value.
     * @param value (T) - the value to place at {@code position}.
     * @return the new root if {@code point} is outside of this node
     * otherwise {@code this}.
     */
    public void add(Vec2 point, T value) {
        while (!range.contains(point)) {
            var oldPosition = position.clone();
            var pos = getPosition(point);
            var node = new QuadTree<T>(size.x, position);
            node.value = value;
            node.topLeft = topLeft;
            node.topRight = topRight;
            node.bottomLeft = bottomLeft;
            node.bottomRight = bottomRight; 
            this.value = null;
            topLeft = null;
            topRight = null;
            bottomLeft = null;
            bottomRight = null;
            position.set(pos);
            size.mul(2);

            int index = pos
                .sub(oldPosition)
                .map(a -> a == -(size.x >> 1) ? 1 : 0)
                .flat((x, y) -> x + y * 2);
            setChild(index, node);
        }

        var node = this;
        while (node.size.x != 1) {
            var next = node.getChild(point);
            if (next != null) {
                node = next;
                continue;
            }
            var newNode = new QuadTree<T>(node.size.x >> 1, node.getPosition(point));
            node.setChild(point, newNode);
            node = newNode;
        }

        node.value = value;
    }

    /**
     * @param point (Vec2) - the point to query data from.
     * @return {@code Optional.empty()} if there's no value at {@code point}
     * otherwise returns {@code Optional.<T>of()}.
     */
    public Optional<T> query(Vec2 point) {
        var node = this;
        while (node.size.x != 1) {
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
    public void remove(Vec2 point, Consumer<T> consumer) {
        var stack = new Stack<QuadTree<T>>();
        var node = this;
        stack.push(node);
        while (node.size.x != 1) {
            var next = node.getChild(point);
            if (next == null)
                return;

            stack.push(next);
            node = next;
        }

        consumer.accept(stack.pop().value);
        while (!stack.isEmpty()) {
            node = stack.pop();
            node.setChild(point, null);
            if (!node.isLeaf()) {
                break;
            }
        }
    }

    // remove(Range)
    // query(Range)

    public Iterable<T> iterable(Range range) {
        var items = new ArrayList<T>();

        var stack = new Stack<QuadTree<T>>();
        var node = this;
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node == null || !node.range.intersects(range))
                continue;
            if (node.value != null)
                items.add(node.value);

            stack.push(node.bottomRight);
            stack.push(node.bottomLeft);
            stack.push(node.topRight);
            stack.push(node.topLeft);
        }

        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return items.iterator();
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        var self = this;
        return new Iterator<T>() {
            Stack<QuadTree<T>> stack = new Stack<QuadTree<T>>();
            QuadTree<T> node = self;

            @Override
            public boolean hasNext() {
                return stack.isEmpty();
            }

            @Override
            public T next() {
                while (!stack.isEmpty()) {
                    node = stack.pop();
                    if (node == null)
                        continue;
                    if (node.value != null)
                        return (node.value);

                    stack.push(node.bottomRight);
                    stack.push(node.bottomLeft);
                    stack.push(node.topRight);
                    stack.push(node.topLeft);
                }
                return null;
            }
        };
    }

    private void setChild(Vec2 point, QuadTree<T> value) {
        setChild(getChildIndex(point), value);
    }

    private QuadTree<T> getChild(Vec2 point) {
        return getChild(getChildIndex(point));
    }

    private void setChild(int index, QuadTree<T> value) {
        switch (index) {
            case 0 -> topLeft = value;
            case 1 -> topRight = value;
            case 2 -> bottomLeft = value;
            case 3 -> bottomRight = value;
            default -> throw new IllegalStateException("Index must be in range [0;3]");
        }
    }

    private QuadTree<T> getChild(int index) {
        return switch (index) {
            case 0 -> topLeft;
            case 1 -> topRight;
            case 2 -> bottomLeft;
            case 3 -> bottomRight;
            default -> throw new IllegalStateException("Index must be in range [0;3]");
        };
    }

    public boolean isLeaf() {
        return topLeft == null
            && topRight == null
            && bottomLeft == null
            && bottomRight == null;
    }

    private int getChildIndex(Vec2 point) {
        return (point.x - position.x < size.x >> 1 ? 0 : 1)
             + (point.y - position.y < size.y >> 1 ? 0 : 2);
    }

    private Vec2 getPosition(Vec2 point) {
        return point
            .clone()
            .sub(position)
            .map(a -> a < 0 ? -2
                    : a >= size.x ? 0
                    : a >= size.x >> 1 ? 1
                    : 0
            )
            .mul(size.x >> 1)
            .add(position);
    }

    @Override
    public String toString() {
        return new StringBuilder("<QuadTree:")
            .append(range)
            .append('|')
            .append(topLeft == null ? 0 : 1)
            .append(topRight == null ? 0 : 1)
            .append(bottomLeft == null ? 0 : 1)
            .append(bottomRight == null ? 0 : 1)
            .append('|')
            .append(value)
            .append('>')
            .toString();
    }
}
