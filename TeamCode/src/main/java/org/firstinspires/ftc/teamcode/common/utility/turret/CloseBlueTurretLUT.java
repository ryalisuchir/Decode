package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class CloseBlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public CloseBlueTurretLUT() {
        turretServoLUT.addPoint(-2.03035, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.80217, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.52498, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.24432, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.97833, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.69625, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.40825, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.15111, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.03759, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.37073, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.69460, 0.500); // radians, servo position
        turretServoLUT.addPoint( 0.96560, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.25755, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.53318, 0.650); // radians, servo position
        turretServoLUT.addPoint( 1.82139, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.10949, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.40481, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.68413, 0.850); // radians, servo position
        turretServoLUT.addPoint( 2.88366, 0.900); // radians, servo position
        turretServoLUT.addPoint( 3.08098, 0.950); // radians, servo position
        turretServoLUT.addPoint( 3.08097, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}