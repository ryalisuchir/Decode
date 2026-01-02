package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.RedTurretLUT;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();

    private final Follower follower;
    private final Globals.Side side;
    private final double goalX, goalY;

    private double lastSetPosition = -999;

    public Turret(
            Globals.Side side,
            ServoImplEx turret1,
            ServoImplEx turret2,
            Follower follower,
            double goalX,
            double goalY
    ) {
        this.side = side;
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.goalX = goalX;
        this.goalY = goalY;
    }


    private void setPositionOnce(double pos) {
        if (Math.abs(pos - lastSetPosition) > 0.005) {
            turret1.setPosition(pos);
            turret2.setPosition(pos);
            lastSetPosition = pos;
        }
    }

    public InstantCommand reset() {
        return new InstantCommand(() ->setPositionOnce(Globals.TURRET_RESET));
    }

    public InstantCommand blueObeliskRead() {
        return new InstantCommand(() -> setPositionOnce(Globals.TURRET_BLUE_CLOSE_READ));
    }

    public InstantCommand redObeliskRead() {
        return new InstantCommand(() -> setPositionOnce(Globals.TURRET_RED_CLOSE_READ));
    }

    public void setFixedPosition(double pos) {
        setPositionOnce(clamp(pos, Globals.MIN_TURRET, Globals.MAX_TURRET));
    }

    public void followGoal() {
        double turretAngle = getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading()
        );

        double servoPosition =
                (side == Globals.Side.BLUE)
                        ? blueTurretLUT.getServoValue(turretAngle)
                        : redTurretLUT.getServoValue(turretAngle);

        setFixedPosition(servoPosition);
    }

    public void loop() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {
            followGoal();
        }
    }

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

        return turretAngle;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}