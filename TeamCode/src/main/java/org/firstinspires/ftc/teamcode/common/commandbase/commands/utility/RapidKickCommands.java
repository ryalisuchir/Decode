package org.firstinspires.ftc.teamcode.common.commandbase.commands.utility;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

import com.seattlesolvers.solverslib.command.WaitCommand;

public class RapidKickCommands {

    public static Command kickOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.kick(slot), kicker);
    }

    public static Command resetOnce(Kicker kicker, int slot) {
        return new InstantCommand(() -> kicker.reset(slot), kicker);
    }

    public static Command kickAndReset(Robot robot, int slot) {
        Globals.shooterKicking = true;
        return new SequentialCommandGroup(
                new InstantCommand(() -> robot.kicker.kick(slot), robot.kicker),
                new WaitCommand(Globals.KICK_WAIT_RAPID),
                new InstantCommand(() -> robot.kicker.reset(slot), robot.kicker)
        );
    }

    public static Command kickMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.kickMany(slots), kicker);
    }

    public static Command resetMany(Kicker kicker, int... slots) {
        return new InstantCommand(() -> kicker.resetMany(slots), kicker);
    }

    public static Command kickAndResetMany(Robot robot, int... slots) {
        Globals.shooterKicking = true;
        SequentialCommandGroup group = new SequentialCommandGroup();
        group.addCommands(new InstantCommand(robot.turret::applyVisionCorrectionOnce));
        for (int i = 0; i < slots.length; i++) {
            group.addCommands(kickAndReset(robot, slots[i]));
        }
        group.addCommands(new ParallelCommandGroup(new InstantCommand(() -> Globals.shooterKicking = false)), new ResetShooterCmd(robot, false, 0));
        return group;
    }

    public static Command resetAll(Kicker kicker) {
        return new InstantCommand(kicker::resetAll, kicker);
    }
}
