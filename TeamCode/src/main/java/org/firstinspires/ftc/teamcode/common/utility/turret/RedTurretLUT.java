package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-2.09646, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.77087, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.47041, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.16368, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.87806, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.57495, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.29101, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.00051, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.18237, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.49922, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.80866, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.06047, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.34253, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.59853, 0.650); // radians, servo position
        turretServoLUT.addPoint( 1.88001, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.15780, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.42069, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.69027, 0.850); // radians, servo position
        turretServoLUT.addPoint( 2.90938, 0.900); // radians, servo position
        turretServoLUT.addPoint( 3.11471, 0.950); // radians, servo position
        turretServoLUT.addPoint( 3.11468, 1.000); // radians, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}