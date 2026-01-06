package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT;

public class FarBlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public FarBlueTurretLUT() {
        turretServoLUT.addPoint(-2.47697, 0); //radians, servo position
        turretServoLUT.addPoint(-2.22762, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.98731, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.68953, 0.15); //radians, servo position
        turretServoLUT.addPoint(-1.43998, 0.2); //radians, servo position
        turretServoLUT.addPoint(-1.12852, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.86499, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.58244, 0.35); //radians, servo position
        turretServoLUT.addPoint(-0.31387, 0.4); //radians, servo position
        turretServoLUT.addPoint(0.01512, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.27956, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.52901, 0.55); //radians, servo position
        turretServoLUT.addPoint(0.84317, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.16463, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.49227, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.78602, 0.75); //radians, servo position
        turretServoLUT.addPoint(2.06040, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.42538, 0.85); //radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return robotAngle * 0.18 + 0.41;
    }
}