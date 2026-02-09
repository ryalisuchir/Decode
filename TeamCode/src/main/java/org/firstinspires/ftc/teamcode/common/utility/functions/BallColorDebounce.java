package org.firstinspires.ftc.teamcode.common.utility.functions;

import org.firstinspires.ftc.teamcode.common.utility.G;

public class BallColorDebounce {

    private final int requiredStableFrames;
    private G.BallColor lastStable = G.BallColor.NONE;
    private G.BallColor candidate = G.BallColor.NONE;
    private int counter = 0;

    public BallColorDebounce(int frames) {
        this.requiredStableFrames = frames;
    }

    public G.BallColor update(G.BallColor newReading) {

        // If same as last stable, reset candidate tracking
        if (newReading == lastStable) {
            counter = 0;
            return lastStable;
        }

        // If new candidate appears, start counting
        if (newReading != candidate) {
            candidate = newReading;
            counter = 1;
            return lastStable;
        }

        // Candidate repeated
        counter++;

        // Only switch after N stable frames
        if (counter >= requiredStableFrames) {
            lastStable = candidate;
            counter = 0;
        }

        return lastStable;
    }
}
