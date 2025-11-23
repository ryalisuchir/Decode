package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.TurretLUT;

public class HoodSubsystem extends SubsystemBase {

    public final ServoImplEx servo1, servo2;
    public final Follower follower;
    private final TurretLUT turretLUT = new TurretLUT();
    double turretAngle;
    double goalX, goalY;

    public TurretSubsystem(ServoImplEx servo1Input, ServoImplEx servo2Input, Follower followerInput) {
        servo1 = servo1Input;
        servo2 = servo2Input;
        follower = followerInput;
    }

    public double getTurretAngleToGoal(Globals.Side side, double robotX, double robotY, double robotHeadingRadians) {
        if (side == Globals.Side.BLUE) {
            goalX = Globals.BLUE_CASTLE.getX();
            goalY = Globals.BLUE_CASTLE.getY();
        } else if (side == Globals.Side.RED) {
            goalX = Globals.RED_CASTLE.getX();
            goalY = Globals.RED_CASTLE.getX();
        }

        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double absoluteAngle = Math.atan2(dy, dx);

        turretAngle = absoluteAngle - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle)); //normalizes

        return turretAngle; //in radians
    }

    @Override
    public void periodic() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {
            servo1.setPosition(turretLUT.getServoValue(turretAngle));
            servo2.setPosition(turretLUT.getServoValue(turretAngle));
        } else {
            servo1.setPosition(Globals.TURRET_RESET);
            servo2.setPosition(Globals.TURRET_RESET);
        }
    }

}