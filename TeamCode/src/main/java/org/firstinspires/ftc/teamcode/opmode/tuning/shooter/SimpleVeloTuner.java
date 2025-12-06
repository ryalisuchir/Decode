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
public class SimpleVeloTuner extends OpMode {
    Robot robot;
    public static double setPoint = 0;

    public static double P, F;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }


    public double getTarget() {
        return setPoint;
    }
    public double getVelocity() {
        return robot.shooterSpinner2.getCorrectedVelocity();
    }

    @Override
    public void loop() {
        F = 0.13;
        P = 0.004;
        double power;
        if (Math.abs(setPoint - robot.shooterSpinner2.get()) < 100 && setPoint !=0) {
            P = 0.0012;
        }

        power = (setPoint-robot.shooterSpinner2.getCorrectedVelocity()) * P;

       robot.shooterSpinner1.set(F + power);
        robot.shooterSpinner2.set(F + power);

        robot.clearCache();

        telemetry.addData("Current Velocity:", robot.shooterSpinner2.getCorrectedVelocity());
        telemetry.addData("Target Velocity:", setPoint);
        telemetry.update();
    }
}