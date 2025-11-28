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

    public TurretSubsystem(Globals.Side side, ServoImplEx turret1, ServoImplEx turret2, Follower follower) {
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.side = side;
    }

    public void syncer() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {

            double servoPosition;

            if (side == Globals.Side.BLUE) {
                servoPosition = blueTurretLUT.getServoValue(robot.getTurretAngleToGoal(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading()));
            } else {
                servoPosition = redTurretLUT.getServoValue(robot.getTurretAngleToGoal(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading()));
            }

            if(Math.abs(turret1.getPosition() - servoPosition) < 0.02) return;

            turret1.setPosition(servoPosition);
            turret2.setPosition(servoPosition);
        } else {
            turret1.setPosition(Globals.TURRET_RESET);
            turret2.setPosition(Globals.TURRET_RESET);
        }

    }
}