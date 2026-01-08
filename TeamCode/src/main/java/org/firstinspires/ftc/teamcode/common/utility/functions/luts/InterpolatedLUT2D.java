package org.firstinspires.ftc.teamcode.common.utility.functions.luts;

import java.util.ArrayList;
import java.util.List;

public class InterpolatedLUT2D {

    private static class Point {
        double x, y;
        ShooterParams params;
        Point(double x, double y, ShooterParams params) {
            this.x = x;
            this.y = y;
            this.params = params;
        }
    }

    private final List<Point> points = new ArrayList<>();

    public void addPoint(double x, double y, ShooterParams params) {
        points.add(new Point(x, y, params));
    }

    public ShooterParams get(double x, double y) {
        if (points.isEmpty()) return new ShooterParams(0, 0);

        // Step 1: collect all unique y-values
        List<Double> ys = new ArrayList<>();
        for (Point p : points) if (!ys.contains(p.y)) ys.add(p.y);
        ys.sort(Double::compare);

        double[] hoodValues = new double[ys.size()];
        double[] velValues = new double[ys.size()];

        // Step 2: For each y-row, interpolate along x
        for (int i = 0; i < ys.size(); i++) {
            double yi = ys.get(i);
            List<Double> xs = new ArrayList<>();
            List<Double> hoods = new ArrayList<>();
            List<Double> vels = new ArrayList<>();

            for (Point p : points) if (p.y == yi) {
                xs.add(p.x);
                hoods.add(p.params.hoodPos);
                vels.add(p.params.shooterVel);
            }

            if (xs.isEmpty()) continue;

            // Convert to arrays
            double[] xArr = xs.stream().mapToDouble(d -> d).toArray();
            double[] hoodArr = hoods.stream().mapToDouble(d -> d).toArray();
            double[] velArr = vels.stream().mapToDouble(d -> d).toArray();

            hoodValues[i] = MonotoneCubic.interpolate(xArr, hoodArr, x);
            velValues[i] = MonotoneCubic.interpolate(xArr, velArr, x);
        }

        // Step 3: interpolate along y
        double[] yArr = ys.stream().mapToDouble(d -> d).toArray();
        double hood = MonotoneCubic.interpolate(yArr, hoodValues, y);
        double vel = MonotoneCubic.interpolate(yArr, velValues, y);

        return new ShooterParams(hood, vel);
    }
}
