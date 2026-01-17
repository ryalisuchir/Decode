package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class ResetShooterCmd extends ParallelCommandGroup {
        public ResetShooterCmd(Robot r, boolean i, double x) { //boolean is to ask if we want to start running the intake or not
            super(
                    new ParallelCommandGroup(
                            KickCommands.resetAll(r.kicker),
                            new InstantCommand(() -> Globals.shooterKicking = false),
                            r.turret.reset(),
                            r.shooter.stopShooter(),
                            i ? new IntakeCmd(r, x) : new InstantCommand(() -> r.spinner.transferStop())
                    )
            );
        }
}
