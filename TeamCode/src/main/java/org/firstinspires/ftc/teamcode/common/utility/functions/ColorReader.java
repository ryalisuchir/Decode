package org.firstinspires.ftc.teamcode.common.utility.functions;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

public class ColorReader {

    private final DenoiseFilter denoise;
    private final int index; // 0, 1, 2 depending on which sensor we're looking at

    public ColorReader(int index, DenoiseFilter denoise) {
        this.denoise = denoise;
        this.index = index;
    }

    public void readColor(double hue) {
        Globals.BallColor color;

        if (hue < 115) {
            color = Globals.BallColor.NONE;
        } else if (hue < 128) {
            color = Globals.BallColor.G;
        } else if (hue < 170) {
            color = Globals.BallColor.P;
        } else {
            color = Globals.BallColor.NONE;
        }

        // Denoise
        if (denoise.isWindowFull()) {
            int avgIndex = (int) Math.round(denoise.filter(color.ordinal()));
            color = Globals.BallColor.values()[avgIndex];
        } else {
            denoise.filter(color.ordinal());
            color = Globals.BallColor.NONE;
        }

        Globals.ballColors[index] = color;
    }
}