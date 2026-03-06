package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

public class ResetShooterAndReadCmd extends ParallelCommandGroup {
    public ResetShooterAndReadCmd(Halo r, boolean i, double x, G.Side s) { //boolean is to ask if we want to start running the intake or not
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        s == G.Side.BLUE ? r.turret.blueObeliskRead() : r.turret.redObeliskRead(),
                        new InstantCommand(() -> G.turretState = G.TurretState.RED_CLOSE_OBELISK),
                        r.shooter.stopShooterFollow(),
                        i ? new IntakeCmd(r, x) : new InstantCommand(() -> r.spinner.transferStop())
                )
        );
    }
}
