package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-2.65402, 0.000); // radians, servo position
        turretServoLUT.addPoint(-2.37756, 0.050); // radians, servo position
        turretServoLUT.addPoint(-2.07849, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.78960, 0.150); // radians, servo position
        turretServoLUT.addPoint(-1.50909, 0.200); // radians, servo position
        turretServoLUT.addPoint(-1.22207, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.95761, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.67285, 0.350); // radians, servo position
        turretServoLUT.addPoint(-0.37798, 0.400); // radians, servo position
        turretServoLUT.addPoint(-0.12655, 0.450); // radians, servo position
        turretServoLUT.addPoint(0.16556, 0.500); // radians, servo position
        turretServoLUT.addPoint(0.43427, 0.550); // radians, servo position
        turretServoLUT.addPoint(0.69617, 0.600); // radians, servo position
        turretServoLUT.addPoint(0.97831, 0.650); // radians, servo position
        turretServoLUT.addPoint(1.26905, 0.700); // radians, servo position
        turretServoLUT.addPoint(1.56348, 0.750); // radians, servo position
        turretServoLUT.addPoint(1.84338, 0.800); // radians, servo position
        turretServoLUT.addPoint(2.12129, 0.850); // radians, servo position
        turretServoLUT.addPoint(2.40678, 0.900); // radians, servo position
        turretServoLUT.addPoint(2.71791, 0.950); // radians, servo position
        turretServoLUT.addPoint(3.00078, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}
