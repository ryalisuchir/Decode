package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.ShootingMaster;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.StartIntake;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class SubsystemTest extends CommandOpMode {

    Robot robot;
    private boolean isAutomated = true;

    @Override
    public void initialize() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);

        if (Robot.endPose != null) {
            robot.follower.setStartingPose(Robot.endPose);
        }

        robot.follower.startTeleopDrive();
    }

    @Override
    public void run() {
        robot.follower.setTeleOpDrive(
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_y),
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_x),
                -0.5 * Math.tan(1.12 * gamepad1.right_stick_x),
                true);


        if (gamepad1.ps) {
            gamepad1.rumble(1000);
            robot.follower.setPose(Globals.DEFAULT_START_POSE);
        }

        if (gamepad1.left_bumper) {
            schedule(new StartIntake());
        }

        if (gamepad1.right_bumper) {
            schedule(new ShootingMaster(robot.shooterSubsystem));
        }

        telemetry.update();
        if (isAutomated) {
            robot.loop(robot);
        }

    }
}

