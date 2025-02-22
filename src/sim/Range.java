package sim;

public class Range {
    public static final Range MAX_RANGE = new Range(
            Integer.MIN_VALUE,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE
    );
    public Vec2 position, size, position2;

    /**
     * @throws IllegalStateException if width or height are negative.
     */
    public Range(Vec2 position, Vec2 size) {
        this.position = position.clone();
        this.size = size.clone();
        if (size.x < 0 || size.y < 0)
            throw new IllegalStateException("Size must be positive.");
    }

    /**
     * @throws IllegalStateException if width or height is negative.
     */
    public Range(int x, int y, int width, int height) {
        position = new Vec2(x, y);
        size = new Vec2(width, height); 
        if (width < 0 || height < 0)
            throw new IllegalStateException("Size must be positive.");
    }

    public boolean contains(Vec2 point) {
        return point.x >= position.x
            && point.y >= position.y
            && point.x < position.x + size.x
            && point.y < position.y + size.y;
    }

    public static boolean lineIntersectLine1D(int x1, int x2, int x3, int x4) {
        return (x1 >= x3 && x1 < x4)
            || (x3 >= x1 && x3 < x2);
    }

    public boolean intersects(Range range) {
        return lineIntersectLine1D(
                position.x, position.x + size.x,
                range.position.x, range.position.x + range.size.x
            )
            && lineIntersectLine1D(
                position.y, position.y + size.y,
                range.position.y, range.position.y + range.size.y
            );
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
