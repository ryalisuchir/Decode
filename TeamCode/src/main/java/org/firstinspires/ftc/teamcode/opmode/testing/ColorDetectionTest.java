package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.DenoiseFilter;


//@TeleOp
public class ColorDetectionTest extends OpMode {
    Robot r;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
    }


    @Override
    public void loop() {
        double hue1 = r.c1.getVoltage() / 3.3 * 360;
        double hue2 = r.c2.getVoltage() / 3.3 * 360;
        double hue3 = r.c3.getVoltage() / 3.3 * 360;

        telemetry.addData("Hue 1: ", hue1);
        telemetry.addData("Hue 2: ", hue2);
        telemetry.addData("Hue 3: ", hue3);

        telemetry.addData("Distance 1: ", r.c1.getVoltage() / 3.3 * 100);
        telemetry.addData("Distance 2: ", r.c2.getVoltage() / 3.3 * 100);
        telemetry.addData("Distance 3: ", r.c3.getVoltage() / 3.3 * 100);

       telemetry.addData("Ball 1 Color: ", Globals.ballColors[0]);
        telemetry.addData("Ball 2 Color: ", Globals.ballColors[1]);
        telemetry.addData("Ball 3 Color: ", Globals.ballColors[2]);

        r.loop(r);
        telemetry.update();
    }
}
