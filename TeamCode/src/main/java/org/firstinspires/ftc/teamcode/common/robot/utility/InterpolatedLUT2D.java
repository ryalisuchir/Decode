package org.firstinspires.ftc.teamcode.common.robot.utility;

import java.util.Map;
import java.util.TreeMap;

public class InterpolatedLUT2D {
    private final TreeMap<Double, TreeMap<Double, ShooterParams>> lut = new TreeMap<>();

    // Add calibration point
    public void addPoint(double distance, double angle, ShooterParams params) {
        lut.putIfAbsent(distance, new TreeMap<>());
        lut.get(distance).put(angle, params);
    }

    public ShooterParams get(double distance, double angle) {
        Map.Entry<Double, TreeMap<Double, ShooterParams>> lowerDist = lut.floorEntry(distance);
        Map.Entry<Double, TreeMap<Double, ShooterParams>> upperDist = lut.ceilingEntry(distance);

        if (lowerDist == null) return interpolateAngle(upperDist.getValue(), angle);
        if (upperDist == null) return interpolateAngle(lowerDist.getValue(), angle);

        double d1 = lowerDist.getKey(), d2 = upperDist.getKey();
        ShooterParams a1 = interpolateAngle(lowerDist.getValue(), angle);
        ShooterParams a2 = interpolateAngle(upperDist.getValue(), angle);

        double t = (distance - d1) / (d2 - d1);
        double hood = a1.hoodPos + t * (a2.hoodPos - a1.hoodPos);
        double velocity = a1.shooterVel + t * (a2.shooterVel - a1.shooterVel);

        return new ShooterParams(hood, velocity);
    }

    private ShooterParams interpolateAngle(TreeMap<Double, ShooterParams> angleMap, double angle) {
        Map.Entry<Double, ShooterParams> lower = angleMap.floorEntry(angle);
        Map.Entry<Double, ShooterParams> upper = angleMap.ceilingEntry(angle);

        if (lower == null) return upper.getValue();
        if (upper == null) return lower.getValue();

        double a1 = lower.getKey(), a2 = upper.getKey();
        ShooterParams p1 = lower.getValue(), p2 = upper.getValue();

        double t = (angle - a1) / (a2 - a1);
        double hood = p1.hoodPos + t * (p2.hoodPos - p1.hoodPos);
        double velocity = p1.shooterVel + t * (p2.shooterVel - p1.shooterVel);

        return new ShooterParams(hood, velocity);
    }
}
