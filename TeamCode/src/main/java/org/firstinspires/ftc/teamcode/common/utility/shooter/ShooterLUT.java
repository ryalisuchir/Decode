package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParamLUT;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;

public class ShooterLUT {
    private final ShooterParamLUT shooterLUT = new ShooterParamLUT();

    public ShooterLUT() {
        shooterLUT.addPoint(42, new ShooterParams(0.74, 900)); //done

        shooterLUT.addPoint(73, new ShooterParams(0.78, 1000)); //done

        shooterLUT.addPoint(85, new ShooterParams(0.86, 1200)); //done

        shooterLUT.addPoint(100, new ShooterParams(0.85, 1300)); //done

        shooterLUT.addPoint(143, new ShooterParams(0.87, 2100));
        shooterLUT.addPoint(157, new ShooterParams(0.92, 2300)); //middle of far triangle (0.01 was good offset)
        shooterLUT.addPoint(181, new ShooterParams(0.92, 2400)); //inconsistent and 0.01 was good offset
    }

//    hood_data = np.array([0.77, 0.83, 0.87, 0.87, 0.90, 0.92, 0.91, 0.92])
//    velo_data = np.array([1100, 1200, 1400, 1500, 1600, 1800, 2000, 2400])
//    dist_data = np.array([51, 71, 95, 116, 150, 163, 176, 195])

    public ShooterParams getShooterValue(double dist) {
        double baseHood = (4.17432e-9) * Math.pow(dist, 4)
                - 0.00000196658 * Math.pow(dist, 3)
                + 0.000318789 * Math.pow(dist, 2)
                - 0.0189858 * dist
                + 1.09674;

        double baseVel = -0.0000270146 * Math.pow(dist, 4)
                + 0.0113594 * Math.pow(dist, 3)
                - 1.62607 * Math.pow(dist, 2)
                + 102.36663 * dist
                - 1222.90722;

        return new ShooterParams(baseHood, baseVel);
    }
}