package org.firstinspires.ftc.teamcode.opmode.autonomous;

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
import org.firstinspires.ftc.teamcode.opmode.autonomous.paths.RedFarPather;

@Autonomous
@Config
public class RedFar extends OpMode {
    Robot robot;
    RedFarPather p;
    boolean running;

    @Override
    public void init() {
        running = true;
        CommandScheduler.getInstance().reset();
        robot = new Robot(hardwareMap, Globals.RED_FAR_START, Globals.Side.RED, true);
        p = new RedFarPather(robot);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Red Far");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        robot.initLoop(robot);
        Globals.turretState = Globals.TurretState.RED_FAR_OBELISK;
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> Globals.transferState = Globals.TransferState.TRANSFERRING),
                                new InstantCommand(() -> Globals.shooterState = Globals.ShooterState.SHOOTING),
                                new FollowPath(robot, p.next()) //shoot0
                        ),
                        new DeferredCommand(() -> new WaitUntilCommand(() -> robot.shooterSubsystem.reached)),
                        new WaitCommand(400),
                        new ShootingMaster(),
                        new ParallelCommandGroup( //go to 1
                                new StartIntake(),
                                new SequentialCommandGroup(
                                        new FollowPath(robot, p.next()), //pretake1
                                        new FollowPath(robot, p.next(), 0.7) //intake1
                                )
                        ),
                        new FollowPath(robot, p.next()), //shoot1
                        new WaitCommand(400),
                        new ShootingMaster(),
                        new ParallelCommandGroup( //go to 2
                                new SequentialCommandGroup(
                                        new FollowPath(robot, p.next()), //pretake2
                                        new FollowPath(robot, p.next(), 0.7) //intake2
                                ),
                                new StartIntake()
                        ),
                        new FollowPath(robot, p.next()), //shoot2
                        new WaitCommand(400),
                        new ShootingMaster(),
                        new FollowPath(robot, p.next()) //park
                )
        );
    }

    @Override
    public void loop() {
        if (running) {
            robot.loop(robot);
        }
        telemetry.addData("1:", Globals.ballColor1);
        telemetry.addData("2:", Globals.ballColor2);
        telemetry.addData("3:", Globals.ballColor3);
        telemetry.addData("Current distance: ", robot.getDistanceToGoalPinpoint());
        telemetry.addData("Anticipated velocity: ", robot.shooterSubsystem.targetVelocity);
        telemetry.addData("Current velocity: ", robot.shooterSubsystem.shooterMotor2.getCorrectedVelocity());
        telemetry.addData("Vel reached", robot.shooterSubsystem.shooterIsSpunUp());
    }

    @Override
    public void stop() { robot.stop(); }
}