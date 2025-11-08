package org.firstinspires.ftc.teamcode.common.robot;

import org.firstinspires.ftc.teamcode.common.robot.utility.InterpolatedLUT;

public class TurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public TurretLUT() {
        turretServoLUT.addPoint(0, 0.35);
        turretServoLUT.addPoint(45, 0.47);
        turretServoLUT.addPoint(90, 0.60);
        turretServoLUT.addPoint(135, 0.73);
        turretServoLUT.addPoint(180, 0.85);
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}