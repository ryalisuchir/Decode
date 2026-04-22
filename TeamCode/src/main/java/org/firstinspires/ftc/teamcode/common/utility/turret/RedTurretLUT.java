package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.tables.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(1.05490, 0.000);
        turretServoLUT.addPoint(1.38627, 0.050);
        turretServoLUT.addPoint(1.69863, 0.100);
        turretServoLUT.addPoint(2.00602, 0.150);
        turretServoLUT.addPoint(2.27263, 0.200);
        turretServoLUT.addPoint(2.64289, 0.250);
        turretServoLUT.addPoint(2.90973, 0.300);
        turretServoLUT.addPoint(3.14032, 0.350);
        turretServoLUT.addPoint(-2.71639, 0.400);
        turretServoLUT.addPoint(-2.30484, 0.450);
        turretServoLUT.addPoint(-1.92968, 0.500);
        turretServoLUT.addPoint(-1.65135, 0.550);
        turretServoLUT.addPoint(-1.28313, 0.600);
        turretServoLUT.addPoint(-0.82323, 0.650);
        turretServoLUT.addPoint(-0.48949, 0.700);
        turretServoLUT.addPoint(-0.18775, 0.750);
        turretServoLUT.addPoint(0.08688, 0.800);
        turretServoLUT.addPoint(0.43023, 0.850);
        turretServoLUT.addPoint(0.77350, 0.900);
        turretServoLUT.addPoint(1.04537, 0.950);
        turretServoLUT.addPoint(1.31674, 1.000);
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}
