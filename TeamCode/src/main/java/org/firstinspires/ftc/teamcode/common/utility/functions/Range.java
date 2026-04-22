package org.firstinspires.ftc.teamcode.common.utility.functions;

public class Range {

    private double min;
    private double max;

    public Range(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double resetPos() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double kickPos() {
        return max;
    }

    public double engagedPos() {
        return max;
    }

    public void setRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
        this.min = min;
        this.max = max;
    }

    public double clamp(double value) {
        return Math.max(min, Math.min(max, value));
    }
}