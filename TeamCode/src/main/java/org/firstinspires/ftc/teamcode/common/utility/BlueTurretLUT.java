package org.firstinspires.ftc.teamcode.common.utility;

import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-2.353941, 0); //radians, servo position
        turretServoLUT.addPoint(-1.548523, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.237707, 0.1); //radians, servo position
        turretServoLUT.addPoint(-0.809855, 0.15); //radians, servo position
        turretServoLUT.addPoint(-0.457673, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.159696, 0.25); //radians, servo position
        turretServoLUT.addPoint(0.095573, 0.3); //radians, servo position
        turretServoLUT.addPoint(0.327672, 0.35); //radians, servo position
        turretServoLUT.addPoint(0.553544, 0.40); //radians, servo position
        turretServoLUT.addPoint(0.777344, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.922548, 0.50); //radians, servo position
        turretServoLUT.addPoint(1.044108, 0.55); //radians, servo position
        turretServoLUT.addPoint(1.208952, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.391802, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.477102, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.629054, 0.75); //radians, servo position
        turretServoLUT.addPoint(1.793994, 0.8); //radians, servo position
        turretServoLUT.addPoint(1.947329, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.090458, 0.9); //radians, servo position
        turretServoLUT.addPoint(2.234402, 0.95); //radians, servo position
        turretServoLUT.addPoint(2.434158, 1); //radians, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}