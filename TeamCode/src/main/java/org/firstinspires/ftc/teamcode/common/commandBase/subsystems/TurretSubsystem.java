package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.TurretLUT;

public class TurretSubsystem extends SubsystemBase {

    public final ServoImplEx servo1, servo2;
    public final Follower follower;
    private final TurretLUT turretLUT = new TurretLUT();
    double turretAngle;

    public TurretSubsystem(ServoImplEx servo1Input, ServoImplEx servo2Input, Follower followerInput) {
        servo1 = servo1Input;
        servo2 = servo2Input;
        follower = followerInput;
    }

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double goalX = -72;
        double goalY = 72;
        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double absoluteAngle = Math.atan2(dy, dx);

        turretAngle = absoluteAngle - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle)); //normalizes

        return turretAngle; //in radians
    }

    @Override
    public void periodic() {
        servo1.setPosition(turretLUT.getServoValue(turretAngle));
        servo2.setPosition(turretLUT.getServoValue(turretAngle));
    }

}