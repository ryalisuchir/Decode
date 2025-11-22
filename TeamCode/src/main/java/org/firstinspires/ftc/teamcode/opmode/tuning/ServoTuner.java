package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@Autonomous
@Config
public class ServoTuner extends OpMode {
    Robot robot;
    public static double kicker1 = Globals.KICKER1_RESET;
    public static double kicker2 = Globals.KICKER2_RESET;
    public static double kicker3 = Globals.KICKER3_RESET;
    public static double turret = 0;
    public static double hood = 0;

    public static double transferSpeed = 0;
    public static double shooterSpeed = 0;
    public static double intakeSpeed = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, new Pose(0, 0, Math.toRadians(0)), true);
        telemetry.addLine("Reset all encoders.");
        telemetry.update();

    }

    @Override
    public void loop() {
        robot.kicker1.setPosition(kicker1);
        robot.kicker2.setPosition(kicker2);
        robot.kicker3.setPosition(kicker3);
        robot.turret1.setPosition(turret);
        robot.turret2.setPosition(turret);
        robot.hood.setPosition(hood);

        robot.transfer.setPower(transferSpeed);
        robot.shooterSpinner1.setPower(shooterSpeed);
        robot.shooterSpinner2.setPower(shooterSpeed);
        robot.intake.setPower(intakeSpeed);

        telemetry.addData("Transfer Motor Velocity:", robot.transfer.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter 1 Motor Velocity:", robot.shooterSpinner1.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter 2 Motor Velocity:", robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES));

        telemetry.update();
    }
}