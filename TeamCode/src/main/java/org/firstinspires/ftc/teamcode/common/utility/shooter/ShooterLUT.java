package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParamLUT;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;

public class ShooterLUT {
    private final ShooterParamLUT shooterLUT = new ShooterParamLUT();

    public ShooterLUT() {
        shooterLUT.addPoint(23, new ShooterParams(0.65, 1490)); //done
        shooterLUT.addPoint(35, new ShooterParams(0.72, 1510)); //done
        shooterLUT.addPoint(51, new ShooterParams(0.75, 1600)); //done
        shooterLUT.addPoint(72, new ShooterParams(0.81, 1750)); //done
        shooterLUT.addPoint(90, new ShooterParams(0.83, 1950)); //done
        shooterLUT.addPoint(130, new ShooterParams(0.89, 2230));
        shooterLUT.addPoint(147, new ShooterParams(0.9, 2350));
        shooterLUT.addPoint(181, new ShooterParams(0.92, 2500));
    }

    public ShooterParams getShooterValue(double dist) {
        double baseHood = -(1.23418e-9) * Math.pow(dist, 4)
                + (5.61273e-7) * Math.pow(dist, 3)
                - 0.0000970021 * Math.pow(dist, 2)
                + 0.00882568 * dist
                + 0.49629;

        double baseVel = 0.00000388478 * Math.pow(dist, 4)
                - 0.00191582 * Math.pow(dist, 3)
                + 0.312943 * Math.pow(dist, 2)
                - 12.07482 * dist
                + 1624.99329;

        return new ShooterParams(baseHood, baseVel);
    }
}