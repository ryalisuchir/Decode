package org.firstinspires.ftc.teamcode.opmode.auto.red.close;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidOrderCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.ExodusInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close.Red21Pathing;

import java.util.Collections;

@Autonomous
public class Red21 extends OpMode {
    Halo r;
    Red21Pathing p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, Globals.Positions.RED_EXODUS_START, Globals.Alliance.RED, Globals.Match.AUTO);
        p = new Red21Pathing(r);
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ExodusInitCmd(r),
                        new WaitCommand(1000),
                        r.turret.redCloseClose()
                )
        );
        telemetry = new PeacockTelemetry(this);

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "21 Ball Auto (Red).");
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new RapidOrderCmd(r),
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redFirstSpike()), Collections.emptyList()),
                                r.turret.redFirstSpike()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4, r.turret.redShootSpec1()), Collections.emptyList()),
                                        r.turret.redShootSpec1()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidOrderCmd(r),
                        //this is gate sequence 1:
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3.5, r.turret.redShootGateShoot()), Collections.emptyList()),
                                        r.turret.redShootGateShoot()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        //end of gate sequence 1 ^^
                        //this is gate sequence 2:
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3.5, r.turret.redShootGateShoot()), Collections.emptyList()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        //end of gate sequence 2 ^^
                        new ParallelCommandGroup(
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()), //intakes spike closest to obelisk
                                new SequentialCommandGroup(
                                        new DeferredCommand(() -> new ResetShooterCmd(r, 3,  r.turret.redLarperShot()), Collections.emptyList()),
                                        r.turret.redLarperShot()
                                )
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        //this is gate sequence 3:
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redShootGateShoot()), Collections.emptyList()),
                                r.turret.redShootGateShoot()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        //end of gate sequence 3 ^^
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redShootGateShoot()), Collections.emptyList()),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidOrderCmd(r),
                        new WaitCommand(100),
                        //end of gate sequence 4 ^^
                        new FollowPathCmd(r, p.next())
                ));
    }

    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("Current Position: ", r.dt.getPose());
        r.loop(r);
    }

    @Override
    public void stop() {
        r.stop();
    }
}
