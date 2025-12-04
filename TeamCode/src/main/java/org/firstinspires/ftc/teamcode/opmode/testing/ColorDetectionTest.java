package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.utility.DenoiseFilter;

@TeleOp
public class ColorDetectionTest extends OpMode {
    AnalogInput sensor1, sensor2, sensor3;
    private DenoiseFilter denoise1, denoise2, denoise3;

    @Override
    public void init() {
        sensor1 = hardwareMap.analogInput.get("analog1");
        sensor2 = hardwareMap.analogInput.get("analog2");
        sensor3 = hardwareMap.analogInput.get("analog3");
        denoise1 = new DenoiseFilter(5);
        denoise2 = new DenoiseFilter(5);
        denoise3 = new DenoiseFilter(5);
    }


    private Globals.BallColor1 readColor(double hue, DenoiseFilter denoise) {
        Globals.BallColor1 color = null;

        if (hue < 120) {
            Globals.ballColor1 = Globals.BallColor1.NONE;
            color = Globals.BallColor1.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            Globals.ballColor1 = Globals.BallColor1.G;
            color = Globals.BallColor1.G;
        }
        else if (hue >= 125 && hue < 180) {
            Globals.ballColor1 = Globals.BallColor1.P;
            color = Globals.BallColor1.P;
        }

        if (color == null) {
            denoise.reset();
            Globals.ballColor1 = Globals.BallColor1.NONE;
            color = Globals.BallColor1.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            Globals.ballColor1 = Globals.BallColor1.values()[avgIndex];
            color = Globals.BallColor1.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            Globals.ballColor1 = Globals.BallColor1.NONE;
            color = Globals.BallColor1.NONE;
        }

        return color;
    }

    private Globals.BallColor2 readColor2(double hue, DenoiseFilter denoise) {
        Globals.BallColor2 color = null;

        if (hue < 120) {
            Globals.ballColor2 = Globals.BallColor2.NONE;
            color = Globals.BallColor2.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            Globals.ballColor2 = Globals.BallColor2.G;
            color = Globals.BallColor2.G;
        }
        else if (hue >= 125 && hue < 180) {
            Globals.ballColor2 = Globals.BallColor2.P;
            color = Globals.BallColor2.P;
        }

        if (color == null) {
            denoise.reset();
            Globals.ballColor2 = Globals.BallColor2.NONE;
            color = Globals.BallColor2.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            Globals.ballColor2 = Globals.BallColor2.values()[avgIndex];
            color = Globals.BallColor2.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            Globals.ballColor2 = Globals.BallColor2.NONE;
            color = Globals.BallColor2.NONE;
        }

        return color;
    }

    private Globals.BallColor3 readColor3(double hue, DenoiseFilter denoise) {
        Globals.BallColor3 color = null;

        if (hue < 120) {
            Globals.ballColor3 = Globals.BallColor3.NONE;
            color = Globals.BallColor3.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            Globals.ballColor3 = Globals.BallColor3.G;
            color = Globals.BallColor3.G;
        }
        else if (hue >= 125 && hue < 180) {
            Globals.ballColor3 = Globals.BallColor3.P;
            color = Globals.BallColor3.P;
        }

        if (color == null) {
            denoise.reset();
            Globals.ballColor3 = Globals.BallColor3.NONE;
            color = Globals.BallColor3.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            Globals.ballColor3 = Globals.BallColor3.values()[avgIndex];
            color = Globals.BallColor3.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            Globals.ballColor3 = Globals.BallColor3.NONE;
            color = Globals.BallColor3.NONE;
        }

        return color;
    }



    @Override
    public void loop() {
        double hue1 = sensor1.getVoltage() / 3.3 * 360;
        double hue2 = sensor2.getVoltage() / 3.3 * 360;
        double hue3 = sensor3.getVoltage() / 3.3 * 360;

        telemetry.addData("Sensor 1 Hue: ", hue1);
        telemetry.addData("Sensor 2 Hue: ", hue2);
        telemetry.addData("Sensor 3 Hue: ", hue3);

        Globals.ballColor1 = readColor(hue1, denoise1);
        Globals.ballColor2 = readColor2(hue2, denoise2);
        Globals.ballColor3 = readColor3(hue3, denoise3);

        telemetry.addData("Ball Color 1: ", Globals.ballColor1);
        telemetry.addData("Ball Color 2: ", Globals.ballColor2);
        telemetry.addData("Ball Color 3: ", Globals.ballColor3);

        telemetry.update();
    }
}
