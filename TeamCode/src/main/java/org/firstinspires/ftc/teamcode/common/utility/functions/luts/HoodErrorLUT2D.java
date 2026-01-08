package org.firstinspires.ftc.teamcode.common.utility.functions.luts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * 2D Linear Lookup Table for Hood Adjustments
 * X = Distance, Y = Velocity Error
 */
public class HoodErrorLUT2D {

    // Using a Nested TreeMap to keep data sorted by Distance (X) then Error (Y)
    // Map<Distance, Map<Error, Adjustment>>
    private final TreeMap<Double, TreeMap<Double, Double>> grid = new TreeMap<>();

    public void addPoint(double distance, double velocityError, double hoodAdjustment) {
        grid.computeIfAbsent(distance, k -> new TreeMap<>()).put(velocityError, hoodAdjustment);
    }

    public double get(double x, double y) {
        if (grid.isEmpty()) return 0.0;

        // 1. Find bounding distances (x1, x2)
        Double x1 = grid.floorKey(x);
        Double x2 = grid.ceilingKey(x);

        if (x1 == null) x1 = x2;
        if (x2 == null) x2 = x1;

        // 2. Interpolate along Y for both x1 and x2
        double yValAtX1 = interpolateLinearY(grid.get(x1), y);
        double yValAtX2 = interpolateLinearY(grid.get(x2), y);

        // 3. Interpolate between those results along X
        return interpolate(x1, yValAtX1, x2, yValAtX2, x);
    }

    private double interpolateLinearY(TreeMap<Double, Double> yMap, double y) {
        if (yMap == null || yMap.isEmpty()) return 0.0;

        Double y1 = yMap.floorKey(y);
        Double y2 = yMap.ceilingKey(y);

        if (y1 == null) y1 = y2;
        if (y2 == null) y2 = y1;

        return interpolate(y1, yMap.get(y1), y2, yMap.get(y2), y);
    }

    private double interpolate(double startX, double startY, double endX, double endY, double targetX) {
        if (endX == startX) return startY;
        return startY + (targetX - startX) * (endY - startY) / (endX - startX);
    }
}