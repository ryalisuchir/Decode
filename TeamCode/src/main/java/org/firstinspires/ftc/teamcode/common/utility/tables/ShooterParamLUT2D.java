package org.firstinspires.ftc.teamcode.common.utility.tables;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShooterParamLUT2D {

    private static class Point {
        final Pose pose;
        final ShooterParams params;

        Point(Pose pose, ShooterParams params) {
            this.pose = pose;
            this.params = params;
        }
    }

    private static final double EPSILON = 1e-6;
    private static final double IDW_POWER = 2.0;
    private static final int NEIGHBOR_COUNT = 4;

    private final List<Point> points = new ArrayList<>();

    public void addPoint(Pose pose, ShooterParams params) {
        points.add(new Point(pose, params));
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public ShooterParams get(Pose pose) {
        if (points.isEmpty()) return new ShooterParams(0, 0);
        if (points.size() == 1) return points.get(0).params;

        List<Point> nearest = new ArrayList<>(points);
        nearest.sort(Comparator.comparingDouble(p -> distance(pose, p.pose)));
        if (nearest.size() > NEIGHBOR_COUNT) {
            nearest = nearest.subList(0, NEIGHBOR_COUNT);
        }

        double weightedHood = 0.0;
        double weightedVel = 0.0;
        double totalWeight = 0.0;

        for (Point p : nearest) {
            double d = distance(pose, p.pose);
            if (d < EPSILON) {
                return p.params;
            }

            double w = 1.0 / Math.pow(d, IDW_POWER);
            weightedHood += p.params.hoodPos * w;
            weightedVel += p.params.shooterVel * w;
            totalWeight += w;
        }

        if (totalWeight < EPSILON) return nearest.get(0).params;
        return new ShooterParams(weightedHood / totalWeight, weightedVel / totalWeight);
    }

    private static double distance(Pose a, Pose b) {
        return Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
    }
}
