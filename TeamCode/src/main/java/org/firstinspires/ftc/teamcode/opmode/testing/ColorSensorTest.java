package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name = "ColorDistanceTest")
public class ColorSensorTest extends OpMode {

    AnalogInput dist1, dist2, dist3;
    NormalizedColorSensor color1, color2, color3;

    private static final double DISTANCE_THRESHOLD = 20;
    private static final int COLOR_SAMPLE_COUNT = 15;

    private static final float HUE_GREEN_MIN = 100f;
    private static final float HUE_GREEN_MAX = 200f;
    private static final float HUE_PURPLE_MIN = 205f;
    private static final float HUE_PURPLE_MAX = 280f;

    private boolean colorReadDone = false;
    private int colorSamplesTaken = 0;
    private float[][] hsvAccumulator = new float[3][3];
    private String[] ballStates = {"NONE", "NONE", "NONE"};
    private float[] lastHues = {0, 0, 0};

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        dist1 = hardwareMap.get(AnalogInput.class, "dist1");
        dist2 = hardwareMap.get(AnalogInput.class, "dist2");
        dist3 = hardwareMap.get(AnalogInput.class, "dist3");

        color1 = hardwareMap.get(NormalizedColorSensor.class, "color1");
        color2 = hardwareMap.get(NormalizedColorSensor.class, "color2");
        color3 = hardwareMap.get(NormalizedColorSensor.class, "color3");
    }

    @Override
    public void loop() {
        double d1 = readDistance(dist1);
        double d2 = readDistance(dist2);
        double d3 = readDistance(dist3);

        boolean ball0 = d1 < DISTANCE_THRESHOLD;
        boolean ball1 = d2 < DISTANCE_THRESHOLD;
        boolean ball2 = d3 < DISTANCE_THRESHOLD;

        if (!ball0) { ballStates[0] = "NONE"; }
        else if (ballStates[0].equals("NONE")) { ballStates[0] = "PRESENT"; }

        if (!ball1) { ballStates[1] = "NONE"; }
        else if (ballStates[1].equals("NONE")) { ballStates[1] = "PRESENT"; }

        if (!ball2) { ballStates[2] = "NONE"; }
        else if (ballStates[2].equals("NONE")) { ballStates[2] = "PRESENT"; }

        boolean threeBalls = ball0 && ball1 && ball2;

        if (threeBalls && !colorReadDone) {
            if (colorSamplesTaken < COLOR_SAMPLE_COUNT) {
                sampleAll();
                colorSamplesTaken++;
            }
            if (colorSamplesTaken >= COLOR_SAMPLE_COUNT) {
                if (ball0) ballStates[0] = classify(hsvAccumulator[0][0] / COLOR_SAMPLE_COUNT, 0);
                if (ball1) ballStates[1] = classify(hsvAccumulator[1][0] / COLOR_SAMPLE_COUNT, 1);
                if (ball2) ballStates[2] = classify(hsvAccumulator[2][0] / COLOR_SAMPLE_COUNT, 2);
                colorReadDone = true;
            }
        }

        if (!threeBalls) {
            colorReadDone = false;
            colorSamplesTaken = 0;
            hsvAccumulator = new float[3][3];
        }

        // Telemetry
        telemetry.addLine("=== BALL STATES ===");
        telemetry.addData("Slot 1", ballStates[0]);
        telemetry.addData("Slot 2", ballStates[1]);
        telemetry.addData("Slot 3", ballStates[2]);

        telemetry.addLine("=== DISTANCES (cm) ===");
        telemetry.addData("Dist 1", String.format("%.2f", d1));
        telemetry.addData("Dist 2", String.format("%.2f", d2));
        telemetry.addData("Dist 3", String.format("%.2f", d3));

        telemetry.addLine("=== RAW HUES ===");
        telemetry.addData("Hue 1", String.format("%.1f", lastHues[0]));
        telemetry.addData("Hue 2", String.format("%.1f", lastHues[1]));
        telemetry.addData("Hue 3", String.format("%.1f", lastHues[2]));

        telemetry.addLine("=== STATUS ===");
        telemetry.addData("Samples taken", colorSamplesTaken + "/" + COLOR_SAMPLE_COUNT);
        telemetry.addData("Color read done", colorReadDone);
        telemetry.addData("Three balls", threeBalls);

        telemetry.update();
    }

    private void sampleAll() {
        sampleOne(color1, 0);
        sampleOne(color2, 1);
        sampleOne(color3, 2);
    }

    private void sampleOne(NormalizedColorSensor sensor, int slot) {
        NormalizedRGBA rgba = sensor.getNormalizedColors();
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(rgba.toColor(), hsv);
        lastHues[slot] = hsv[0];
        hsvAccumulator[slot][0] += hsv[0];
        hsvAccumulator[slot][1] += hsv[1];
        hsvAccumulator[slot][2] += hsv[2];
    }

    private String classify(float avgHue, int slot) {
        if (avgHue >= HUE_GREEN_MIN && avgHue <= HUE_GREEN_MAX) return "GREEN";
        if (avgHue >= HUE_PURPLE_MIN && avgHue <= HUE_PURPLE_MAX) return "PURPLE";
        return "UNKNOWN (hue=" + String.format("%.1f", avgHue) + ")";
    }

    private double readDistance(AnalogInput pin) {
        return pin.getVoltage() / 3.3 * 100.0;
    }
}