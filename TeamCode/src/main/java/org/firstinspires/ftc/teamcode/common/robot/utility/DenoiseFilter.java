package org.firstinspires.ftc.teamcode.common.robot.utility;
public class DenoiseFilter {

    private final double[] window;
    private final int size;
    private int index = 0;
    private int count = 0;

    public DenoiseFilter(int windowSize) {
        this.size = windowSize;
        this.window = new double[windowSize];
    }

    // Add a new sample, return the average of the window
    public double filter(double newValue) {
        window[index] = newValue;
        index = (index + 1) % size;

        if (count < size) count++;

        // compute average
        double sum = 0;
        for (int i = 0; i < count; i++) {
            sum += window[i];
        }
        return sum / count;
    }

    public boolean isWindowFull() {
        return count == size;
    }

    public void reset() {
        index = 0;
        count = 0;
        for (int i = 0; i < size; i++) {
            window[i] = 0;
        }
    }
}

