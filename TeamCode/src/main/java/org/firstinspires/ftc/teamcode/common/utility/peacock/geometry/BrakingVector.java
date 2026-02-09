package org.firstinspires.ftc.teamcode.common.utility.peacock.geometry;

public class BrakingVector {
    private final double x;
    private final double y;

    public BrakingVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public BrakingVector plus(BrakingVector other) {
        return new BrakingVector(this.x + other.x, this.y + other.y);
    }

    public BrakingVector minus(BrakingVector other) {
        return new BrakingVector(this.x - other.x, this.y - other.y);
    }

    public BrakingVector times(double scalar) {
        return new BrakingVector(this.x * scalar, this.y * scalar);
    }

    public BrakingVector dividedBy(double scalar) {
        return new BrakingVector(this.x / scalar, this.y / scalar);
    }

    public double computeMagnitude() {
        return Math.hypot(x, y);
    }

    public BrakingVector withMagnitude(double magnitude) {
        double current = computeMagnitude();
        if (current == 0) return new BrakingVector(0, 0);
        return times(magnitude / current);
    }

    public BrakingVector withMaxMagnitude(double maxMagnitude) {
        double m = computeMagnitude();
        if (m > maxMagnitude) return withMagnitude(maxMagnitude);
        return this;
    }

    public BrakingVector map(BrakingVector other,
                      BiFunctionDouble mappingFunction) {
        return new BrakingVector(
                mappingFunction.apply(getX(), other.getX()),
                mappingFunction.apply(getY(), other.getY())
        );
    }

    /**
     * Apply a custom operation to each pair of components.
     * <code>new Vector(operation(x1, x2), operation(y1, y2))</code>
     */
    @FunctionalInterface
    public interface BiFunctionDouble {
        double apply(double component1, double component2);
    }

    public double dot(BrakingVector other) {
        return this.x * other.x + this.y * other.y;
    }

    public double cross(BrakingVector other) {
        return this.x * other.y - this.y * other.x;
    }

    public BrakingVector withX(double newX) {
        return new BrakingVector(newX, this.y);
    }

    public BrakingVector withY(double newY) {
        return new BrakingVector(this.x, newY);
    }

    /**
     * Rotates the vector counterclockwise by the given radians.
     */
    public BrakingVector rotateCounterclockwiseBy(double radians) {
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new BrakingVector(getX() * cos - getY() * sin,
                getX() * sin + getY() * cos);
    }

    /**
     * Rotates the vector clockwise by the given radians.
     */
    public BrakingVector rotateClockwiseBy(double radians) {
        return rotateCounterclockwiseBy(-radians);
    }

    public double getAngleToLookAt(BrakingVector point) {
        BrakingVector direction = this.minus(point);
        return Math.atan2(direction.getY(), direction.getX());
    }

    public double distanceTo(BrakingVector point) {
        double dx = point.getX() - this.getX();
        double dy = point.getY() - this.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public BrakingVector normalized() {
        double magnitude = computeMagnitude();
        if (magnitude == 0) {
            return new BrakingVector(0, 0);
        }
        return new BrakingVector(x / magnitude, y / magnitude);
    }

    @Override
    public String toString() {
        return "Vector(" + x + ", " + y + ")";
    }
}