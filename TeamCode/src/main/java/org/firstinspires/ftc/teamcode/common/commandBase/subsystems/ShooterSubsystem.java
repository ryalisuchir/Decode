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

    public static double setPoint = 0;
    private PIDFController b, s;
    public static double bp = 0.007, bd = 0.0, bf = 0.0, sp = 0.005, sd = 0, sf = 0.0;
    public static double pSwitch = 150;

    private double lastPower = 0.0;
    public double power;

    public final Motor shooterMotor1, shooterMotor2;
    public final DcMotorEx transferMotor;
    public final ServoImplEx hood;
    public final Follower follower;
    Robot robot;

    public ShooterSubsystem(Motor shooterMotor1, Motor shooterMotor2, DcMotorEx transferMotor, ServoImplEx hood, Follower follower, double goalX, double goalY) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.transferMotor = transferMotor;
        this.hood = hood;
        this.follower = follower;
        b = new PIDFController(new PIDFCoefficients(bp, 0, bd, bf));
        s = new PIDFController(new PIDFCoefficients(sp, 0, sd, sf));
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public boolean shooterIsSpunUp() {
        return Math.abs(shooterMotor2.getCorrectedVelocity()-setPoint) < Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    public void syncer() {
        if (Globals.shooterState == Globals.ShooterState.SHOOTING) {
            double dxOdo = follower.getPose().getX() - goalX;
            double dyOdo = follower.getPose().getY() - goalY;

            ShooterParams currentShooterParam = shooterLUT.getShooterValue(Math.hypot(dxOdo, dyOdo));

            hood.setPosition(currentShooterParam.hoodPos);

            if (currentShooterParam.shooterVel < 400 && currentShooterParam.shooterVel != 0) {
                setPoint = Globals.MIN_SHOOTER_VELOCITY;
            } else {
                setPoint = currentShooterParam.shooterVel;
            }

            b.setCoefficients(new PIDFCoefficients(bp, 0, bd, bf));
            s.setCoefficients(new PIDFCoefficients(sp, 0, sd, sf));

            if (Math.abs(setPoint - shooterMotor2.getCorrectedVelocity()) < pSwitch) {
                s.updateError(setPoint - shooterMotor2.getCorrectedVelocity());
                shooterMotor1.set(s.run());
                shooterMotor2.set(s.run());
            } else {
                b.updateError(setPoint - shooterMotor2.getCorrectedVelocity());
                shooterMotor1.set(b.run());
                shooterMotor2.set(b.run());
            }
        } else {
            shooterMotor1.set(Globals.MIN_SHOOTER_POWER);
            shooterMotor2.set(Globals.MIN_SHOOTER_POWER);
        }

        if (Globals.transferState == Globals.TransferState.TRANSFERRING) {
            transferMotor.setPower(Globals.MAX_TRANSFER_POWER);
        } else {
            transferMotor.setPower(0);
        }

    }

}
