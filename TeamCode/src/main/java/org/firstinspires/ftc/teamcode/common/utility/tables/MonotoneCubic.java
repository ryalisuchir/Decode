package org.firstinspires.ftc.teamcode.common.utility.tables;

public class MonotoneCubic {

    /**
     * Performs monotone cubic interpolation.
     * xs and ys must be same length and xs must be sorted ascending.
     */
    public static double interpolate(double[] xs, double[] ys, double x) {
        int n = xs.length;

        // Handle bounds
        if (x <= xs[0]) return ys[0];
        if (x >= xs[n - 1]) return ys[n - 1];

        // Find interval
        int i = 0;
        while (x > xs[i + 1]) i++;

        double x0 = xs[i], x1 = xs[i + 1];
        double y0 = ys[i], y1 = ys[i + 1];
        double h = x1 - x0;
        double t = (x - x0) / h;

        // Compute finite differences
        double d0 = i == 0 ? (ys[1] - ys[0]) / (xs[1] - xs[0]) : (ys[i + 1] - ys[i - 1]) / (xs[i + 1] - xs[i - 1]);
        double d1 = i + 1 == n - 1 ? (ys[n - 1] - ys[n - 2]) / (xs[n - 1] - xs[n - 2]) : (ys[i + 2] - ys[i]) / (xs[i + 2] - xs[i]);

        // Monotone cubic Hermite spline coefficients
        double m0 = d0;
        double m1 = d1;

        double h00 = (1 + 2 * t) * (1 - t) * (1 - t);
        double h10 = t * (1 - t) * (1 - t);
        double h01 = t * t * (3 - 2 * t);
        double h11 = t * t * (t - 1);

        return h00 * y0 + h10 * h * m0 + h01 * y1 + h11 * h * m1;
    }
}
