package sim;

public class Vec2 {
  public int x, y;

  public Vec2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vec2(int v) {
    this(v, v);
  }

  public static Vec2 zero() {
    return new Vec2(0, 0);
  }

  public static Vec2 one() {
    return new Vec2(1, 1);
  }

  public Vec2 set(Vec2 v) {
    x = v.x;
    y = v.y;
    return this;
  }

  public Vec2 normPerAxis() {
    x /= x == 0 ? 1 : x;
    y /= y == 0 ? 1 : y;
    return this;
  }

  public Vec2 min(Vec2 v) {
    this.x = Math.min(v.x, x);
    this.y = Math.min(v.y, y);
    return this;
  }

  public Vec2 clone() {
    return new Vec2(x, y);
  }

  public Vec2 abs() {
    x = Math.abs(x);
    y = Math.abs(y);
    return this;
  }

  public Vec2 add(int x, int y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vec2 add(Vec2 v) {
    return add(v.x, v.y);
  }

  public Vec2 sub(int x, int y) {
    return add(-x, -y);
  }

  public Vec2 sub(Vec2 v) {
    return sub(v.x, v.y);
  }

  public Vec2 mul(int x, int y) {
    this.x *= x;
    this.y *= y;
    return this;
  }

  public Vec2 mul(Vec2 v) {
    return mul(v.x, v.y);
  }

  public Vec2 mul(int n) {
    return mul(n, n);
  }

  public Vec2 div(int x, int y) {
    this.x /= x;
    this.y /= y;
    return this;
  }

  public Vec2 div(Vec2 v) {
    return div(v.x, v.y);
  }

  public Vec2 div(int n) {
    return div(n, n);
  }

  public Vec2 shiftRight(int n) {
    this.x >>= n;
    this.y >>= n;
    return this;
  }

  public boolean equals(Vec2 other) {
    return other.x == x && other.y == y;
  }

  public boolean equals(int x, int y) {
    return this.x == x && this.y == y;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(x)
        .append(';')
        .append(y)
        .toString();
  }
}
