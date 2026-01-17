package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class FarRedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public FarRedTurretLUT() {
        turretServoLUT.addPoint(-1.92445, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.68102, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.40048, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.11287, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.84729, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.55705, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.27869, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.00143, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.26890, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.52325, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.87320, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.14444, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.41615, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.71129, 0.650); // radians, servo position
        turretServoLUT.addPoint( 1.99640, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.28795, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.55876, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.80775, 0.850); // radians, servo position
        turretServoLUT.addPoint( 2.99928, 0.900); // radians, servo position
        turretServoLUT.addPoint( 3.11347, 0.950); // radians, servo position
        turretServoLUT.addPoint( 3.11352, 1.000); // radians, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}