package org.firstinspires.ftc.teamcode.common.utility.functions.luts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShooterParamLUT {

    private static class Point {
        double distance;
        ShooterParams params;

        Point(double distance, ShooterParams params) {
            this.distance = distance;
            this.params = params;
        }
    }

    private final List<Point> points = new ArrayList<>();

    public void addPoint(double distance, ShooterParams params) {
        points.add(new Point(distance, params));
        points.sort(Comparator.comparingDouble(p -> p.distance));
    }

    public ShooterParams get(double distance) {
        if (points.isEmpty()) return new ShooterParams(0, 0);
        if (points.size() == 1) return points.get(0).params;

        double[] distArr = new double[points.size()];
        double[] hoodArr = new double[points.size()];
        double[] velArr  = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            distArr[i] = p.distance;
            hoodArr[i] = p.params.hoodPos;
            velArr[i]  = p.params.shooterVel;
        }

        double hood = MonotoneCubic.interpolate(distArr, hoodArr, distance);
        double vel  = MonotoneCubic.interpolate(distArr, velArr, distance);

        return new ShooterParams(hood, vel);
    }
}
