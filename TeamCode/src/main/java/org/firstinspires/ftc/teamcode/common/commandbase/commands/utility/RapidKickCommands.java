package org.firstinspires.ftc.teamcode.common.commandbase.commands.utility;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

import com.seattlesolvers.solverslib.command.WaitCommand;

public class RapidKickCommands {

    public static Command kickOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.kick(slot), kicker);
    }

    public static Command resetOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.reset(slot), kicker);
    }

    public static Command kickAndReset(Halo robot, int slot) {
        G.shooterKicking = true;
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.kicker.kick(slot), robot.kicker),
                new WaitCommand(G.KICK_WAIT_RAPID),
                new InstantCommand(() -> robot.kicker.reset(slot), robot.kicker)
        );
    }

    public static Command kickMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.kickMany(slots), kicker);
    }

    public static Command resetMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.resetMany(slots), kicker);
    }

    public static Command kickAndResetMany(Halo robot, int... slots) {
        G.shooterKicking = true;
        SequentialCommandGroup group = new SequentialCommandGroup();
        for (int i = 0; i < slots.length; i++) {
            group.addCommands(kickAndReset(robot, slots[i]));
        }
        group.addCommands(
                new SequentialCommandGroup(
                new WaitCommand(100),
                new ParallelCommandGroup(
                    new InstantCommand(() -> G.shooterKicking = false)),
                    new ResetShooterCmd(robot)
                ));
        return group;
    }

    public static Command resetAll(Kicker kicker) {
        return new InstantCommand(kicker::resetAll, kicker);
    }
}
