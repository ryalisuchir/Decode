package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.virtualGoalX;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.virtualGoalY;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.xVel;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.yVel;

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
            goalX = virtualGoalX;
            goalY = virtualGoalY;
        }

        double distance = TurretMath.getDistanceToGoalPinpoint(follower, goalX, goalY);
        double hoodDistance = getCompensatedDistance(
                distance,
                goalX,
                goalY,
                G.SHOOTER_TOF_COMP_GAIN_HOOD,
                G.SHOOTER_TOF_BLEND_HOOD
        );
        double velocityDistance = getCompensatedDistance(
                distance,
                goalX,
                goalY,
                G.SHOOTER_TOF_COMP_GAIN_VEL,
                G.SHOOTER_TOF_BLEND_VEL
        );
        ShooterParams hoodParams = shooterLUT.getShooterValue(hoodDistance);
        ShooterParams velocityParams = shooterLUT.getShooterValue(velocityDistance);

        hood.setPosition(
                clamp(hoodParams.hoodPos, G.HOOD_LOWERED, G.HOOD_MAX)
        );

        targetVelocity = velocityParams.shooterVel;

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

    private double getCompensatedDistance(
            double distance,
            double goalX,
            double goalY,
            double gain,
            double blend
    ) {
        if (!G.SHOOTER_TOF_COMP_ENABLED) return distance;
        if (Math.hypot(xVel, yVel) < G.SHOOTER_SOTM_MIN_SPEED) return distance;

        double tof = timeOfFlightLUT.get(distance);
        double radialVel = getRadialVelocityInPerSec(goalX, goalY);
        double predictedDistance = distance - (radialVel * tof * gain);
        double delta = clamp(predictedDistance - distance, -G.SHOOTER_TOF_MAX_DELTA_IN, G.SHOOTER_TOF_MAX_DELTA_IN);
        double filteredDistance = distance + delta * clamp(blend, 0.0, 1.0);
        return Math.max(0.0, filteredDistance);
    }

    private double getRadialVelocityInPerSec(double goalX, double goalY) {
        double dx = goalX - follower.getPose().getX();
        double dy = goalY - follower.getPose().getY();

        double dist = Math.hypot(dx, dy);
        if (dist < 1e-6) return 0.0;

        double ux = dx / dist;
        double uy = dy / dist;
        return xVel * ux + yVel * uy;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
