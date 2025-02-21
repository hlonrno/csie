package sim.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;

import javax.swing.plaf.BorderUIResource;

import sim.Vec2;
import sim.Range;

public class QuadTree<T> {
    public T value = null;
    private QuadTree<T> topLeft = null,
            topRight = null,
            bottomLeft = null,
            bottomRight = null;
    private Vec2 position;
    private Vec2 size;
    private Range range;

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
            var pos = getPosition(point);
            var node = new QuadTree<T>(size.x, position);
            node.topLeft = topLeft;
            node.topRight = topRight;
            node.bottomLeft = bottomLeft;
            node.bottomRight = bottomRight; 
            topLeft = null;
            topRight = null;
            bottomLeft = null;
            bottomRight = null;
            position = pos.clone();
            size.mul(2);

            pos.sub(position);
            if (pos.x == -(size.x ^ (size.x & 1)))
                pos.x = 1;
            if (pos.y == -(size.x ^ (size.x & 1)))
                pos.y = 1;
            setChild(pos.x + pos.y * 2, node);
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
    public Optional<T> remove(Vec2 point) {
        var stack = new Stack<QuadTree<T>>();
        var node = this;
        stack.push(node);
        while (node.size.x != 1) {
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

    public Iterable<T> iterable(Range range) {
        var items = new ArrayList<T>();
        System.out.println(range);

        var stack = new Stack<QuadTree<T>>();
        var node = this;
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            if (node == null || !range.intersects(node.range))
                continue;
            System.out.println(node);
            if (node.value != null)
                items.add(node.value);

            stack.push(node.bottomRight);
            stack.push(node.bottomLeft);
            stack.push(node.topRight);
            stack.push(node.topLeft);
        }
        System.out.println("\n" + items.size());

        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return items.iterator();
            }
        };
    }

    public boolean isLeaf() {
        return topLeft == null
            && topRight == null
            && bottomLeft == null
            && bottomRight == null;
    }

    private Vec2 getPosition(Vec2 point) {
        return point
            .clone()
            .sub(position)
            .map(x -> x < 0 ? -2
                    : x >= size.x ? 0
                    : x >= size.x >> 1 ? 1
                    : 0
            )
            .mul(size.x >> 1)
            .add(position);
    }

    private void setChild(Vec2 point, QuadTree<T> node) {
        setChild(getChildIndex(point), node);
    }

    private void setChild(int index, QuadTree<T> node) {
        switch (index) {
            case 0 -> topLeft = node;
            case 1 -> topRight = node;
            case 2 -> bottomLeft = node;
            case 3 -> bottomRight = node;
            default -> throw new IllegalStateException("QuadTree.getChildIndex(Vec2) must return integers 0..=3.");
        }
    }

    private QuadTree<T> getChild(Vec2 point) {
        return switch (getChildIndex(point)) {
            case 0 -> topLeft;
            case 1 -> topRight;
            case 2 -> bottomLeft;
            case 3 -> bottomRight;
            default -> throw new IllegalStateException("QuadTree.getChildIndex(Vec2) must return integers 0..=3.");
        };
    }

    private int getChildIndex(Vec2 point) {
        return point
            .clone()
            .sub(position)
            .map(x -> x < size.x >> 1 ? 0 : 1)
            .flatMap((x, y) -> x + y * 2);
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
            .append(value == null ? "" : value)
            .append('>')
            .toString();
    }
}
