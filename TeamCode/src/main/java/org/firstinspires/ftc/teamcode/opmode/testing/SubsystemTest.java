package org.firstinspires.ftc.teamcode.opmode.testing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.intake.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class SubsystemTest extends CommandOpMode {

    Robot robot;
    private boolean prevLB = false;
    private boolean intakeRunning = false;
    private Command intakeCommand;

    @Override
    public void initialize() {
        robot = new Robot(hardwareMap, new Pose(0, 0, Math.toRadians(0)), Globals.Side.BLUE, true);
        intakeCommand = new IntakeCommand(robot.intakeSubsystem);
    }

    @Override
    public void run() {
        boolean lb = gamepad1.left_bumper;

        if (lb && !prevLB) {
            if (!intakeRunning) {
                schedule(intakeCommand);
                intakeRunning = true;
            } else {
                intakeCommand.cancel();
                intakeRunning = false;
            }
        }

        prevLB = lb;
        if (!intakeCommand.isScheduled() && intakeRunning) {
            intakeRunning = false;
        }

        telemetry.update();

    }
}