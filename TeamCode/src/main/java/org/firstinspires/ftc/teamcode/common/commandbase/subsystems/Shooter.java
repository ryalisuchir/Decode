package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.virtualGoalX;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.virtualGoalY;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.xVel;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret.yVel;

import com.qualcomm.robotcore.hardware.DcMotorEx;
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
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.shooter.ShooterLUT;


public class Shooter extends SubsystemBase { //new pidf system rather than relying on ramp down

    private final PIDFController flywheelController = new PIDFController(
            new PIDFCoefficients(
                    0.003,
                    0,
                    0,
                    0.00035
            )
    );

    private static final double WHEEL_DIAMETER_IN = 60.0 / 25.4;
    private static final double GEAR_RATIO = 1.0;

    public boolean reached = false;

    private double targetVelocity = 0.0;

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;

    private final double gX, gY;
    private Vector2d customPosition = null;

    private final ShooterLUT shooterLUT = new ShooterLUT();

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

        double distance;

        if (customPosition != null) {
            distance = TurretMath.getDistanceToGoalPinpoint(
                    follower,
                    customPosition.getX(),
                    customPosition.getY()
            );
        } else {
            distance = TurretMath.getDistanceToGoalPinpoint(
                    follower,
                    virtualGoalX,
                    virtualGoalY
            );
        }

        ShooterParams params = shooterLUT.getShooterValue(distance);

        hood.setPosition(
                clamp(params.hoodPos, G.HOOD_LOWERED, G.HOOD_MAX)
        );

//        double radialVel = getRadialVelocityInPerSec(virtualGoalX, virtualGoalY);
//        double rpmComp = linearVelocityToRPM(radialVel);

        targetVelocity = params.shooterVel; //accounts for robot movement into the goal

        double flywheelVel = shooterMotor1.getCorrectedVelocity();

        flywheelController.setSetPoint(Math.min(targetVelocity, 2600));
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

    private double getRadialVelocityInPerSec(double goalX, double goalY) {
        double dx = goalX - follower.getPose().getX();
        double dy = goalY - follower.getPose().getY();

        double dist = Math.hypot(dx, dy);
        if (dist < 1e-6) return 0;

        double ux = dx / dist;
        double uy = dy / dist;

        return xVel * ux + yVel * uy;
    }

    private double linearVelocityToRPM(double vInPerSec) {
        double wheelCircumference = Math.PI * WHEEL_DIAMETER_IN;
        return (vInPerSec / wheelCircumference) * 60.0 * GEAR_RATIO;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}