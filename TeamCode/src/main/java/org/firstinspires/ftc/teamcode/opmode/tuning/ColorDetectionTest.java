package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class ColorDetectionTest extends OpMode {

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, new Pose(0, 0, Math.toRadians(0)), Globals.Side.BLUE, true);
    }

    @Override
    public void loop() {
//        robot.determineColor();
        telemetry.addData("Color 1 (close): ", Globals.ballColor1);
        telemetry.addData("Color 2 (middle): ", Globals.ballColor2);
        telemetry.addData("Color 3 (far): ", Globals.ballColor3);
    }
}