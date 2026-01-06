package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.shooter.ShooterLUT;

public class Shooter extends SubsystemBase {

    private final ShooterLUT closeShooterLUT = new ShooterLUT();

    public static double kV = 0.00045;
    public static double kS = 0.02;
    public static double kP = 0.0013;
    public boolean reached = false;

    private double targetVelocity = 0;
    private double lastShooterPower = -999;
    Spinner r;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;
    private Vector2d customPosition = null;

    private final double gX, gY;

    public Shooter(Motor shooterMotor1, Motor shooterMotor2,
                   ServoImplEx hood, Follower follower,
                   double gX, double gY, Spinner r) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        this.follower = follower;
        this.gX = gX;
        this.gY = gY;
        this.r = r;
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if (Globals.match == Globals.Match.AUTO && Globals.turretState != Globals.TurretState.BLUE_CLOSE_OBELISK && Globals.turretState != Globals.TurretState.RED_CLOSE_OBELISK) {
                Globals.turretState = Globals.TurretState.FOLLOWING;
            }
        });
    }

    public void setCustomDistance(double x, double y) {
        this.customPosition = new Vector2d(x, y);
    }

    public void clearCustomDistance() {
        this.customPosition = null;
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.turretState = Globals.TurretState.RESET;
        });
    }

    public InstantCommand stopShooterFollow() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.turretState = Globals.TurretState.BLUE_CLOSE_OBELISK;
        });
    }

    public void loop() {
        reached = shooterIsSpunUp();

        if (Globals.shooterState != Globals.ShooterState.SHOOTING && Globals.match != Globals.Match.AUTO) {
            shooterMotor1.set(Globals.MIN_SHOOTER_POWER);
            shooterMotor2.set(Globals.MIN_SHOOTER_POWER);
            return;
        }

        double x;
        double y;
        if (customPosition != null) {
            x = customPosition.getX() - gX;
            y = customPosition.getY() - gY;
        } else {
            x = follower.getPose().getX() - gX;
            y = follower.getPose().getY() - gY;
        }

        ShooterParams params = closeShooterLUT.getShooterValue(Math.hypot(x,y), -shooterMotor2.getCorrectedVelocity());

        double hoodPos = clamp(params.hoodPos,
                Globals.HOOD_LOWERED,
                Globals.HOOD_MAX);

        hood.setPosition(hoodPos);

        targetVelocity = params.shooterVel;

        double currentVel = -shooterMotor2.getCorrectedVelocity();

        double power = clamp(
                feedforward(targetVelocity) +
                        feedback(targetVelocity, currentVel),
                0, 1
        );

        setShooterPowerOnce(power);
    }

    public boolean shooterIsSpunUp() {
        return Math.abs(
                -shooterMotor2.getCorrectedVelocity() - targetVelocity
        ) < Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    public double getShooterVelocity() {
        return shooterMotor2.getCorrectedVelocity();
    }

    public double getShooterRPM() {
        return shooterMotor2.getCorrectedVelocity() / 28 * 60;
    }

    public double getShooterPower() {
        return shooterMotor2.get();
    }

    private void setShooterPowerOnce(double power) {
        if (Math.abs(power - lastShooterPower) > 0.02) {
            shooterMotor1.set(power);
            shooterMotor2.set(power);
            lastShooterPower = power;
        }
    }

    private double feedforward(double targetVel) {
        if (Math.abs(targetVel) < 1e-6) return 0;
        return kS * Math.signum(targetVel) + kV * targetVel;
    }

    private double feedback(double targetVel, double currentVel) {
        return kP * (targetVel - currentVel);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}