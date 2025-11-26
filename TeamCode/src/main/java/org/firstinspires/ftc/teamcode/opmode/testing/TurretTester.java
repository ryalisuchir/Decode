package org.firstinspires.ftc.teamcode.opmode.testing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;
import org.firstinspires.ftc.teamcode.common.robot.TurretLUT;

@TeleOp
public class TurretTester extends OpMode {
    Robot robot;
    private final TurretLUT turretLUT = new TurretLUT();

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
    }

    @Override
    public void loop() {
        double servoPosition = turretLUT.getServoValue(robot.getTurretAngleToGoal(Globals.side, robot.follower.getPose().getX(), robot.follower.getPose().getY(), robot.follower.getPose().getHeading()));

        if(Math.abs(robot.turret1.getPosition() - servoPosition) < 0.02) return;

        robot.turret1.setPosition(servoPosition);
        robot.turret2.setPosition(servoPosition);

        robot.loop(robot);
    }
}
