package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
@Config
public class ShooterPositionTuner extends OpMode {
    Robot r;
    public static double hoodPosition = Globals.HOOD_MAX;

    public double kV = 0.00045;
    public double kS = 0.02;
    public double kP = 0.0012;
    public static double targetVelocity = 0;
    public static double rotatorPower = 0;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.BLUE_FAR_START, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }


    @Override
    public void loop() {
        double currentVel = r.s2.getCorrectedVelocity();

        double ff = feedforward(targetVelocity);
        double fb = feedback(targetVelocity, currentVel);

        double power = ff + fb;
        power = clamp(power, 0, 1);

        // Apply to both shooter motors
        r.s1.set(power);
        r.s2.set(power);
        r.i.setPower(rotatorPower);
        r.t.setPower(rotatorPower);

        r.clearCache();
        r.dt.periodic();

        if (hoodPosition < Globals.HOOD_LOWERED) hoodPosition = Globals.HOOD_LOWERED;
        if (hoodPosition > Globals.HOOD_MAX) hoodPosition = Globals.HOOD_MAX;

        r.r.setPosition(hoodPosition);

        telemetry.addData("Distance: ", r.dt.getGoalDistance());
        telemetry.addData("Current Velocity: ", r.s2.getCorrectedVelocity());
        telemetry.addData("Hood Value: ", r.r.getPosition());
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