package org.firstinspires.ftc.teamcode.common.utility;

import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(2.70919, 0); //radians, servo position
        turretServoLUT.addPoint(3.08838, 0.05); //radians, servo position
        turretServoLUT.addPoint(-2.98304, 0.1); //radians, servo position
        turretServoLUT.addPoint(-2.74967, 0.15); //radians, servo position
        turretServoLUT.addPoint(-2.42360, 0.2); //radians, servo position
        turretServoLUT.addPoint(-2.06969, 0.25); //radians, servo position
        turretServoLUT.addPoint(-1.82591, 0.3); //radians, servo position
        turretServoLUT.addPoint(-1.48893, 0.35); //radians, servo position
        turretServoLUT.addPoint(-1.15866, 0.4); //radians, servo position
        turretServoLUT.addPoint(-0.97829, 0.45); //radians, servo position
        turretServoLUT.addPoint(-0.60528, 0.5); //radians, servo position
        turretServoLUT.addPoint(-0.29739, 0.55); //radians, servo position
        turretServoLUT.addPoint(-0.08971, 0.6); //radians, servo position
        turretServoLUT.addPoint(0.18117, 0.65); //radians, servo position
        turretServoLUT.addPoint(0.35176, 0.7); //radians, servo position
        turretServoLUT.addPoint(0.54061, 0.75); //radians, servo position
        turretServoLUT.addPoint(0.97424, 0.8); //radians, servo position
        turretServoLUT.addPoint(1.19464, 0.85); //radians, servo position
        turretServoLUT.addPoint(1.41383, 0.90); //radians, servo position
        turretServoLUT.addPoint(1.53626, 0.95); //radians, servo position
        turretServoLUT.addPoint(1.96013, 1); //radians, servo position

    }

    public double getServoValue(double robotAngle) {
//        return turretServoLUT.get(robotAngle);
        return robotAngle * 0.1753 - 0.3777;
    }
}