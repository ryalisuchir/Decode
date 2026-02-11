package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;

@Autonomous
@Config
public class MasterChecker extends OpMode {
    Halo robot;
    public static double kicker1 = G.KICKER1_RESET;
    public static double kicker2 = G.KICKER2_RESET;
    public static double kicker3 = G.KICKER3_RESET;
    public static double turret = G.TURRET_RESET;
    public static double hood = G.HOOD_MAX;
    public static double gate = G.GATE_OPEN;

    public static double shooterSpeed = 0;
    public static double intakeSpeed = 0;
    public static double transferSpeed = 0;

    public static double dtSpeed1 = 0;
    public static double dtSpeed2 = 0;
    public static double dtSpeed3 = 0;
    public static double dtSpeed4 = 0;

    @Override
    public void init() {
        robot = new Halo(hardwareMap, G.RED_CUBE_START, G.Side.RED, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Reset all encoders.");
        telemetry.update();
        Vision.switchToRegular();

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

        robot.t.setPower(transferSpeed);
        robot.i.setPower(intakeSpeed);

        robot.s1.set(shooterSpeed);
        robot.s2.set(shooterSpeed);

        Pose fpose = robot.dt.getFollower().getPose();

        telemetry.addData("Transfer Motor Velocity:", robot.t.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("tx to Goal:", Vision.getTx());
        telemetry.addData("Distance to goal (pp): ", robot.dt.getGoalDistance());
        telemetry.addData("Distance to goal (ll): ", Vision.distanceFromTag());
        telemetry.addData("Fiducial: ", Halo.l.getLatestResult().getFiducialResults());
        telemetry.addData("Shooter 1 Motor Velocity:", robot.s1.getCorrectedVelocity());
        telemetry.addData("Shooter 2 Motor Velocity:", robot.s2.getCorrectedVelocity());
        telemetry.addData("Shooter 1 Motor RPM: ", robot.s1.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Shooter 2 Motor RPM: ", robot.s2.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Transfer Velocity: ", robot.t.getVelocity(AngleUnit.DEGREES));
        telemetry.addData("Obelisk:", G.obeliskOptions);
        telemetry.addData("Pinpoint x:", fpose.getX());
        telemetry.addData("Pinpoint y:", fpose.getY());
        telemetry.addData("Pinpoint heading:", Math.toDegrees(fpose.getHeading()));
        telemetry.addData("Turret Heading: ", getTurretAngleToGoal(fpose.getX(), fpose.getY(), fpose.getHeading()));

        telemetry.update();
        robot.clearCache();
        robot.dt.loop();
        robot.updateVision();
    }

    public double getTurretAngleToGoal(
            double robotX,
            double robotY,
            double robotHeadingRadians
    ) {
        double dx = G.RED_CASTLE.getX() - robotX;
        double dy = G.RED_CASTLE.getY() - robotY;
        double angleToGoal = Math.atan2(dy, dx);

        double turretAngle = angleToGoal - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }
}
