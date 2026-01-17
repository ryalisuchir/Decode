package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT2D;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;

public class ShooterLUT {
    private final InterpolatedLUT2D shooterLUT = new InterpolatedLUT2D();

    public ShooterLUT() {
        shooterLUT.addPoint(0, 51, new ShooterParams(0.77, 1100)); //done
        shooterLUT.addPoint(0, 71, new ShooterParams(0.83, 1200)); //done
        shooterLUT.addPoint(0, 95, new ShooterParams(0.87, 1400)); //done
        shooterLUT.addPoint(0, 116, new ShooterParams(0.87, 1500)); //done
        shooterLUT.addPoint(0, 150, new ShooterParams(0.90, 1600)); //far right of far triangle (0.02 was good offset)
        shooterLUT.addPoint(0, 163, new ShooterParams(0.92, 1800)); //middle of far triangle (0.01 was good offset)
        shooterLUT.addPoint(0, 176, new ShooterParams(0.91, 2000)); //inconsistent and 0.01 was good offset
        shooterLUT.addPoint(0, 195, new ShooterParams(0.92, 2400)); //inconsistent and 0.01 was good offset
    }

//    hood_data = np.array([0.77, 0.83, 0.87, 0.87, 0.90, 0.92, 0.91, 0.92])
//    velo_data = np.array([1100, 1200, 1400, 1500, 1600, 1800, 2000, 2400])
//    dist_data = np.array([51, 71, 95, 116, 150, 163, 176, 195])

    public ShooterParams getShooterValue(double dist) {
        double baseHood = 0.654425 + 0.002878*dist - 0.000008*dist*dist;
        double baseVel = 1100.622035 - 1.367801*dist + 0.045720*dist*dist;

        return new ShooterParams(baseHood, baseVel);
    }
}