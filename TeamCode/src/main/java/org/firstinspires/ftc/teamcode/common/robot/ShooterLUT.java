package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT1D;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT1D shooterLUT = new InterpolatedLUT1D();

    public ShooterLUT() {
        shooterLUT.addPoint(145, new ShooterParams(0.8, 1850));
        shooterLUT.addPoint(95, new ShooterParams(0.8, 1400));
        shooterLUT.addPoint(57, new ShooterParams(0.8, 1150));
        shooterLUT.addPoint(67, new ShooterParams(0.8, 1230));
        shooterLUT.addPoint(36, new ShooterParams(0.2, 1200));
    }

    public ShooterParams getShooterValue(double distance) {
        return shooterLUT.get(distance);
    }
}