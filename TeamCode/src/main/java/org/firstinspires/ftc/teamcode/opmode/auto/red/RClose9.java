package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.NoCorrectKickACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath9;

@Autonomous
public class RClose9 extends OpMode {
    Robot r;
    RedClosePath9 p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.RED_CUBE_START, Globals.Side.RED, true);
        p = new RedClosePath9(r);
        r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY()+5);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "9 Ball Auto (Red)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new InstantCommand(() -> {
                                    Globals.turretState = Globals.TurretState.RED_CLOSE_GOAL;
                                    r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY()+5);
                                }),
                                new SequentialCommandGroup(
                                        new WaitCommand(2800),
                                        new NoCorrectKickACmd(r)
                                )
                        ),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterAndReadCmd(r, true, 5, Globals.Side.RED)),
                                new FollowPathCmd(r, p.next()),
                                new SequentialCommandGroup(
                                        new WaitCommand(1300),
                                        new InstantCommand(() -> {
                                            Globals.turretState = Globals.TurretState.RED_CLOSE_GOAL;
                                            r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY()+5);
                                        })
                                )
                        ),
                        new WaitCommand(1000),
                        new NoCorrectKickACmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 3.5)
                        ),
                        new InstantCommand(() -> {
                            Globals.turretState = Globals.TurretState.RED_CLOSE_GOAL;
                            r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY()+5);
                        }),
                        new WaitCommand(1000),
                        new NoCorrectKickACmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 0)
                        )
                )
        );
    }

    @Override
    public void loop() {
        if (Globals.obeliskOptions != Globals.ObeliskOptions.NOT_FOUND) read = true;

        if (read)  {
            r.noVisionLoop(r);
        } else {
            r.loop(r);
        }

        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
    }

    @Override
    public void stop() {
        r.stop();
    }
}