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


public class SetShooterClass extends SubsystemBase {

    double optimalHood = G.HOOD_LOWERED;
    public double optimalVelocity = 0.0;
    double currentFlywheelVelocity = 0.0;
    double powerSupplied = 0.0;

    private final PIDFController flywheelController = new PIDFController(
            new PIDFCoefficients(
                    0.0023,
                    0,
                    0,
                    0.00036
            )
    );

    public boolean reached = false;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;

    public SetShooterClass(
            Motor shooterMotor1,
            Motor shooterMotor2,
            ServoImplEx hood
    ) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
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

    public void loop() {
        reached = flywheelController.atSetPoint();
        if (G.setShooterState == G.SetShooterState.far) {
            optimalHood = 0.845;
            optimalVelocity = 2150;
        } else if (G.setShooterState == G.SetShooterState.reg_18) {
            optimalHood = 0.82;
            optimalVelocity = 1630;
        } else if (G.setShooterState == G.SetShooterState.sort_12) {
            optimalHood = 0.835;
            optimalVelocity = 1530;
        } else if (G.setShooterState == G.SetShooterState.test) {
            optimalHood = 0.835;
            optimalVelocity = 800;
        } else if (G.setShooterState == G.SetShooterState.very_very_close) {
            optimalHood = 0.74;
            optimalVelocity = 1400;
        }

        currentFlywheelVelocity = shooterMotor1.getCorrectedVelocity();

        flywheelController.setSetPoint(Math.min(optimalVelocity, 2700));
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
