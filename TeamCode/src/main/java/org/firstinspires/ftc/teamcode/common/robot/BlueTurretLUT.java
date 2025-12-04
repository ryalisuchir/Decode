package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-0.04789, 0.34); //radians, servo position
        turretServoLUT.addPoint(0.09331, 0.39); //radians, servo position
        turretServoLUT.addPoint(-2.052976, 0); //radians, servo position
        turretServoLUT.addPoint(-1.624904, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.546209, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.13588, 0.15); //radians, servo position
        turretServoLUT.addPoint(-0.867029, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.639818, 0.25); //radians, servo position   turretServoLUT.addPoint(-0.04789, 0.34); //radians, servo position
        turretServoLUT.addPoint(0.09331, 0.39); //radians, servo position
        turretServoLUT.addPoint(-2.052976, 0); //radians, servo position
        turretServoLUT.addPoint(-1.624904, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.546209, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.13588, 0.15); //radians, servo position
        turretServoLUT.addPoint(-0.867029, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.639818, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.349885, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.23061, 0.35); //radians, servo position
        turretServoLUT.addPoint(0.026019, 0.40); //radians, servo position
        turretServoLUT.addPoint(0.371979, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.59152, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.81984, 0.55); //radians, servo position
        turretServoLUT.addPoint(0.974748, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.154432, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.402087, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.588896, 0.75); //radians, servo position
        turretServoLUT.addPoint(1.911144, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.051248, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.348408, 0.9); //radians, servo position
        turretServoLUT.addPoint(2.43221, 0.95); //radians, servo position
        turretServoLUT.addPoint(2.706188, 1); //radians, servo position
        turretServoLUT.addPoint(-0.349885, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.23061, 0.35); //radians, servo position
        turretServoLUT.addPoint(0.026019, 0.40); //radians, servo position
        turretServoLUT.addPoint(0.371979, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.59152, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.81984, 0.55); //radians, servo position
        turretServoLUT.addPoint(0.974748, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.154432, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.402087, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.588896, 0.75); //radians, servo position
        turretServoLUT.addPoint(1.911144, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.051248, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.348408, 0.9); //radians, servo position
        turretServoLUT.addPoint(2.43221, 0.95); //radians, servo position
        turretServoLUT.addPoint(2.706188, 1); //radians, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}