package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.Globals;


public class SetShooter extends SubsystemBase {
    double currentFlywheelVelocity = 0.0;
    double powerSupplied = 0.0;

    private final PIDFController flywheelController = new PIDFController(
            Globals.shooterCoefficients
    );

    public boolean reached = false;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;

    public SetShooter(
            Motor shooterMotor1,
            Motor shooterMotor2,
            ServoImplEx hood
    ) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        flywheelController.setTolerance(70);
    }

    public void loop(double gulp) {
        reached = flywheelController.atSetPoint();
        double optimalHood = 0.1;
        double optimalVelocity = 1100;

        if (gulp == 1) {
            optimalHood = 0.61;
            optimalVelocity = 5000;
        } else if (gulp == 2) {
            optimalHood = 0.10;
            optimalVelocity = 1200;
        }


        hood.setPosition(optimalHood);
        currentFlywheelVelocity = shooterMotor2.getCorrectedVelocity();
        flywheelController.setSetPoint(Math.min(optimalVelocity, 5000));
        powerSupplied = flywheelController.calculate(currentFlywheelVelocity);

        setShooterPower(powerSupplied);
    }

    private void setShooterPower(double power) {
        shooterMotor1.set(power);
        shooterMotor2.set(power);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
