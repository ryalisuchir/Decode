package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class FarRedTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public FarRedTurretLUT() {
        turretServoLUT.addPoint(-2.27075, 0); //radians, servo position
        turretServoLUT.addPoint(-1.98240, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.72783, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.47496, 0.15); //radians, servo position
        turretServoLUT.addPoint(-1.19432, 0.2); //radians, servo position
        turretServoLUT.addPoint(-0.94247, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.64718, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.39162, 0.35); //radians, servo position
        turretServoLUT.addPoint(-0.10809, 0.4); //radians, servo position
        turretServoLUT.addPoint(0.17941, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.47079, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.78328, 0.55); //radians, servo position
        turretServoLUT.addPoint(1.09655, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.36225, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.69092, 0.7); //radians, servo position
        turretServoLUT.addPoint(2.02666, 0.75); //radians, servo position
        turretServoLUT.addPoint(2.35162, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.63771, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.91599, 0.90); //radians, servo position
        turretServoLUT.addPoint(-3.05585, 0.95); //radians, servo position
        turretServoLUT.addPoint(-2.73323, 1); //radiansh, servo position

    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}