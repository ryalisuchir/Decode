package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.HoodErrorLUT2D;

public class VelocityErrorLUT {
    private final HoodErrorLUT2D hoodErrorLUT2D = new HoodErrorLUT2D();

    public VelocityErrorLUT() {
        hoodErrorLUT2D.addPoint(28, 110, -0.1);
    }

    public Double getHoodErrorChange(double distance, double velocityError) { //returns hood adjustment
       return hoodErrorLUT2D.get(distance, velocityError);
    }
}