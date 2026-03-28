package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import java.time.Instant;

public class ResetShooterCmd extends ParallelCommandGroup {
    public ResetShooterCmd(Halo r, double x) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        r.turret.reset(),
                        r.shooter.stopShooter(),
                        new InstantCommand(() -> r.shooter.clearCustomDistance()),
                        new IntakeCmd(r, x)
                )
        );
    }

    public ResetShooterCmd(Halo r, double x, InstantCommand instant) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        instant,
                        new IntakeCmd(r, x)
                )
        );
    }

    public ResetShooterCmd(Halo r, double x, InstantCommand instant, InstantCommand instant2) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        instant,
                        instant2,
                        new IntakeCmd(r, x)
                )
        );
    }

    public ResetShooterCmd(Halo r, double x, InstantCommand instant, InstantCommand instant2, InstantCommand instant3, InstantCommand instant4) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        instant,
                        instant2,
                        instant3,
                        instant4,
                        new IntakeCmd(r, x)
                )
        );
    }

    public ResetShooterCmd(Halo r) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        r.turret.reset(),
                        r.shooter.stopShooter(),
                        new InstantCommand(() -> r.shooter.clearCustomDistance()),
                        new SequentialCommandGroup(
                                new WaitCommand(800),
                                new InstantCommand(() -> r.spinner.transferStop())
                        )
                )
        );
    }

    public ResetShooterCmd(Halo r, InstantCommand instant) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        instant,
                        new SequentialCommandGroup(
                                new WaitCommand(800),
                                new InstantCommand(() -> r.spinner.transferStop())
                        )
                )
        );
    }

    public ResetShooterCmd(Halo r, InstantCommand instant, InstantCommand instant2) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> G.shooterKicking = false),
                        instant,
                        instant2,
                        new SequentialCommandGroup(
                                new WaitCommand(800),
                                new InstantCommand(() -> r.spinner.transferStop())
                        )
                )
        );
    }

}