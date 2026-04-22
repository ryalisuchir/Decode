package org.firstinspires.ftc.teamcode.opmode.testing;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

@TeleOp(name = "ReadColorSensors")
public class ReadColorSensors extends OpMode {

    AnalogInput dist1, dist2, dist3;
    NormalizedColorSensor color1, color2, color3;


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

        NormalizedRGBA rgba1 = color1.getNormalizedColors();
        float[] hsv1 = new float[3];
        Color.colorToHSV(rgba1.toColor(), hsv1);

        NormalizedRGBA rgba2 = color2.getNormalizedColors();
        float[] hsv2 = new float[3];
        Color.colorToHSV(rgba2.toColor(), hsv2);

        NormalizedRGBA rgba3 = color3.getNormalizedColors();
        float[] hsv3 = new float[3];
        Color.colorToHSV(rgba3.toColor(), hsv3);

        telemetry.addLine("=== DISTANCES (cm) ===");
        telemetry.addData("Dist 1", String.format("%.2f", d1));
        telemetry.addData("Dist 2", String.format("%.2f", d2));
        telemetry.addData("Dist 3", String.format("%.2f", d3));

        telemetry.addLine("=== RAW HUES ===");
        telemetry.addData("Hue 1", String.format("%.1f", hsv1[0]));
        telemetry.addData("Hue 2", String.format("%.1f", hsv2[0]));
        telemetry.addData("Hue 3", String.format("%.1f", hsv3[0]));


        telemetry.update();
    }

    private double readDistance(AnalogInput pin) {
        return pin.getVoltage() / 3.3 * 100.0;
    }
}