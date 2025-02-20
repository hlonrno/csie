package sim;

public class Range {
    public static final Range MAX_RANGE = new Range(
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE
    );
    public Vec2 position, size;

    public Range(Vec2 position, Vec2 size) {
        this.position = position.clone();
        this.size = size.clone();
    }

    public Range(int x, int y, int width, int height) {
        position = new Vec2(x, y);
        size = new Vec2(width, height);

        if (width < 0 || height < 0)
            throw new IllegalStateException("Size must be positive.");
    }

    public boolean contains(Vec2 point) {
        return point
            .clone()
            .sub(position)
            .flatMap((x, y) -> x > -1 && y > -1 && x < size.x && y < size.y);
    }

    public boolean intersects(Range range) {
        // TODO: help.
        return true;
    }

    public Range clone() {
        return new Range(position, size);
    }

    @Override
    public String toString() {
        return new StringBuilder("<")
            .append(position)
            .append("::")
            .append(size)
            .append(">")
            .toString();
    }
}
