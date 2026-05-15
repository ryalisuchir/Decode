package org.firstinspires.ftc.teamcode.common.commandbase.commands.custom;

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

public class RapidResetTCmd extends SequentialCommandGroup {

    public RapidResetTCmd(Halo r) {

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(new InstantCommand(() -> r.spinner.transferStart()), r.shooter.startShooter()));

        sequence.add(kickCommand(r.kicker, 1));
        sequence.add(kickCommand(r.kicker, 3));
        sequence.add(kickCommand(r.kicker, 2));

        sequence.add(
                new SequentialCommandGroup(
                        new WaitCommand(Globals.Timings.KICK_SORT + 150),
                        new ResetTCmd(r)
                )
        );

        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(Kicker kicker, int slot) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> Globals.shooterKicking = true),
                KickCommands.kickOnce(kicker, slot),
                new WaitCommand(Globals.Timings.KICK_RAPID),
                KickCommands.clearBallSlot(slot)
        );
    }
}
