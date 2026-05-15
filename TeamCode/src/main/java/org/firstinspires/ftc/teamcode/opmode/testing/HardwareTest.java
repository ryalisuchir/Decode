package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.TurretConfig;
import org.firstinspires.ftc.teamcode.common.utility.Vision;

@Autonomous
@Config
@Configurable
public class HardwareTest extends OpMode {
    Halo robot;
    public static double kicker1 = Globals.Kicker.KICKER1.getMin();
    public static double kicker2 = Globals.Kicker.KICKER2.getMin();
    public static double kicker3 = Globals.Kicker.KICKER3.getMin();
    public static double turret = TurretConfig.TURRET_FORWARD;
    public static double hood = Globals.HOOD.getMin();
    public static double gate = Globals.Gate.GATE_OPEN;
    public static double pivot = Globals.Pivot.PIVOT_RAISED;

    public static double shooterSpeed = 0;
    public static double intakeSpeed = 0;
    public static double transferSpeed = 0;

    public static double dtSpeed1 = 0;
    public static double dtSpeed2 = 0;
    public static double dtSpeed3 = 0;
    public static double dtSpeed4 = 0;

    @Override
    public void init() {
        robot = new Halo(hardwareMap, Globals.Positions.BLUE_CUBE_START, Globals.Alliance.RED, Globals.Match.TESTING);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Reset all encoders.");
        telemetry.update();
        Vision.switchToRegular();
        FtcDashboard.getInstance().startCameraStream(Halo.ll, 0);
    }

    @Override
    public void loop() {
        robot.k1.setPosition(kicker1);
        robot.k2.setPosition(kicker2);
        robot.k3.setPosition(kicker3);

        robot.t1.setPosition(turret);
        robot.t2.setPosition(turret);
        robot.hood.setPosition(hood);
        robot.gate.setPosition(gate);
        robot.pivot.setPosition(pivot);

        robot.fl.setPower(dtSpeed1);
        robot.fr.setPower(dtSpeed2);
        robot.rl.setPower(dtSpeed3);
        robot.rr.setPower(dtSpeed4);

        robot.transfer.setPower(transferSpeed);
        robot.intake.setPower(intakeSpeed);

        robot.shooter1.set(shooterSpeed);
        robot.shooter2.set(shooterSpeed);

        Pose fpose = robot.dt.getFollower().getPose();


        telemetry.addData("tx to Goal:", Vision.getTx());
        telemetry.addData("Distance to goal (pp): ", robot.dt.getGoalDistance());
        telemetry.addData("Distance to goal (ll): ", Vision.distanceFromTag());
        telemetry.addData("Fiducial: ", Halo.ll.getLatestResult().getFiducialResults());
        telemetry.addData("Shooter 1 Motor Velocity:", robot.shooter1.getCorrectedVelocity());
        telemetry.addData("Shooter 2 Motor Velocity:", robot.shooter2.getCorrectedVelocity());
        telemetry.addData("Shooter 1 Motor RPM: ", robot.shooter1.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Shooter 2 Motor RPM: ", robot.shooter2.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Obelisk:", Globals.obeliskOptions);
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
        double dx = Globals.Positions.RED_GOAL.getX() - robotX;
        double dy = Globals.Positions.RED_GOAL.getY() - robotY;
        double angleToGoal = Math.atan2(dy, dx);

        double turretAngle = angleToGoal - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }
}
