package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class FarBlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public FarBlueTurretLUT() {
        turretServoLUT.addPoint(-1.69617, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.60937, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.41700, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.13982, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.87439, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.58912, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.32742, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.04500, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.13074, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.48156, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.84031, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.11846, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.39642, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.67684, 0.650); // radians, servo position
        turretServoLUT.addPoint( 1.95365, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.24119, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.49415, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.79987, 0.850); // radians, servo position
        turretServoLUT.addPoint( 3.01127, 0.900); // radians, servo position
        turretServoLUT.addPoint( 3.13092, 0.950); // radians, servo position
        turretServoLUT.addPoint( 3.13003, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}