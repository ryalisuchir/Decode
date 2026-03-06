package org.firstinspires.ftc.teamcode.common.utility.turret;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.InterpolatedLUT;

public class BlueTurretLUT {
    private final InterpolatedLUT turretServoLUT = new InterpolatedLUT();

    public BlueTurretLUT() {
        turretServoLUT.addPoint(-1.86522, 0.000); // radians, servo position
        turretServoLUT.addPoint(-1.56792, 0.050); // radians, servo position
        turretServoLUT.addPoint(-1.28890, 0.100); // radians, servo position
        turretServoLUT.addPoint(-0.98228, 0.150); // radians, servo position
        turretServoLUT.addPoint(-0.70106, 0.200); // radians, servo position
        turretServoLUT.addPoint(-0.43086, 0.250); // radians, servo position
        turretServoLUT.addPoint(-0.17741, 0.300); // radians, servo position
        turretServoLUT.addPoint( 0.09808, 0.350); // radians, servo position
        turretServoLUT.addPoint( 0.36562, 0.400); // radians, servo position
        turretServoLUT.addPoint( 0.64195, 0.450); // radians, servo position
        turretServoLUT.addPoint( 0.96980, 0.500); // radians, servo position
        turretServoLUT.addPoint( 1.23227, 0.550); // radians, servo position
        turretServoLUT.addPoint( 1.52076, 0.600); // radians, servo position
        turretServoLUT.addPoint( 1.82848, 0.650); // radians, servo position
        turretServoLUT.addPoint( 2.09571, 0.700); // radians, servo position
        turretServoLUT.addPoint( 2.40057, 0.750); // radians, servo position
        turretServoLUT.addPoint( 2.67842, 0.800); // radians, servo position
        turretServoLUT.addPoint( 2.96933, 0.850); // radians, servo position
        turretServoLUT.addPoint(-3.01959, 0.900); // radians, servo position
        turretServoLUT.addPoint(-2.68741, 0.950); // radians, servo position
        turretServoLUT.addPoint(-2.39392, 1.000); // radians, servo position
    }

    public double getServoValue(double robotAngle) {
        return turretServoLUT.get(robotAngle);
    }
}
