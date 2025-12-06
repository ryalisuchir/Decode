package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.FollowPath;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.ShootingMaster;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.StartIntake;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;
import org.firstinspires.ftc.teamcode.opmode.autonomous.paths.FarPather;

//@Autonomous
@Config
public class FarBluePath extends OpMode {
    Robot robot;
    FarPather p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robot = new Robot(hardwareMap, Globals.BLUE_FAR_START, Globals.Side.BLUE, true);
        p = new FarPather(robot);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Blue Far");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        robot.initLoop(robot);
        Globals.turretState = Globals.TurretState.BLUE_FAR_OBELISK;
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new FollowPath(robot, p.next()),
                        new InstantCommand(() -> robot.follower.setMaxPower(0.4)),
                        new FollowPathCommand(robot.follower, p.next()),
                        new WaitCommand(3000),
                        new FollowPath(robot, p.next()),
                        new WaitCommand(3000),
                        new InstantCommand(() -> robot.follower.setMaxPower(1)),
                        new FollowPath(robot, p.next()),
                        new InstantCommand(() -> robot.follower.setMaxPower(0.4)),
                        new FollowPath(robot, p.next()),
                        new WaitCommand(3000),
                  new FollowPath(robot, p.next()),
                        new WaitCommand(3000),
                  new FollowPath(robot, p.next()),
                        new WaitCommand(3000),
                  new FollowPath(robot, p.next()),
                        new WaitCommand(3000),
                        new InstantCommand(() -> robot.follower.setMaxPower(1)),
                  new FollowPath(robot, p.next())
                )
        );
    }

    @Override
    public void loop() {
        robot.loop(robot);
    }

    @Override
    public void stop() { robot.stop(); }
}