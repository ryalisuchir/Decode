package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-0.052053, 0.43); //radians, servo position
        turretServoLUT.addPoint(0.065504, 0.48); //radians, servo position
        turretServoLUT.addPoint(-0.448756, 0.35); //radians, servo position
        turretServoLUT.addPoint(-1.267128, 0.2); //radians, servo position
        turretServoLUT.addPoint(-1.351721, 0.15); //radians, servo position
        turretServoLUT.addPoint(-1.746817, 0.1); //radians, servo position
        turretServoLUT.addPoint(-2.534844, 0); //radians, servo position
        turretServoLUT.addPoint(0.236435, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.582613, 0.55); //radians, servo position
        turretServoLUT.addPoint(0.865427, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.147426, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.679656, 0.75); //radians, servo position
        turretServoLUT.addPoint(1.264063, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.352483, 0.9); //radians, servo position
        turretServoLUT.addPoint(2.326805, 0.95); //radians, servo position
        turretServoLUT.addPoint(2.591857, 1); //radians, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}