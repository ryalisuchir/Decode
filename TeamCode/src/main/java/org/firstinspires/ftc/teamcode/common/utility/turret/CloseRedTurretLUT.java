package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT;

public class CloseRedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public CloseRedTurretLUT() {
        turretServoLUT.addPoint(-2.18500, 0); //radians, servo position
        turretServoLUT.addPoint(-1.91650, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.65375, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.41627, 0.15); //radians, servo position
        turretServoLUT.addPoint(-1.21458, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.97649, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.65473, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.37663, 0.35); //radians, servo position
        turretServoLUT.addPoint(-0.14934, 0.4); //radians, servo position
        turretServoLUT.addPoint(0.11719, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.40797, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.75449, 0.55); //radians, servo position
        turretServoLUT.addPoint(1.10825, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.42119, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.83157, 0.7); //radians, servo position
        turretServoLUT.addPoint(2.11446, 0.75); //radians, servo position
        turretServoLUT.addPoint(2.38101, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.67488, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.92948, 0.90); //radians, servo position
        turretServoLUT.addPoint(-3.00275, 0.95); //radians, servo position
        turretServoLUT.addPoint(-2.71959, 1); //radiansh, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
//        if (robotAngle < -1.8) return turretServoLUT.get(robotAngle);
//
//        double normalizedAngle = Math.atan2(Math.sin(robotAngle), Math.cos(robotAngle));
//        double servoPos = (normalizedAngle * Globals.slope) + Globals.intercept;
//
//        return servoPos;
    }
}