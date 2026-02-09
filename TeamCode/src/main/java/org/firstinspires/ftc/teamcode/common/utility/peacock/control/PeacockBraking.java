package org.firstinspires.ftc.teamcode.common.utility.peacock.control;

import org.firstinspires.ftc.teamcode.common.utility.peacock.util.PeacockBrakingCoefficients;

public class PeacockBraking {
    private PeacockBrakingCoefficients coefficients;

    public PeacockBraking(PeacockBrakingCoefficients coefficients) {
        this.coefficients = coefficients;
    }

    public void setCoefficients(PeacockBrakingCoefficients coefficients) {
        this.coefficients = coefficients;
    }

    public double computeOutput(double error, double velocity) {
        double directionOfMotion = Math.signum(velocity);

        double outputPower =
                coefficients.P * (error - computeBrakingDisplacement(velocity, directionOfMotion));

        return clampReversePower(outputPower, directionOfMotion);
    }

    public double computeBrakingDisplacement(double velocity, double directionOfMotion) {
        return directionOfMotion * velocity * velocity * coefficients.kQuadraticFriction
                + velocity * coefficients.kLinearBraking;
    }

    private double clampReversePower(double power, double directionOfMotion) {
        boolean isOpposingMotion = directionOfMotion * power < 0;
        if (!isOpposingMotion) {
            return power;
        }
        double clampedPower;
        if (power < 0) {
            clampedPower = Math.max(power, -coefficients.maximumBrakingPower);
        } else {
            clampedPower = Math.min(power, coefficients.maximumBrakingPower);
        }
        return clampedPower;
    }
}