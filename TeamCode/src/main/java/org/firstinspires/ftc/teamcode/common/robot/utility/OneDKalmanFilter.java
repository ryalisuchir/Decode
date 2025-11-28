package org.firstinspires.ftc.teamcode.common.robot.utility;

public class OneDKalmanFilter {
    private double estimate;
    private double error;

    private final double processNoise;
    private final double measurementNoise;

    public OneDKalmanFilter(double initialEstimate, double initialError, double Q, double R) {
        this.estimate = initialEstimate;
        this.error = initialError;
        this.processNoise = Q;
        this.measurementNoise = R;
    }

    public double update(double measurement) {
        error += processNoise;

        double K = error / (error + measurementNoise);

        estimate = estimate + K * (measurement - estimate);

        error = (1 - K) * error;

        return estimate;
    }

    public double getEstimate() {
        return estimate;
    }
}
