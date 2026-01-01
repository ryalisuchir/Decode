package org.firstinspires.ftc.teamcode.common.utility;

import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT1D;
import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT2D;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT2D shooterLUT = new InterpolatedLUT2D();

    public ShooterLUT() {
        shooterLUT.addPoint(48, 50, new ShooterParams(0.77, 1180));
        shooterLUT.addPoint(51, 50, new ShooterParams(0.77, 1200));
        shooterLUT.addPoint(57, 50, new ShooterParams(0.77, 1240));
        shooterLUT.addPoint(63, 50, new ShooterParams(0.77, 1280));
        shooterLUT.addPoint(74, 50, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(78, 50, new ShooterParams(0.79, 1250));
        shooterLUT.addPoint(91, 50, new ShooterParams(0.77, 1450));
        shooterLUT.addPoint(110, 50, new ShooterParams(0.85, 1400));
        shooterLUT.addPoint(117, 50, new ShooterParams(0.85, 1400));
        shooterLUT.addPoint(106, 50, new ShooterParams(0.88, 1500));
        shooterLUT.addPoint(104, 50, new ShooterParams(0.89, 1400));
        shooterLUT.addPoint(148, 50, new ShooterParams(0.94, 1900));
        shooterLUT.addPoint(158, 50, new ShooterParams(0.92, 2100));

    }

    public ShooterParams getShooterValue(double x, double y) {
        return shooterLUT.get(x, y);
    }
}