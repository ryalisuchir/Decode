package org.firstinspires.ftc.teamcode.opmode.testing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.common.robot.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.RedTurretLUT;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class TurretTester extends OpMode {
    Robot robot;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        robot.follower.startTeleOpDrive();
    }


    @Override
    public void loop() {
        double servoPosition;

        robot.follower.setTeleOpDrive(
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_y),
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_x),
                -0.5 * Math.tan(1.12 * gamepad1.right_stick_x),
                true);

        servoPosition = blueTurretLUT.getServoValue(robot.getTurretAngleToGoal(robot.follower.getPose().getX(), robot.follower.getPose().getY(), robot.follower.getPose().getHeading()));

        robot.turret1.setPosition(servoPosition);
        robot.turret2.setPosition(servoPosition);

        robot.clearCache();
        robot.follower.update();
    }
}
