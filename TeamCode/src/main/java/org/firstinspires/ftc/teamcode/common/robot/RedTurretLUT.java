package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT;

public class RedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public RedTurretLUT() {
        turretServoLUT.addPoint(-1.829063, 0); //radians, servo position
        turretServoLUT.addPoint(-1.641902, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.317446, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.035948, 0.15); //radians, servo position
        turretServoLUT.addPoint(-0.73568, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.490864, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.243673, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.040776, 0.33); //radians, servo position
        turretServoLUT.addPoint(-0.024542, 0.35); //radians, servo position
        turretServoLUT.addPoint(0.337527, 0.40); //radians, servo position
        turretServoLUT.addPoint(0.520924, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.69354, 0.50); //radians, servo position
        turretServoLUT.addPoint(0.923752, 0.55); //radians, servo position
        turretServoLUT.addPoint(1.125367, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.328902, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.502817, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.693312, 0.75); //radians, servo position
        turretServoLUT.addPoint(1.89491, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.07371, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.27692, 0.9); //radians, servo position
        turretServoLUT.addPoint(2.417325, 0.95); //radians, servo position
        turretServoLUT.addPoint(2.67239, 1); //radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}