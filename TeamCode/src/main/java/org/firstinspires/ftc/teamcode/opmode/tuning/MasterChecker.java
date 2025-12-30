package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

import kotlin.Unit;

@Autonomous
@Config
public class MasterChecker extends OpMode {
    Robot robot;
    public static double kicker1 = Globals.KICKER1_RESET;
    public static double kicker2 = Globals.KICKER2_RESET;
    public static double kicker3 = Globals.KICKER3_RESET;
    public static double turret = Globals.TURRET_RESET;
    public static double hood = Globals.HOOD_MAX;
    public static double gate = Globals.GATE_OPEN;

    public static double shooterSpeed = 0;
    public static double rollerSpeed = 0;

    public static double dtSpeed1 = 0;
    public static double dtSpeed2 = 0;
    public static double dtSpeed3 = 0;
    public static double dtSpeed4 = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Reset all encoders.");
        telemetry.update();

    }

    @Override
    public void loop() {
        robot.k1.setPosition(kicker1);
        robot.k2.setPosition(kicker2);
        robot.k3.setPosition(kicker3);

        robot.t1.setPosition(turret);
        robot.t2.setPosition(turret);
        robot.r.setPosition(hood);
        robot.g.setPosition(gate);

        robot.fl.setPower(dtSpeed1);
        robot.fr.setPower(dtSpeed2);
        robot.rl.setPower(dtSpeed3);
        robot.rr.setPower(dtSpeed4);

        robot.t.setPower(rollerSpeed);
        robot.i.setPower(rollerSpeed);
        robot.s1.set(shooterSpeed);
        robot.s2.set(shooterSpeed);

        Pose fpose = robot.dt.getFollower().getPose();

        telemetry.addData("Transfer Motor Velocity:", robot.t.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Shooter 1 Motor Velocity:", robot.s1.getCorrectedVelocity());
        telemetry.addData("Shooter 2 Motor Velocity:", robot.s2.getCorrectedVelocity());
        telemetry.addData("Shooter 1 Motor RPM: ", robot.s1.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Shooter 2 Motor RPM: ", robot.s2.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Transfer Velocity: ", robot.t.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Obelisk:", Globals.obeliskOptions);
        telemetry.addData("Pinpoint x:", fpose.getX());
        telemetry.addData("Pinpoint y:", fpose.getY());
        telemetry.addData("Pinpoint heading:", Math.toDegrees(fpose.getHeading()));

        telemetry.update();
        robot.dt.loop();
        robot.clearCache();
//        robot.loop(robot);
    }
}