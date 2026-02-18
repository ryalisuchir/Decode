package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;

@TeleOp
@Disabled
public class DigitalDetection extends OpMode {

    int color1 = 0;
    int color2 = 0;
    int color3 = 0;

    DigitalChannel dig1a, dig2a;
    AnalogInput analog1;

    DigitalChannel dig3a, dig4a;
    AnalogInput analog2;

    DigitalChannel dig5a, dig6a;
    AnalogInput analog3;

    @Override
    public void init() {
        telemetry = new PeacockTelemetry(this);

        analog1 = hardwareMap.get(AnalogInput.class, "analog1");
        dig1a = hardwareMap.get(DigitalChannel.class, "dig1a");
        dig2a = hardwareMap.get(DigitalChannel.class, "dig2a");

        analog2 = hardwareMap.get(AnalogInput.class, "analog2");
        dig3a = hardwareMap.get(DigitalChannel.class, "dig3a");
        dig4a = hardwareMap.get(DigitalChannel.class, "dig4a");

        analog3 = hardwareMap.get(AnalogInput.class, "analog3");
        dig5a = hardwareMap.get(DigitalChannel.class, "dig5a");
        dig6a = hardwareMap.get(DigitalChannel.class, "dig6a");
    }


    @Override
    public void loop() {
        double hue1 = analog1.getVoltage() / 3.3 * 360;
        double hue2 = analog2.getVoltage() / 3.3 * 360;
        double hue3 = analog3.getVoltage() / 3.3 * 360;

        if ((!dig2a.getState() && !dig1a.getState()) || (dig2a.getState() && dig1a.getState())) {
            if (hue1 < 115) {
                color1 = 0;
            } else if (hue1 < 128) {
                color1 = 2; //green
            } else if (hue1 < 170) {
                color1 = 1; //purple
            }

        } else {
            if (dig2a.getState()) {
                color1 = 2;
            } else {
                color1 = 1;
            }
        }

        //second:
        if ((!dig4a.getState() && !dig3a.getState()) || (dig4a.getState() && dig3a.getState())) {
            if (hue2 < 115) {
                color2 = 0;
            } else if (hue2 < 128) {
                color2 = 2; //green
            } else if (hue2 < 170) {
                color2 = 1; //purple
            }

        } else {
            if (dig4a.getState()) {
                color2 = 2;
            } else {
                color2 = 1;
            }
        }

        //third:
        if ((!dig6a.getState() && !dig5a.getState()) || (dig6a.getState() && dig5a.getState())) {
            if (hue3 < 115) {
                color3 = 0;
            } else if (hue3 < 128) {
                color3 = 2; //green
            } else if (hue3 < 170) {
                color3 = 1; //purple
            }

        } else {
            if (dig6a.getState()) {
                color3 = 2;
            } else {
                color3 = 1;
            }
        }

        if (color1 == 0) {
            telemetry.addLine("1: None");
        }
        if (color1 == 1) {
            telemetry.addLine("1: Purple");
        }
        if (color1 == 2) {
            telemetry.addLine("1: Green");
        }
        //second:
        if (color2 == 0) {
            telemetry.addLine("2: None");
        }
        if (color2 == 1) {
            telemetry.addLine("2: Purple");
        }
        if (color2 == 2) {
            telemetry.addLine("2: Green");
        }
        //third:
        if (color3 == 0) {
            telemetry.addLine("3: None");
        }
        if (color3 == 1) {
            telemetry.addLine("3: Purple");
        }
        if (color3 == 2) {
            telemetry.addLine("3: Green");
        }

        telemetry.addLine("Sensor 1:");
        telemetry.addData("Hue 1: ", hue1);
        telemetry.addData("Dig 1:", dig1a.getState());
        telemetry.addData("Dig 2:", dig2a.getState());

        telemetry.addLine("Sensor 2:");
        telemetry.addData("Hue 2: ", hue2);
        telemetry.addData("Dig 1:", dig3a.getState());
        telemetry.addData("Dig 2:", dig4a.getState());

        telemetry.addLine("Sensor 3:");
        telemetry.addData("Hue 3: ", hue3);
        telemetry.addData("Dig 1:", dig5a.getState());
        telemetry.addData("Dig 2:", dig6a.getState());


        telemetry.update();
    }
}