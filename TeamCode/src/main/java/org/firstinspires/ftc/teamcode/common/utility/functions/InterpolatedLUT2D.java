package org.firstinspires.ftc.teamcode.common.utility.functions;

import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

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

    /**
     * Bilinear-like interpolation: find the four nearest neighbors in x and y and interpolate.
     * For simplicity, this uses the closest points and does weighted average.
     */
    public ShooterParams get(double x, double y) {
        if (points.isEmpty()) return new ShooterParams(0, 0);

        Point p1 = null, p2 = null, p3 = null, p4 = null;
        double minDist = Double.MAX_VALUE;

        // Find four closest points (for simplicity, brute-force; could optimize later)
        points.sort((a, b) -> {
            double da = Math.hypot(a.x - x, a.y - y);
            double db = Math.hypot(b.x - x, b.y - y);
            return Double.compare(da, db);
        });

        // Use the 4 nearest points
        p1 = points.get(0);
        p2 = points.size() > 1 ? points.get(1) : p1;
        p3 = points.size() > 2 ? points.get(2) : p1;
        p4 = points.size() > 3 ? points.get(3) : p1;

        // Weighted average by inverse distance
        double totalWeight = 0;
        double hood = 0, vel = 0;

        for (Point p : new Point[]{p1, p2, p3, p4}) {
            double dist = Math.hypot(p.x - x, p.y - y);
            double weight = 1.0 / (dist + 1e-6); // prevent divide by zero
            totalWeight += weight;
            hood += p.params.hoodPos * weight;
            vel += p.params.shooterVel * weight;
        }

        return new ShooterParams(hood / totalWeight, vel / totalWeight);
    }
}
