package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.TimeOfFlightLUT;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.shooter.ShooterLUT;


public class Shooter extends SubsystemBase { //new pidf system rather than relying on ramp down

    private final PIDFController flywheelController = new PIDFController(
            new PIDFCoefficients(
                    0.0012,
                    0,
                    0,
                    0.0004
            )
    );

    public boolean reached = false;

    private double targetVelocity = 0.0;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;
    private final double fixedGoalX;
    private final double fixedGoalY;

    private Vector2d customPosition = null;

    private final ShooterLUT shooterLUT = new ShooterLUT();
    private final TimeOfFlightLUT timeOfFlightLUT = new TimeOfFlightLUT();

    public Shooter(
            Motor shooterMotor1,
            Motor shooterMotor2,
            ServoImplEx hood,
            Follower follower,
            double gX,
            double gY
    ) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        this.follower = follower;
        this.fixedGoalX = gX;
        this.fixedGoalY = gY;
        flywheelController.setTolerance(G.SHOOTER_VELOCITY_TOLERANCE);
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() -> {
            G.shooterState = G.ShooterState.SHOOTING;
            if (G.match == G.Match.AUTO &&
                    G.turretState != G.TurretState.BLUE_CLOSE_OBELISK &&
                    G.turretState != G.TurretState.RED_CLOSE_OBELISK &&
                    G.turretState != G.TurretState.SET_POSITION
            ) {
                G.turretState = G.TurretState.FOLLOWING;
            }
        });
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() -> {
            G.shooterState = G.ShooterState.STOPPED;
            G.turretState = G.TurretState.RESET;
            targetVelocity = 0;
        });
    }

    public InstantCommand stopShooterFollow() {
        return new InstantCommand(() -> {
            G.shooterState = G.ShooterState.STOPPED;
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

        if (G.shooterState != G.ShooterState.SHOOTING &&
                G.match != G.Match.AUTO) {
            setShooterPower(G.MIN_SHOOTER_POWER);
            return;
        }

        double goalX;
        double goalY;

        if (customPosition != null) {
            goalX = customPosition.getX();
            goalY = customPosition.getY();
        } else {
            goalX = fixedGoalX;
            goalY = fixedGoalY;
        }

        double vx = follower.getVelocity().getXComponent();
        double vy = follower.getVelocity().getYComponent();
        double ledGoalX = goalX;
        double ledGoalY = goalY;
        if (G.SHOOTER_SOTM_ENABLED) {
            double baseDistance = TurretMath.getDistanceToGoalPinpoint(follower, goalX, goalY);
            double tof = timeOfFlightLUT.get(baseDistance);
            ledGoalX = goalX - (vx * tof);
            ledGoalY = goalY - (vy * tof);
        }

        double distance = TurretMath.getDistanceToGoalPinpoint(follower, ledGoalX, ledGoalY);
        ShooterParams params = shooterLUT.getShooterValue(distance);

        hood.setPosition(
                clamp(params.hoodPos, G.HOOD_LOWERED, G.HOOD_MAX)
        );

        double velocityOffset = G.SHOOTER_SOTM_ENABLED ? shotVelocityOffset(vx, vy) * G.SHOOTER_SOTM_RPM_GAIN : 0.0;
        targetVelocity = params.shooterVel + velocityOffset;

        double flywheelVel = shooterMotor1.getCorrectedVelocity();

        flywheelController.setSetPoint(Math.min(targetVelocity, 2550));
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
        return shooterMotor1.getCorrectedVelocity();
    }

    public double getShooterGoal() {
        return targetVelocity;
    }

    public double getShooterRPM() {
        return getShooterVelocity() / 28.0 * 60.0;
    }

    public double getShooterPower() {
        return shooterMotor1.get();
    }

    private double shotVelocityOffset(double xVelInPerSec, double yVelInPerSec) {
        double velVectorMagnitudeMps = Math.hypot(
                Math.max(xVelInPerSec, 0.0),
                Math.min(yVelInPerSec, 0.0)
        ) * 0.0254;
        return ((velVectorMagnitudeMps * 1000.0) / (G.SHOOTER_SOTM_WHEEL_DIAMETER_MM * Math.PI)) * 60.0;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
