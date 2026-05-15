package org.firstinspires.ftc.teamcode.common.commandbase.commands.utility;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import com.seattlesolvers.solverslib.command.WaitCommand;

public class KickCommands {

    public static Command clearBallSlot(int slot) {
        return new InstantCommand(() -> Globals.ballColors[slot - 1] = Globals.BallColor.NONE);
    }

    public static Command kickOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.kick(slot), kicker);
    }

    public static Command resetOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.reset(slot), kicker);
    }

    public static Command kickAndReset(Kicker kicker, int slot) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> kicker.kick(slot), kicker),
                new WaitCommand(Globals.Timings.KICK_SORT),
                clearBallSlot(slot),
                new InstantCommand(() -> kicker.reset(slot), kicker)
        );
    }

    public static Command kickMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.kickMany(slots), kicker);
    }

    public static Command resetMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.resetMany(slots), kicker);
    }

    public static Command kickAndResetMany(Kicker kicker, int... slots) {
        SequentialCommandGroup group = new SequentialCommandGroup();
        for (int i = 0; i < slots.length; i++) {
            group.addCommands(kickAndReset(kicker, slots[i]));
        }
        return group;
    }

    public static Command resetAll(Kicker kicker) {
        return new InstantCommand(kicker::resetAll, kicker);
    }
}
