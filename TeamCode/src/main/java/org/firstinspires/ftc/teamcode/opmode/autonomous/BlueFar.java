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

@Autonomous
@Config
public class BlueFar extends OpMode {
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
                  new ParallelCommandGroup(
                          new InstantCommand(() -> Globals.shooterState = Globals.ShooterState.SHOOTING),
                          new InstantCommand(() -> Globals.transferState = Globals.TransferState.TRANSFERRING),
                          new InstantCommand(() -> Globals.turretState = Globals.TurretState.FOLLOWING)
                  ),
                  new DeferredCommand(() -> new WaitUntilCommand(() -> robot.shooterSubsystem.reached)),
                  new ShootingMaster(),
                  new ParallelCommandGroup( //go to 1
                          new StartIntake(),
                          new FollowPath(robot, p.next())
                  ),
                  new FollowPath(robot, p.next()),
                  new ShootingMaster(),
                  new ParallelCommandGroup( //go to 2
                          new StartIntake(),
                          new FollowPath(robot, p.next())
                  ),
                  new FollowPath(robot, p.next()),
                  new ShootingMaster(),
                  new ParallelCommandGroup( //go to 3
                          new StartIntake(),
                          new FollowPath(robot, p.next())
                  ),
                  new FollowPath(robot, p.next()),
                  new ShootingMaster(),
                  new FollowPath(robot, p.next())
          )
        );
    }

    @Override
    public void loop() {
        robot.loop(robot);
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