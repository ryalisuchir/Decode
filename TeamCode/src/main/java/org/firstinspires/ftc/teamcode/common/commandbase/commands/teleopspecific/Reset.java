package org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class Reset extends ParallelCommandGroup {
    public Reset(Robot r) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> Globals.shooterKicking = false),
                        r.turret.reset(),
                        r.shooter.stopShooter(),
                        new InstantCommand(() -> r.shooter.clearCustomDistance()),
                        new InstantCommand(() -> r.spinner.transferStop())
                )
        );
    }
}