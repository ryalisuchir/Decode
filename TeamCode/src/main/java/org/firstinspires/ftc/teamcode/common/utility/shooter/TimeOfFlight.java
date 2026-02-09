package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.functions.luts.TimeOfFlightLUT;

public class TimeOfFlight {
    private final TimeOfFlightLUT timeOfFlightLUT = new TimeOfFlightLUT();

    public TimeOfFlight() { //in seconds
        timeOfFlightLUT.addPoint(51, 0.8);
    }

    public double getTimeOfFlight(double dist) {
        return timeOfFlightLUT.get(dist);
    }
}