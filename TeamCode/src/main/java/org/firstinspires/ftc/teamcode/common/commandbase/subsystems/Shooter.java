package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.ShooterLUT;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;

public class Shooter extends SubsystemBase {

    private final ShooterLUT shooterLUT = new ShooterLUT();

    public static double kV = 0.00045;
    public static double kS = 0.02;
    public static double kP = 0.0013;

    private double targetVelocity = 0;
    private double lastShooterPower = -999;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;

    private final double gX, gY;

    public Shooter(Motor shooterMotor1, Motor shooterMotor2,
                   ServoImplEx hood, Follower follower,
                   double gX, double gY) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        this.follower = follower;
        this.gX = gX;
        this.gY = gY;
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() ->
                Globals.shooterState = Globals.ShooterState.SHOOTING
        );
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() ->
                Globals.shooterState = Globals.ShooterState.STOPPED
        );
    }

    public void loop() {
        if (Globals.shooterState != Globals.ShooterState.SHOOTING) {
            setShooterPowerOnce(Globals.MIN_SHOOTER_POWER);
            return;
        }

        double dx = follower.getPose().getX() - gX;
        double dy = follower.getPose().getY() - gY;
        double distance = Math.hypot(dx, dy);

        ShooterParams params = shooterLUT.getShooterValue(distance);

        double hoodPos = clamp(params.hoodPos,
                Globals.HOOD_LOWERED,
                Globals.HOOD_MAX);

        hood.setPosition(hoodPos);

        targetVelocity = params.shooterVel;

        double currentVel = shooterMotor2.getCorrectedVelocity();

        double power = clamp(
                feedforward(targetVelocity) +
                        feedback(targetVelocity, currentVel),
                0, 1
        );

        setShooterPowerOnce(power);
    }

    public boolean shooterIsSpunUp() {
        return Math.abs(
                shooterMotor2.getCorrectedVelocity() - targetVelocity
        ) < Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    public double getShooterVelocity() {
        return shooterMotor2.getCorrectedVelocity();
    }

    public double getShooterPower() {
        return shooterMotor2.get();
    }

    private void setShooterPowerOnce(double power) {
        if (power != lastShooterPower) {
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
