package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
@Config
public class ShooterTuner extends OpMode {
    Robot robot;
    static double hoodPosition = Globals.HOOD_LOWERED;

    public static double setPoint = 0;

    private com.pedropathing.control.PIDFController b, s;

    public static double bp = 0.007, bd = 0.0, bf = 0.0, sp = 0.005, sd = 0, sf = 0.0;
    public static double pSwitch = 150;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        b = new com.pedropathing.control.PIDFController(new PIDFCoefficients(bp, 0, bd, bf));
        s = new PIDFController(new PIDFCoefficients(sp, 0, sd, sf));
    }

    public double getTarget() {
        return setPoint;
    }
    public double getVelocity() {
        return robot.shooterSpinner2.getCorrectedVelocity();
    }

    @Override
    public void loop() {
        b.setCoefficients(new PIDFCoefficients(bp, 0, bd, bf));
        s.setCoefficients(new PIDFCoefficients(sp, 0, sd, sf));

        if (Math.abs(getTarget() - getVelocity()) < pSwitch) {
            s.updateError(getTarget() - getVelocity());
            robot.shooterSpinner1.set(s.run());
            robot.shooterSpinner2.set(s.run());
        } else {
            b.updateError(getTarget() - getVelocity());
            robot.shooterSpinner1.set(b.run());
            robot.shooterSpinner2.set(b.run());
        }

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
}