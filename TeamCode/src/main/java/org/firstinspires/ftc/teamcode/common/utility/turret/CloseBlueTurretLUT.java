package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.InterpolatedLUT;

public class CloseBlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public CloseBlueTurretLUT() {
        turretServoLUT.addPoint(-2.42123, 0); //radians, servo position
        turretServoLUT.addPoint(-2.14509, 0.05); //radians, servo position
        turretServoLUT.addPoint(-1.85760, 0.1); //radians, servo position
        turretServoLUT.addPoint(-1.56952, 0.15); //radians, servo position
        turretServoLUT.addPoint(-1.33183, 0.2); //radians, servo position
        turretServoLUT.addPoint(-1.08606, 0.25); //radians, servo position
        turretServoLUT.addPoint(-0.82243, 0.3); //radians, servo position
        turretServoLUT.addPoint(-0.53618, 0.35); //radians, servo position
        turretServoLUT.addPoint(-0.31089, 0.4); //radians, servo position
        turretServoLUT.addPoint(0.00574, 0.45); //radians, servo position
        turretServoLUT.addPoint(0.31770, 0.5); //radians, servo position
        turretServoLUT.addPoint(0.54848, 0.55); //radians, servo position
        turretServoLUT.addPoint(0.90634, 0.6); //radians, servo position
        turretServoLUT.addPoint(1.18111, 0.65); //radians, servo position
        turretServoLUT.addPoint(1.46588, 0.7); //radians, servo position
        turretServoLUT.addPoint(1.83361, 0.75); //radians, servo position
        turretServoLUT.addPoint(2.18190, 0.8); //radians, servo position
        turretServoLUT.addPoint(2.45315, 0.85); //radians, servo position
        turretServoLUT.addPoint(2.73117, 0.90); //radians, servo position
        turretServoLUT.addPoint(3.02201, 0.95); //radians, servo position
        turretServoLUT.addPoint(3.044, 1); //radiansh, servo position

    }

    public double getServoValue(double robotAngle) {
        return robotAngle * 0.18 + 0.42;
    }
}