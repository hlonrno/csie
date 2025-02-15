package sim.world;

import java.util.Optional;
import java.util.Stack;

import sim.gates.Gate;
import sim.Vec2;

public class QuadTree {
  protected QuadTree[] children = new QuadTree[] { null, null, null, null };
  protected Vec2 size, halfSize, position;
  protected Gate value = null;
  private boolean isLeaf = false;
  private int index = 0;

  public QuadTree(Vec2 size, Vec2 position) {
    this.size = size.clone();
    this.position = position.clone();
    halfSize = size.clone().map(x -> x >> 1);
  }

  public QuadTree(Vec2 treeSize, Vec2 position, Gate value) {
    this(treeSize, position);
    this.value = value;
  }

  public QuadTree add(Gate gate) {
    var node = this;
    while (!node.size.equals(1, 1)) {
      int childIndex = node.getChildIndex(gate.position);

      if (node.children[childIndex] == null) {
        var newPosition = node.getPosition(gate.position);
        node.children[childIndex] = new QuadTree(node.halfSize, newPosition);
      }
      node = node.children[childIndex];
    }

    node.isLeaf = true;
    node.value = gate;
    return node;
  }

  public Optional<QuadTree> query(Vec2 point) {
    var root = this;
    while (!root.isLeaf) {
      int childIndex = root.getChildIndex(point);

      if (root.children[childIndex] == null)
        return Optional.empty();
      root = root.children[childIndex];
    }

    return Optional.of(root);
  }

  public Optional<Gate> remove(Vec2 point) {
    var root = this;
    var path = new Stack<QuadTree>();
    while (!root.isLeaf) {
      int childIndex = root.getChildIndex(point);
      if (root.children[childIndex] == null)
        return Optional.empty();
      path.push(root);
      root = root.children[childIndex];
    }

    var parent = path.peek();
    int childIndex = parent.getChildIndex(point);
    parent.children[childIndex] = null;

    while (!path.isEmpty()) {
      var node = path.pop();

      boolean isNotEmpty = false;
      for (var child : node.children)
        if (child != null) {
          isNotEmpty = true;
          break;
        }

      if (isNotEmpty)
        continue;
      if (path.isEmpty())
        break;

      parent = path.peek();
      childIndex = root.getChildIndex(point);
      parent.children[childIndex] = null;
    }

    return Optional.of(root.value);
  }

  public Vec2 getPosition(Vec2 point) {
    return point.clone()
        .map(x -> x < position.x ? -1 : 0)
        .mul(size)
        .add(position);
  }

  public int getChildIndex(Vec2 point) {
    if (point.x < position.x + halfSize.x) {
      if (point.y < position.y + halfSize.y)
        return 0;
      return 2;
    }
    if (point.y < position.y + halfSize.y)
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
    return isLeaf;
  }

  public Optional<QuadTree> getNextChild() {
    QuadTree child;
    while ((child = children[index]) == null) {
      index++;
      if (index == children.length)
        return Optional.empty();
    }

    return Optional.of(child);
  }
}
