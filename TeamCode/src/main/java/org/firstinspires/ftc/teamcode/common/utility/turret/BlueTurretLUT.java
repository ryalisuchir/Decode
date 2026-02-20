package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-1.88330, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.53662, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.29691, 0.100); // radians, servo position
        turretServoLUT.addPoint(-0.93507, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.69884, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.42212, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.30094, 0.300); // radians, servo position
        turretServoLUT.addPoint(0.06229, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.34592, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.60913, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.94571, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.21931, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.47613, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.82137, 0.650); // radians, servo position
        turretServoLUT.addPoint( 2.10949, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.42779, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.67842, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.98007, 0.850); // radians, servo position
        turretServoLUT.addPoint( -2.93602, 0.900); // radians, servo position
        turretServoLUT.addPoint( -2.68883, 0.950); // radians, servo position
        turretServoLUT.addPoint(-2.38877, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}