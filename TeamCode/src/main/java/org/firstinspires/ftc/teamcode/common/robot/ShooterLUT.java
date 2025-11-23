package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT1D;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT1D shooterLUT = new InterpolatedLUT1D();

    public ShooterLUT() {
        shooterLUT.addPoint(24, new ShooterParams(0.35, 2200));
        shooterLUT.addPoint(24, new ShooterParams(0.38, 2250));
        shooterLUT.addPoint(36, new ShooterParams(0.45, 2400));
        shooterLUT.addPoint(36, new ShooterParams(0.48, 2450));
        shooterLUT.addPoint(48, new ShooterParams(0.55, 2600));
        shooterLUT.addPoint(48, new ShooterParams(0.58, 2650));
    }

    public ShooterParams getShooterValue(double distance) {
        return shooterLUT.get(distance);
    }
}