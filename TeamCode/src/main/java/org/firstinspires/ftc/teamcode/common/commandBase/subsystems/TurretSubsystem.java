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
    Follower follower;
    Globals.Side side;
    double goalX, goalY;

    double lastSetPosition = -999;
    private void setPositionOnce(double pos) {
        if (pos != lastSetPosition) {
            turret1.setPosition(pos);
            turret2.setPosition(pos);
            lastSetPosition = pos;
        }
    }

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
        switch (Globals.turretState) {
            case FOLLOWING: {
                double servoPosition;
                if (side == Globals.Side.BLUE) {
                    servoPosition = blueTurretLUT.getServoValue(
                            getTurretAngleToGoal(
                                    follower.getPose().getX(),
                                    follower.getPose().getY(),
                                    follower.getPose().getHeading()
                            )
                    );
                } else {
                    servoPosition = redTurretLUT.getServoValue(
                            getTurretAngleToGoal(
                                    follower.getPose().getX(),
                                    follower.getPose().getY(),
                                    follower.getPose().getHeading()
                            )
                    );
                }

                if (Math.abs(turret1.getPosition() - servoPosition) >= 0.03) {
                    setPositionOnce(servoPosition);
                }
                break;
            }

            case BLUE_CLOSE_OBELISK:
                setPositionOnce(Globals.TURRET_BLUE_CLOSE_OBELISK);
                break;

            case BLUE_FAR_OBELISK:
                setPositionOnce(Globals.TURRET_BLUE_FAR_OBELISK);
                break;

            case RED_CLOSE_OBELISK:
                setPositionOnce(Globals.TURRET_RED_CLOSE_OBELISK);
                break;

            case RED_FAR_OBELISK:
                setPositionOnce(Globals.TURRET_RED_FAR_OBELISK);
                break;

            default:
                setPositionOnce(Globals.TURRET_RESET);
                break;
        }
    }
}