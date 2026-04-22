package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.utility.ShooterLUT;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

public class Shooter extends SubsystemBase {

    private final PIDFController flywheelController = new PIDFController(Globals.shooterCoefficients);

    public boolean reached = false;

    private double targetVelocity = 0.0;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;

    private Vector2d customPosition = null;

    private final ShooterLUT shooterLUT = new ShooterLUT();
    public double hoodPose, velPos;

    public Shooter(
            Motor shooterMotor1,
            Motor shooterMotor2,
            ServoImplEx hood,
            Follower follower
    ) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        this.follower = follower;
        flywheelController.setTolerance(Globals.SHOOTER_VEL_TOLERANCE);
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if (
                    (Globals.match == Globals.Match.AUTO &&
                    Globals.turretState != Globals.TurretState.SET_POSITION &&
                    Globals.turretState != Globals.TurretState.FOLLOWING_OBELISK) ||
                    Globals.match == Globals.Match.TESTING
            ) {
                Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
            }
        });
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.turretState = Globals.TurretState.RESET;
            targetVelocity = 0;
        });
    }

    public InstantCommand stopShooterFollow() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            targetVelocity = 0;
        });
    }

    public void setCustomDistance(double x, double y) {
        this.customPosition = new Vector2d(x, y);
    }

    public void clearCustomDistance() {
        this.customPosition = null;
    }

    public void loop() {

        reached = flywheelController.atSetPoint();

        if (Globals.shooterState != Globals.ShooterState.SHOOTING &&
                Globals.match != Globals.Match.AUTO) {
            setShooterPower(Globals.MIN_SHOOTER_POWER);
            return;
        }

        double vx = follower.getVelocity().getXComponent();
        double vy = follower.getVelocity().getYComponent();
        Pose lutInputPose = (customPosition != null)
                ? new Pose(customPosition.getX(), customPosition.getY())
                : new Pose(follower.getPose().getX(), follower.getPose().getY());
        ShooterParams params = shooterLUT.getShooterValue(lutInputPose, Globals.alliance);

        hood.setPosition(
                clamp(params.hoodPos, Globals.HOOD.getMin(), Globals.HOOD.getMax())
        );

        double velocityOffset = Globals.SOTM.SHOOTER_ENABLED ? shotVelocityOffset(vx, vy) * Globals.SOTM.SHOOTER_RPM_GAIN : 0.0;
        targetVelocity = params.shooterVel + velocityOffset;

        double flywheelVel = shooterMotor2.getCorrectedVelocity();

        velPos = targetVelocity;
        hoodPose = params.hoodPos;

        flywheelController.setSetPoint(Math.min(targetVelocity, 2700));
        double power = flywheelController.calculate(flywheelVel);

        setShooterPower(power);
    }

    private void setShooterPower(double power) {
        shooterMotor1.set(power);
        shooterMotor2.set(power);
    }

    public boolean shooterIsSpunUp() {
        return flywheelController.atSetPoint();
    }

    public double getShooterVelocity() {
        return shooterMotor2.getCorrectedVelocity();
    }

    public double getShooterGoal() {
        return targetVelocity;
    }

    public double getShooterRPM() {
        return getShooterVelocity() / 28.0 * 60.0;
    }

    public double getShooterPower() {
        return shooterMotor2.get();
    }

    private double shotVelocityOffset(double xVelInPerSec, double yVelInPerSec) {
        double velVectorMagnitudeMps = Math.hypot(
                Math.max(xVelInPerSec, 0.0),
                Math.min(yVelInPerSec, 0.0)
        ) * 0.0254;
        return ((velVectorMagnitudeMps * 1000.0) / (Globals.SOTM.SHOOTER_WHEEL_DIAMETER_MM * Math.PI)) * 60.0;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
