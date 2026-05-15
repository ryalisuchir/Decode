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
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close.Red21Pathing;

import java.util.Collections;

@Autonomous
public class Alpha extends OpMode {
    Halo r;
    AlphaPathing p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, Globals.Positions.ALPHA_START_BLUE, Globals.Alliance.BLUE, Globals.Match.AUTO);
        p = new AlphaPathing(r);
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
                        r.turret.farBoi(),
                      new WaitCommand(2400),
                         r.spinner.transfer(),
                        new EnsuredOrderCmd(r),
                        new FollowPathCmd(r, p.next())
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
