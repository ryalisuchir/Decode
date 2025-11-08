package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT2D;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT2D shooterLUT = new InterpolatedLUT2D();

    public ShooterLUT() {
        shooterLUT.addPoint(24, 0,   new ShooterParams(0.35, 2200));
        shooterLUT.addPoint(24, 15,  new ShooterParams(0.38, 2250));
        shooterLUT.addPoint(36, 0,   new ShooterParams(0.45, 2400));
        shooterLUT.addPoint(36, 15,  new ShooterParams(0.48, 2450));
        shooterLUT.addPoint(48, 0,   new ShooterParams(0.55, 2600));
        shooterLUT.addPoint(48, 15,  new ShooterParams(0.58, 2650));
    }

    public ShooterParams getShooterValue(double distance, double angle) {
        return shooterLUT.get(distance, angle);
    }
}