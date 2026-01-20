package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.shooter.ShooterLUT;

public class Shooter extends SubsystemBase {

    public static double kV = 0.00045;
    public static double kS = 0.02;
    public static double kP = 0.0013;

    public boolean reached = false;

    private double targetVelocity = 0.0;
    private final double lastTargetVelocity = 0.0;
    private double lastShooterPower = -999.0;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;

    private final double gX, gY;
    private Vector2d customPosition = null;

    private final ShooterLUT closeShooterLUT = new ShooterLUT();

    /* ================= CONSTRUCTOR ================= */

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
        this.gX = gX;
        this.gY = gY;
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if (Globals.match == Globals.Match.AUTO &&
                    Globals.turretState != Globals.TurretState.BLUE_CLOSE_OBELISK &&
                    Globals.turretState != Globals.TurretState.RED_CLOSE_OBELISK &&
                    Globals.turretState != Globals.TurretState.RED_FAR_GOAL &&
            Globals.turretState != Globals.TurretState.RED_CLOSE_DIFF_GOAL &&
                    Globals.turretState != Globals.TurretState.RED_CLOSE_GOAL &&
                    Globals.turretState != Globals.TurretState.BLUE_CLOSE_DIFF_GOAL &&
                    Globals.turretState != Globals.TurretState.BLUE_CLOSE_GOAL &&
                    Globals.turretState != Globals.TurretState.BLUE_FAR_GOAL
            ) {
                Globals.turretState = Globals.TurretState.FOLLOWING;
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
            Globals.turretState = Globals.TurretState.BLUE_CLOSE_OBELISK;
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

        reached = shooterIsSpunUp();

        if (Globals.shooterState != Globals.ShooterState.SHOOTING &&
                Globals.match != Globals.Match.AUTO) {
            setShooterPower(Globals.MIN_SHOOTER_POWER);
            return;
        }

        double x, y;
        if (customPosition != null) {
            x = customPosition.getX() - gX;
            y = customPosition.getY() - gY;
        } else {
            x = follower.getPose().getX() - gX;
            y = follower.getPose().getY() - gY;
        }

        double distance = Math.hypot(x, y);

        ShooterParams params = closeShooterLUT.getShooterValue(distance);

        hood.setPosition(
                clamp(params.hoodPos, Globals.HOOD_LOWERED, Globals.HOOD_MAX)
        );

        targetVelocity = params.shooterVel;

        double currentVelocity = -shooterMotor2.getCorrectedVelocity();
        double error = targetVelocity - currentVelocity;

        double feedforward = 0.0;
        if (Math.abs(targetVelocity) > 1e-6) {
            feedforward = kV * targetVelocity;
            if (error > 0) {
                feedforward += kS;
            }
        }

        double feedback = kP * error;

        double power = clamp(
                feedforward + feedback,
                -1.0,
                1.0
        );

        setShooterPower(power);
    }

    private void setShooterPower(double power) {

        boolean decelerating = (-shooterMotor2.getCorrectedVelocity() > targetVelocity);

        double threshold = decelerating ? 0.005 : 0.02;

        if (Math.abs(power - lastShooterPower) > threshold) {
            shooterMotor1.set(power);
            shooterMotor2.set(power);
            lastShooterPower = power;
        }
    }

    public boolean shooterIsSpunUp() {
        return Math.abs(
                -shooterMotor2.getCorrectedVelocity() - targetVelocity
        ) < Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    public double getShooterVelocity() {
        return -shooterMotor2.getCorrectedVelocity();
    }

    public double getShooterRPM() {
        return getShooterVelocity() / 28.0 * 60.0;
    }

    public double getShooterPower() {
        return shooterMotor2.get();
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}