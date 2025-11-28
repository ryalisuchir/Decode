package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
@Config
public class SpinnerTuner extends OpMode {
    Robot robot;
    public static double setPoint = 0;


    public static double P = 0.0001;
    public static double I = 0.0;
    public static double D = 0.0;
    public static double F = 0.0008;

    public PIDFController controller;

    public double power;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        controller = new PIDFController(P, I, D, F);
        controller.setTolerance(Globals.SHOOTER_VELOCITY_TOLERANCE);
        controller.setSetPoint(0);
    }

    @Override
    public void loop() {
        controller.setP(P);
        controller.setI(I);
        controller.setI(D);
        controller.setI(F);

        controller.setSetPoint(setPoint);
        power = controller.calculate(robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES), setPoint);

        robot.shooterSpinner1.setPower(power);
        robot.shooterSpinner2.setPower(power);

        robot.clearCache();

        telemetry.addData("Current Velocity:", robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Target Velocity:", setPoint);
        telemetry.addData("Current Power:", robot.shooterSpinner2.getPower());
        telemetry.update();
    }
}