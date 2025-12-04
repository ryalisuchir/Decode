package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;
import org.firstinspires.ftc.teamcode.common.robot.ShooterLUT;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterSubsystem extends SubsystemBase {

    private final ShooterLUT shooterLUT = new ShooterLUT();
    double goalX, goalY;
    private double lastTransferPower = -999;
    private double lastShooterPower = -999;

    public static double kV = 0.00045;
    public static double kS = 0.02;
    public static double kP = 0.0013;
    public static double targetVelocity = 0;
    public boolean reached = false;

    public final Motor shooterMotor1, shooterMotor2;
    public final DcMotorEx transferMotor;
    public final ServoImplEx hood;
    public final Follower follower;

    public ShooterSubsystem(Motor shooterMotor1, Motor shooterMotor2, DcMotorEx transferMotor, ServoImplEx hood, Follower follower, double goalX, double goalY) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.transferMotor = transferMotor;
        this.hood = hood;
        this.follower = follower;
        this.goalX = goalX;
        this.goalY = goalY;
    }

    private void setTransferPowerOnce(double power) {
        if (power != lastTransferPower) {
            transferMotor.setPower(power);
            lastTransferPower = power;
        }
    }

    public boolean shooterIsSpunUp() {
        return Math.abs(shooterMotor2.getCorrectedVelocity()-targetVelocity) < Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    private void setShooterPowerOnce(double power) {
        if (power != lastShooterPower) {
            shooterMotor1.set(power);
            shooterMotor2.set(power);
            lastShooterPower = power;
        }
    }

    public void syncer() {
        reached = shooterIsSpunUp();
        if (Globals.shooterState == Globals.ShooterState.SHOOTING) {
            double dxOdo = follower.getPose().getX() - goalX;
            double dyOdo = follower.getPose().getY() - goalY;

            ShooterParams currentShooterParam = shooterLUT.getShooterValue(Math.hypot(dxOdo, dyOdo));

            double hoodPos = currentShooterParam.hoodPos;
            if (hoodPos < Globals.HOOD_LOWERED) hoodPos = Globals.HOOD_LOWERED;
            if (hoodPos > Globals.HOOD_MAX) hoodPos = Globals.HOOD_MAX;

            hood.setPosition(hoodPos);
            targetVelocity = currentShooterParam.shooterVel;

            double currentVel = shooterMotor2.getCorrectedVelocity();

            double ff = feedforward(targetVelocity);
            double fb = feedback(targetVelocity, currentVel);

            double power = clamp(ff + fb, 0, 1);

            shooterMotor2.set(power);
            shooterMotor1.set(power);
        } else {
            setShooterPowerOnce(Globals.MIN_SHOOTER_POWER);
        }

        if (Globals.transferState == Globals.TransferState.TRANSFERRING) {
            setTransferPowerOnce(Globals.MAX_TRANSFER_POWER);
        } else {
            setTransferPowerOnce(0);
        }

    }
    private double feedforward(double targetVel) {
        if (Math.abs(targetVel) < 1e-6) return 0;
        double sign = Math.signum(targetVel);
        return kS * sign + kV * targetVel;
    }

    private double feedback(double targetVel, double currentVel) {
        double error = targetVel - currentVel;
        return kP * error;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

}
