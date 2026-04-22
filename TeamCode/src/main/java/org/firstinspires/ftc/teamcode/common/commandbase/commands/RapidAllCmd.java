package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;

import java.util.ArrayList;
import java.util.List;

public class RapidAllCmd extends SequentialCommandGroup {

    public RapidAllCmd(Halo r) {

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(
                new InstantCommand(() -> {
                    r.spinner.transferStart();
                    r.spinner.pivotReady();
                }), r.shooter.startShooter()
                ));

        sequence.add(kickCommand(r.kicker, 1));
        sequence.add(kickCommand(r.kicker, 3));
        sequence.add(kickCommand(r.kicker, 2));

        sequence.add(new WaitCommand(Globals.Timings.KICK_RAPID));

        addCommands(sequence.toArray(new Command[0]));
    }

    public RapidAllCmd(Halo r, long extraTime) {

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(
                new InstantCommand(() -> {
                    r.spinner.transferStart();
                    r.spinner.pivotReady();
                }), r.shooter.startShooter()
        ));

        sequence.add(kickCommand(r.kicker, 1, extraTime));
        sequence.add(kickCommand(r.kicker, 3, extraTime));
        sequence.add(kickCommand(r.kicker, 2, extraTime));

        sequence.add(new WaitCommand(Globals.Timings.KICK_RAPID));

        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(Kicker kicker, int slot) {
        return new SequentialCommandGroup(new InstantCommand(() -> Globals.shooterKicking = true), KickCommands.kickOnce(kicker, slot), new WaitCommand(Globals.Timings.KICK_RAPID));
    }

    private Command kickCommand(Kicker kicker, int slot, long extraTime) {
        return new SequentialCommandGroup(new InstantCommand(() -> Globals.shooterKicking = true), KickCommands.kickOnce(kicker, slot), new WaitCommand(Globals.Timings.KICK_RAPID + extraTime));
    }
}