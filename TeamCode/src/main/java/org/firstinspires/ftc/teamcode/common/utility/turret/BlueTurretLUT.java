package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.tables.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-2.89079, 0.000); // radians, servo position
        turretServoLUT.addPoint(-2.71170, 0.050); // radians, servo position
        turretServoLUT.addPoint(-2.35347, 0.100); // radians, servo position
        turretServoLUT.addPoint(-2.09118, 0.150); // radians, servo position
        turretServoLUT.addPoint(-1.79128, 0.200); // radians, servo position
        turretServoLUT.addPoint(-1.50225, 0.250); // radians, servo position
        turretServoLUT.addPoint(-1.21254, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.89860, 0.350); // radians, servo position
        turretServoLUT.addPoint(-0.60229, 0.400); // radians, servo position
        turretServoLUT.addPoint(-0.34688, 0.450); // radians, servo position
        turretServoLUT.addPoint(-0.04913, 0.500); // radians, servo position
        turretServoLUT.addPoint(0.21955, 0.550); // radians, servo position
        turretServoLUT.addPoint(0.50779, 0.600); // radians, servo position
        turretServoLUT.addPoint(0.75295, 0.650); // radians, servo position
        turretServoLUT.addPoint(1.03390, 0.700); // radians, servo position
        turretServoLUT.addPoint(1.27137, 0.750); // radians, servo position
        turretServoLUT.addPoint(1.56655, 0.800); // radians, servo position
        turretServoLUT.addPoint(1.83788, 0.850); // radians, servo position
        turretServoLUT.addPoint(2.10706, 0.900); // radians, servo position
        turretServoLUT.addPoint(2.41293, 0.950); // radians, servo position
        turretServoLUT.addPoint(2.66160, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}
