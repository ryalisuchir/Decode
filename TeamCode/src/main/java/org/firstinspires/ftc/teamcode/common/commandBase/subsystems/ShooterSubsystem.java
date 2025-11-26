package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;
import org.firstinspires.ftc.teamcode.common.robot.ShooterLUT;
import org.firstinspires.ftc.teamcode.common.robot.utility.ShooterParams;

public class ShooterSubsystem extends SubsystemBase {

    private final ShooterLUT shooterLUT = new ShooterLUT();

    public static double P = 0.006;
    public static double I = 0.0;
    public static double D = 0.0;
    public static double F = 0.0008;
    private double lastPower = 0.0;

    public PIDFController controller;
    public static double setPoint = 0;
    public double power;

    public final DcMotorEx shooterMotor1, shooterMotor2;
    public final DcMotorEx transferMotor;
    public final ServoImplEx hood;
    public final Follower follower;
    Robot robot;

    public ShooterSubsystem(DcMotorEx shooterMotor1, DcMotorEx shooterMotor2, DcMotorEx transferMotor, ServoImplEx hood, Follower follower) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.transferMotor = transferMotor;
        this.hood = hood;
        this.follower = follower;
        controller = new PIDFController(P, I, D, F);
        controller.setTolerance(Globals.SHOOTER_VELOCITY_TOLERANCE);
        controller.setSetPoint(0);
    }

    public void sync() {
        if (Globals.shooterState == Globals.ShooterState.SHOOTING) {
            ShooterParams currentShooterParam = shooterLUT.getShooterValue(robot.getGoalDistance(follower));

            hood.setPosition(currentShooterParam.hoodPos);

            setPoint = currentShooterParam.shooterVel;

            controller.setP(P);
            controller.setI(I);
            controller.setI(D);
            controller.setI(F);

            controller.setSetPoint(setPoint);
            power = controller.calculate(shooterMotor2.getVelocity(AngleUnit.DEGREES), setPoint);

            if (Math.abs(power - lastPower) > 0.03) {
                shooterMotor1.setPower(power);
                shooterMotor2.setPower(power);
                lastPower = power;
            }
        } else {
            shooterMotor1.setPower(Globals.MIN_SHOOTER_POWER);
            shooterMotor2.setPower(Globals.MIN_SHOOTER_POWER);
        }

        if (Globals.transferState == Globals.TransferState.TRANSFERRING) {
            transferMotor.setPower(Globals.MAX_TRANSFER_POWER);
        } else {
            transferMotor.setPower(0);
        }

    }

}
