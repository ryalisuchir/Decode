package org.firstinspires.ftc.teamcode.common.utility.functions.luts;

import java.util.Map;
import java.util.TreeMap;

public class InterpolatedLUT1D {
    private final TreeMap<Double, ShooterParams> lut = new TreeMap<>();

    public void addPoint(double distance, ShooterParams params) {
        lut.put(distance, params);
    }

    public ShooterParams get(double distance) {
        Map.Entry<Double, ShooterParams> lower = lut.floorEntry(distance);
        Map.Entry<Double, ShooterParams> upper = lut.ceilingEntry(distance);

        if (lower == null) return upper.getValue();

        if (upper == null) return lower.getValue();

        double d1 = lower.getKey();
        double d2 = upper.getKey();
        ShooterParams p1 = lower.getValue();
        ShooterParams p2 = upper.getValue();

        if (d1 == d2) return p1;

        double t = (distance - d1) / (d2 - d1);

        double hood = p1.hoodPos + t * (p2.hoodPos - p1.hoodPos);
        double velocity = p1.shooterVel + t * (p2.shooterVel - p1.shooterVel);

        return new ShooterParams(hood, velocity);
    }
}
