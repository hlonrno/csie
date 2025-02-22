package sim;

import java.util.function.BiFunction;
import java.util.function.IntFunction;

public class Vec2 {
    public static final Vec2 zero = new Vec2();
    public static final Vec2 one = new Vec2(1);
    public int x, y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(int n) {
        this(n, n);
    }

    public Vec2() {
        this(0, 0);
    }

    /**
     * x = f(x);
     * y = f(y);
     * @returns this
     */
    public Vec2 map(IntFunction<Integer> f) {
        x = f.apply(x);
        y = f.apply(y);
        return this;
    }

    /**
     * @returns f(x, y)
     */
    public <T> T flat(BiFunction<Integer, Integer, T> f) {
        return f.apply(x, y);
    }

    public Vec2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2 add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2 sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2 mul(int x, int y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2 div(int x, int y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vec2 set(Vec2 v) {
        return set(v.x, v.y);
    }

    public Vec2 add(Vec2 v) {
        return add(v.x, v.y);
    }

    public Vec2 sub(Vec2 v) {
        return sub(v.x, v.y);
    }

    public Vec2 mul(Vec2 v) {
        return mul(v.x, v.y);
    }

    public Vec2 div(Vec2 v) {
        return div(v.x, v.y);
    }

    public Vec2 set(int n) {
        return set(n, n);
    }

    public Vec2 add(int n) {
        return add(n, n);
    }

    public Vec2 sub(int n) {
        return sub(n, n);
    }

    public Vec2 mul(int n) {
        return mul(n, n);
    }

    public Vec2 div(int n) {
        return div(n, n);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Vec2 v) {
        return v.x == x && v.y == y;
    }

    public Vec2 clone() {
        return new Vec2(x, y);
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
