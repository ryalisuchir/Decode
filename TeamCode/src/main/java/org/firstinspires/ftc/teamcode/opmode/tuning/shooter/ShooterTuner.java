package org.firstinspires.ftc.teamcode.opmode.tuning.shooter;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
@Config
public class ShooterTuner extends OpMode {
    Robot robot;
    public static double hoodPosition = Globals.HOOD_MAX;

    public  double kV = 0.00045;
    public  double kS = 0.02;
    public  double kP = 0.0012;
    public static double targetVelocity = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }



    @Override
    public void loop() {
        double currentVel = robot.shooterSpinner2.getCorrectedVelocity();

        double ff = feedforward(targetVelocity);
        double fb = feedback(targetVelocity, currentVel);

        double power = ff + fb;
        power = clamp(power, 0, 1);

        // Apply to both shooter motors
        robot.shooterSpinner1.set(power);
        robot.shooterSpinner2.set(power);

        robot.clearCache();

        if (hoodPosition < Globals.HOOD_LOWERED) hoodPosition = Globals.HOOD_LOWERED;
        if (hoodPosition > Globals.HOOD_MAX) hoodPosition = Globals.HOOD_MAX;

        robot.hood.setPosition(hoodPosition);

        telemetry.addData("Distance: ", robot.getDistanceToGoalPinpoint());
        telemetry.addData("Current Velocity: ", robot.shooterSpinner2.getCorrectedVelocity());
        telemetry.addData("Hood Value: ", robot.hood.getPosition());
        telemetry.update();

        robot.clearCache();
        robot.follower.update();
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