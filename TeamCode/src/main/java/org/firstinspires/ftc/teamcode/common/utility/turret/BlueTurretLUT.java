package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-2.17381, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.83906, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.54903, 0.100); // radians, servo position
        turretServoLUT.addPoint(-1.25143, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.96587, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.66438, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.35998, 0.300); // radians, servo position
        turretServoLUT.addPoint(-0.07954, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.10953, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.41522, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.72875, 0.500); // radians, servo position
        turretServoLUT.addPoint( 0.99290, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.27716, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.53347, 0.650); // radians, servo position
        turretServoLUT.addPoint( 1.79966, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.06896, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.37985, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.65301, 0.850); // radians, servo position
        turretServoLUT.addPoint( 2.88046, 0.900); // radians, servo position
        turretServoLUT.addPoint( 3.10418, 0.950); // radians, servo position
        turretServoLUT.addPoint(-2.84141, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}