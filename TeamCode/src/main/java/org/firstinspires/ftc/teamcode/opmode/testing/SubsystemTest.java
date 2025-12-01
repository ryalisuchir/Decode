package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.ShootingMaster;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.StartIntake;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class SubsystemTest extends CommandOpMode {

    Robot robot;

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
        CommandScheduler.getInstance().run();
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

        if (gamepad1.triangle) {
            schedule(
                    new SequentialCommandGroup(
                            new InstantCommand(() -> Globals.kicker1State = Globals.Kicker1State.KICK),
                            new WaitCommand(1000),
                            new InstantCommand(() -> Globals.kicker2State = Globals.Kicker2State.KICK),
                            new WaitCommand(1000),
                            new InstantCommand(() -> Globals.kicker3State = Globals.Kicker3State.KICK),
                            new WaitCommand(1000)
                    )
            );
        }

        telemetry.addData("Kicker 1: ", Globals.kicker1State);
        telemetry.addData("Kicker 2: ", Globals.kicker2State);
        telemetry.addData("Kicker 3: ", Globals.kicker3State);

        if (gamepad1.right_bumper) {
            telemetry.addLine("RB Pressed");
            schedule(new ShootingMaster());
        }
        telemetry.addData("Turret pos:", robot.turret1.getPosition());
        telemetry.addData("1:", Globals.ballColor1);
        telemetry.addData("2:", Globals.ballColor2);
        telemetry.addData("3:", Globals.ballColor3);

        telemetry.update();
        robot.loop(robot);

    }
}