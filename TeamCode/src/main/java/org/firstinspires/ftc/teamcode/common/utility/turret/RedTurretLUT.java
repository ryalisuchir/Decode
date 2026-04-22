package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.tables.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-0.09608, 0.000);
        turretServoLUT.addPoint(0.24822, 0.050);
        turretServoLUT.addPoint(0.53270, 0.100);
        turretServoLUT.addPoint(0.79850, 0.150);
        turretServoLUT.addPoint(1.14150, 0.200);
        turretServoLUT.addPoint(1.42918, 0.250);
        turretServoLUT.addPoint(1.75716, 0.300);
        turretServoLUT.addPoint(2.07699, 0.350);
        turretServoLUT.addPoint(2.32595, 0.400);
        turretServoLUT.addPoint(2.59675, 0.450);
        turretServoLUT.addPoint(3.00698, 0.500);
        turretServoLUT.addPoint(-2.87619, 0.550);
        turretServoLUT.addPoint(-2.64719, 0.600);
        turretServoLUT.addPoint(-2.40186, 0.650);
        turretServoLUT.addPoint(-2.21384, 0.700);
        turretServoLUT.addPoint(-1.53719, 0.750);
        turretServoLUT.addPoint(-1.25091, 0.800);
        turretServoLUT.addPoint(-0.81972, 0.850);
        turretServoLUT.addPoint(-0.40729, 0.900);
        turretServoLUT.addPoint(-0.11944, 0.950);
        turretServoLUT.addPoint(0.19615, 1.000);
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}
