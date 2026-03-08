package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerSpikeCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.BlueSortedPath12;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.FriendlyRedClosePath18;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath18;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedSortedPath12;

@Autonomous(preselectTeleOp = "Blue")
public class BSorted12 extends OpMode {
    Halo r;
    BlueSortedPath12 p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, G.BLUE_CUBE_START, G.Side.BLUE, true);
        r.init();
        p = new BlueSortedPath12(r);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new PeacockTelemetry(this);

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "12 Ball Sorted Auto (Blue)");
        telemetry.addData("Obelisk Reading:", G.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.next()), //shoot preloads
                                new ResetShooterAndReadCmd(r, false, 0, G.Side.BLUE)
                        ),
                        new WaitCommand(300),
                        r.turret.blue12Pos(),
                        new WaitCommand(800),
                        new DeferredCommand(() -> new RapidSlowerSpikeCmd(r, G.obeliskOptions, 1)),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4, r.turret.blue12Pos())),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1300) //this is gate intake
                        ),
                        new WaitCommand(300),
                        new FollowPathCmd(r, p.next()), //shoot preloads
                        new DeferredCommand(() -> new RapidSlowerSpikeCmd(r, G.obeliskOptions, 2)),
                        new WaitCommand(800),
                        new ParallelCommandGroup(
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()),
                                new DeferredCommand(() -> new ResetShooterCmd(r, 6, r.turret.blue12Pos()))
                        ),
                        r.turret.blue12Pos(),
                        new ParallelCommandGroup(
                                new InstantCommand(() -> r.spinner.transferStart()),
                                new DeferredCommand(() -> new RapidSlowerSpikeCmd(r, G.obeliskOptions, 3))
                        ),
                        new WaitCommand(800),
                        new ParallelCommandGroup(
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()),
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4.5, r.turret.blue12Pos()))
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new ParallelCommandGroup(
                                new InstantCommand(() -> r.spinner.transferStart()),
                                new DeferredCommand(() -> new RapidSlowerSpikeCmd(r, G.obeliskOptions, 1))
                        ),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new DeferredCommand(() -> new ResetShooterCmd(r))
                        )
                ));
    }

    @Override
    public void loop() {
        telemetry.addData("Obelisk: ", G.obeliskOptions);
        telemetry.update();
        r.sortedLoop();
    }

    @Override
    public void stop() {
        r.stop();
    }
}
