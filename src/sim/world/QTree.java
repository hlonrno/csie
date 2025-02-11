package sim.world;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

import sim.gates.Gate;
import sim.Vec2;

public class QTree {
  protected QTree[] children;
  protected Vec2 size, halfSize, position;
  private boolean isLeaf = false;
  public Gate value = null;

  public QTree(Vec2 treeSize, Vec2 position) {
    size = treeSize.clone();
    children = new QTree[4];
    halfSize = treeSize.clone().shiftRight(1);
    this.position = position.clone();
    Arrays.fill(children, null);
  }

  public QTree(Vec2 treeSize, Vec2 position, Gate value) {
    this(treeSize, position);
    this.value = value;
  }

  private int count = 0;
  public QTree add(Gate gate) {
    var root = this;
    while (!root.size.equals(1, 1)) {
      count++; while (count > 16);
      int childIndex = root.getChildIndex(gate.position);

      if (root.children[childIndex] == null) {
        var newPosition = root.getPosition(gate.position);
        root.children[childIndex] = new QTree(root.halfSize, newPosition);
      }
      root = root.children[childIndex];
    }
    System.out.println(root.position);

    root.isLeaf = true;
    root.value = gate;
    return root;
  }

  public Optional<QTree> query(Vec2 point) {
    var root = this;
    while (!root.isLeaf) {
      int childIndex = root.getChildIndex(point);

      if (root.children[childIndex] == null)
        return Optional.empty();
      root = root.children[childIndex];
    }

    return Optional.of(root);
  }

  public boolean remove(Vec2 point) {
    var root = this;
    var path = new Stack<QTree>();
    while (!root.isLeaf) {
      int childIndex = root.getChildIndex(point);
      if (root.children[childIndex] == null)
        return false;
      path.push(root);
      root = root.children[childIndex];
    }

    var parent = path.peek();
    int childIndex = parent.getChildIndex(point);
    parent.children[childIndex] = null;

    boolean isNotEmpty = false;
    for (var child : parent.children)
      if (child != null) {
        isNotEmpty = true;
        break;
      }

    if (!isNotEmpty)
      return true;

    while (!path.isEmpty()) {
      var node = path.pop();

      isNotEmpty = false;
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

    return true;
  }

  public Vec2 getPosition(Vec2 point) {
    // Integer arithmetic
    return position.clone()
        .add(point.clone()
            .abs()
            .sub(position)
            .div(halfSize)
            .mul(halfSize));
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
}
