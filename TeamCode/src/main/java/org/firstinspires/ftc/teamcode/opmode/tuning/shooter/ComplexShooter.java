package org.firstinspires.ftc.teamcode.opmode.tuning.shooter;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

//@TeleOp
@Config
public class ComplexShooter extends OpMode {

    Robot robot;

    public static double kV = 0.00045;
    public static double kS = 0.02;
    public static double kP = 0.0012;
    public static double targetVelocity = 0;

    private long lastLoopTime = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        lastLoopTime = System.nanoTime();
    }

    @Override
    public void loop() {
        // Compute dt

        double currentVel = robot.shooterSpinner2.getCorrectedVelocity();

        double ff = feedforward(targetVelocity);
        double fb = feedback(targetVelocity, currentVel);

        double power = ff + fb;
        power = clamp(power, 0, 1);

        // Apply to both shooter motors
        robot.shooterSpinner1.set(power);
        robot.shooterSpinner2.set(power);

        robot.clearCache();

        telemetry.addData("Target Velocity", targetVelocity);
        telemetry.addData("Current Velocity", currentVel);
        telemetry.addData("Power", power);
        telemetry.update();
    }

    private double feedforward(double targetVel) {
        if (Math.abs(targetVel) < 1e-6) return 0;
        double sign = Math.signum(targetVel);
        return kS * sign + kV * targetVel;
    }

    private double feedback(double targetVel, double currentVel) {
        double error = targetVel - currentVel;
        return kP * error;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
