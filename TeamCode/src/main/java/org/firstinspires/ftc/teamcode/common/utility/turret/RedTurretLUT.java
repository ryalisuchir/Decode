package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-1.68442, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.38712, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.10810, 0.100); // radians, servo position
        turretServoLUT.addPoint(-0.80148, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.52026, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.25006, 0.250); // radians, servo position
        turretServoLUT.addPoint(0.00339, 0.300); // radians, servo position
        turretServoLUT.addPoint(0.27888, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.54642, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.82275, 0.450); // radians, servo position
        turretServoLUT.addPoint( 1.15060, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.41307, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.70156, 0.600); // radians, servo position
        turretServoLUT.addPoint( 2.00928, 0.650); // radians, servo position
        turretServoLUT.addPoint( 2.27651, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.58137, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.85922, 0.800); // radians, servo position
        turretServoLUT.addPoint( -3.13306, 0.850); // radians, servo position
        turretServoLUT.addPoint( -2.83879, 0.900); // radians, servo position
        turretServoLUT.addPoint( -2.50661, 0.950); // radians, servo position
        turretServoLUT.addPoint( -2.21312, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}