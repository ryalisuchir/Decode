package org.firstinspires.ftc.teamcode.common.utility;

import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT1D;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT1D shooterLUT = new InterpolatedLUT1D();

    public ShooterLUT() {
        shooterLUT.addPoint(170, new ShooterParams(0.86, 1850));
        shooterLUT.addPoint(147, new ShooterParams(0.8, 1780));
        shooterLUT.addPoint(116, new ShooterParams(0.8, 1500));
        shooterLUT.addPoint(91, new ShooterParams(0.8, 1350));
        shooterLUT.addPoint(70, new ShooterParams(0.8, 1200));
        shooterLUT.addPoint(41, new ShooterParams(0.2, 1100));
    }

    public ShooterParams getShooterValue(double distance) {
        return shooterLUT.get(distance);
    }
}