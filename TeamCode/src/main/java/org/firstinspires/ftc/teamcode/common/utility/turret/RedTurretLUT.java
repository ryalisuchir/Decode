package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-1.74297, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.39245, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.16156, 0.100); // radians, servo position
        turretServoLUT.addPoint(-0.81166, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.59229, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.26894, 0.250); // radians, servo position
//        turretServoLUT.addPoint(-0.04557, 0.300); // radians, servo position
//        turretServoLUT.addPoint(-0.32009, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.48893, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.85393, 0.450); // radians, servo position
        turretServoLUT.addPoint( 1.13452, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.36083, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.74583, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.94883, 0.650); // radians, servo position
        turretServoLUT.addPoint( 2.24161, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.52746, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.86943, 0.800); // radians, servo position
        turretServoLUT.addPoint( 3.10132, 0.850); // radians, servo position
        turretServoLUT.addPoint( -2.88551, 0.900); // radians, servo position
        turretServoLUT.addPoint( -2.56713, 0.950); // radians, servo position
        turretServoLUT.addPoint( -2.29462, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}