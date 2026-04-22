package org.firstinspires.ftc.teamcode.common.commandbase.commands.custom;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;

public class ResetTCmd extends ParallelCommandGroup {
    public ResetTCmd(Halo r) {
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