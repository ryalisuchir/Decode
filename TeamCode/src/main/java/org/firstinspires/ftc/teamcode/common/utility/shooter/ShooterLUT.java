package org.firstinspires.ftc.teamcode.common.utility.shooter;

import static androidx.core.math.MathUtils.clamp;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT2D;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT2D shooterLUT = new InterpolatedLUT2D();
    private VelocityErrorLUT velocityErrorLUT = new VelocityErrorLUT();

    public ShooterLUT() {
        shooterLUT.addPoint(28, 110, new ShooterParams(0.78, 1200));
        shooterLUT.addPoint(34, 104, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(46, 115, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(53, 125, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(46, 99, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(61, 111, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(57, 97, new ShooterParams(0.79, 1200));
        shooterLUT.addPoint(58, 86, new ShooterParams(0.84, 1300));
        shooterLUT.addPoint(73, 94, new ShooterParams(0.84, 1300));
        shooterLUT.addPoint(79, 115, new ShooterParams(0.84, 1500));
        shooterLUT.addPoint(71, 74, new ShooterParams(0.93, 1500));
        shooterLUT.addPoint(70, 87, new ShooterParams(0.92, 1500));
        shooterLUT.addPoint(70, 97, new ShooterParams(0.91, 1500));
        shooterLUT.addPoint(71, 108, new ShooterParams(0.91, 1500));
        shooterLUT.addPoint(65, 110, new ShooterParams(0.9, 1400));
        shooterLUT.addPoint(73, 121, new ShooterParams(0.885, 1400));
        shooterLUT.addPoint(72, 68, new ShooterParams(0.9, 1400));
        shooterLUT.addPoint(77, 96, new ShooterParams(0.9, 1400));
        shooterLUT.addPoint(77, 114, new ShooterParams(0.9, 1400));
        shooterLUT.addPoint(79, 123, new ShooterParams(0.89, 1400));
        shooterLUT.addPoint(88, 73, new ShooterParams(0.9, 1500));
        shooterLUT.addPoint(91, 96, new ShooterParams(0.9, 1400));
        shooterLUT.addPoint(95, 118, new ShooterParams(0.88, 1400));
        shooterLUT.addPoint(96, 86, new ShooterParams(0.9, 1500));
        shooterLUT.addPoint(109, 105, new ShooterParams(0.9, 1500));
        shooterLUT.addPoint(114, 116, new ShooterParams(0.9, 1500));
        shooterLUT.addPoint(117, 101, new ShooterParams(0.88, 1600));
    }

    public ShooterParams getShooterValue(double dist, double actualVel) {
        double baseHood = 0.616892 + 0.003685*dist - 0.000011*dist*dist;
        double baseVel = 1100.622035 - 1.367801*dist + 0.045720*dist*dist;

        double velError = actualVel - baseVel;

        double correction = velocityErrorLUT.getHoodErrorChange(dist, velError);

        double correctedHood = baseHood + correction;

        if (!Globals.shooterKicking) {
            correctedHood = baseHood;
        }

        correctedHood = clamp(correctedHood, Globals.HOOD_LOWERED, Globals.HOOD_MAX);

        return new ShooterParams(correctedHood, baseVel);
    }
}