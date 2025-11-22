package org.firstinspires.ftc.teamcode.common.robot.utility;

import java.util.Map;
import java.util.TreeMap;

public class InterpolatedLUT {
    private final TreeMap<Double, Double> lut = new TreeMap<>();

    public void addPoint(double key, double value) {
        lut.put(key, value);
    }

    public double get(double key) {
        if (lut.containsKey(key)) return lut.get(key);

        Map.Entry<Double, Double> lower = lut.floorEntry(key);
        Map.Entry<Double, Double> upper = lut.ceilingEntry(key);

        if (lower == null) return upper.getValue();
        if (upper == null) return lower.getValue();

        double x1 = lower.getKey(), y1 = lower.getValue();
        double x2 = upper.getKey(), y2 = upper.getValue();

        double t = (key - x1) / (x2 - x1);
        return y1 + t * (y2 - y1);
    }
}

