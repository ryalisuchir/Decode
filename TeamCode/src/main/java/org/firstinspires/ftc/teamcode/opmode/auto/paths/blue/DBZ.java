package org.firstinspires.ftc.teamcode.opmode.auto.paths.blue;

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
import org.firstinspires.ftc.teamcode.common.commandbase.commands.EnsuredOrderCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidOrderCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.ExodusInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.FarInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.IntakeCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close.Red21Pathing;

import java.util.Collections;

@Autonomous
public class DBZ extends OpMode {
    Halo r;
    DBZPathing p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, Globals.Positions.ALPHA_START_BLUE, Globals.Alliance.BLUE, Globals.Match.AUTO);
        p = new DBZPathing(r);

        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new FarInitCmd(r),
                        new WaitCommand(1000),
                        r.turret.farB()
                )
        );
        telemetry = new PeacockTelemetry(this);

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Park auto.");
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.turret.farBoi(),
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.next()) //shoot 0 pos
                        ),
                        new WaitCommand(2500),
                        new RapidOrderCmd(r), //shoot preloads
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 2.5, r.turret.farBoi()), Collections.emptyList()),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //spike 3
                        ),
                        r.spinner.transfer(),
                        new RapidOrderCmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.farBoi()), Collections.emptyList()),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1500) //hp cycle 1
                        ),
                        new ParallelCommandGroup(
                                r.turret.farBoi(),
                                r.spinner.transfer()
                        ),
                        new RapidOrderCmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4, r.turret.farBoi()), Collections.emptyList()),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1500) //sweep 1
                        ),
                        r.spinner.transfer(),
                        new RapidOrderCmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                r.turret.farBoi(),
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4, r.turret.farBoi()), Collections.emptyList()),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1500) //sweep 2
                        ),
                        r.spinner.transfer(),
                        new RapidOrderCmd(r),
                        new WaitCommand(200),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.farBoi()), Collections.emptyList()),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1500) //hp cycle 2
                        ),
                        new ParallelCommandGroup(
                                r.turret.farBoi(),
                                r.spinner.transfer()
                        ),
                        new RapidOrderCmd(r),
                        new WaitCommand(200),
                        new FollowPathCmd(r, p.next()) //gtfo
                ));
    }

    @Override
    public void loop() {
        telemetry.update();
        telemetry.addData("Current Position: ", r.dt.getPose());
        r.farLoop(r);
    }

    @Override
    public void stop() {
        r.stop();
    }
}
