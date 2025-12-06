package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

//@Autonomous
@Config
public class TurretTuner extends OpMode {
    Robot robot;
    public static double turretPosition = Globals.TURRET_RESET;

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double goalX = Globals.BLUE_CASTLE.getX();
        double goalY = Globals.BLUE_CASTLE.getY();

        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

        return turretAngle;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Reset all encoders.");
        robot.follower.startTeleOpDrive();
        telemetry.update();
    }


    static double change(double val, int dp) {
        val = val*Math.pow(10,dp);
        val=Math.floor(val);
        val=val/Math.pow(10,dp);

        return val;
    }

    @Override
    public void loop() {

        robot.follower.setTeleOpDrive(
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_y),
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_x),
                -0.5 * Math.tan(1.12 * gamepad1.right_stick_x),
                true);
        robot.turret1.setPosition(turretPosition);
        robot.turret2.setPosition(turretPosition);

        telemetry.addData("Follower Heading: ", robot.follower.getHeading());
        telemetry.addData("Turret Reading: ", (change(getTurretAngleToGoal(robot.follower.getPose().getX(), robot.follower.getPose().getY(), robot.follower.getHeading()),6)));
        telemetry.update();
        robot.clearCache();
        robot.follower.update();
    }
}