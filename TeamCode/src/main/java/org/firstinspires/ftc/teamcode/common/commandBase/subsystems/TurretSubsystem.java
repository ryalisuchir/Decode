package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.RedTurretLUT;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

public class TurretSubsystem extends SubsystemBase {

    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();
    Robot robot;
    Follower follower;
    Globals.Side side;
    double goalX, goalY;

    public TurretSubsystem(Globals.Side side, ServoImplEx turret1, ServoImplEx turret2, Follower follower, double goalX, double goalY) {
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.side = side;
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

        return turretAngle;
    }

    public void syncer() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {

            double servoPosition;

            if (side == Globals.Side.BLUE) {
                servoPosition = blueTurretLUT.getServoValue(getTurretAngleToGoal(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading()));
            } else {
                servoPosition = redTurretLUT.getServoValue(getTurretAngleToGoal(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading()));
            }

            turret1.setPosition(servoPosition);
            turret2.setPosition(servoPosition);
        } else {
            turret1.setPosition(Globals.TURRET_RESET);
            turret2.setPosition(Globals.TURRET_RESET);
        }

    }
}