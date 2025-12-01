package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT1D;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT1D shooterLUT = new InterpolatedLUT1D();

    public ShooterLUT() {
        shooterLUT.addPoint(132, new ShooterParams(0.8, 2300));
        shooterLUT.addPoint(96, new ShooterParams(0.8, 1700));
        shooterLUT.addPoint(88, new ShooterParams(0.8, 1800));
        shooterLUT.addPoint(36, new ShooterParams(0.8, 1900));
        shooterLUT.addPoint(48, new ShooterParams(0.8, 1900));
        shooterLUT.addPoint(48, new ShooterParams(0.8, 1900));
    }

    public ShooterParams getShooterValue(double distance) {
        return shooterLUT.get(distance);
    }
}