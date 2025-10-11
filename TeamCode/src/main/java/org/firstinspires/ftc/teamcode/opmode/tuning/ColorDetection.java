package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp(name = "Color Sensor")
public class ColorDetection extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap, new Pose(0, 0, Math.toRadians(0)), true);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

        }
    }
}