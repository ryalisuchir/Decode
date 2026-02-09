package org.firstinspires.ftc.teamcode.common.utility.functions.luts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeOfFlightLUT {

    private static class Point {
        double distance;
        double time;

        Point(double distance, double time) {
            this.distance = distance;
            this.time = time;
        }
    }

    private final List<Point> points = new ArrayList<>();

    public void addPoint(double distance, double time) {
        points.add(new Point(distance, time));
        points.sort(Comparator.comparingDouble(p -> p.distance));
    }

    /**
     * @param distance input distance
     * @return interpolated time
     */
    public double get(double distance) {
        if (points.isEmpty()) return 0.0;
        if (points.size() == 1) return points.get(0).time;

        double[] distArr = new double[points.size()];
        double[] timeArr = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            distArr[i] = p.distance;
            timeArr[i] = p.time;
        }

        return MonotoneCubic.interpolate(distArr, timeArr, distance);
    }
}
