package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class ResetShooterAndReadCmd extends ParallelCommandGroup {
    public ResetShooterAndReadCmd(Robot r, boolean i, double x, Globals.Side s) { //boolean is to ask if we want to start running the intake or not
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        new InstantCommand(() -> Globals.robotState = Globals.RobotState.NOT_KICKING),
                        s == Globals.Side.BLUE ? r.turret.blueObeliskRead() : r.turret.redObeliskRead(),
                        r.shooter.stopShooterFollow(),
                        i ? new IntakeCmd(r, x) : new InstantCommand(() -> r.spinner.transferStop())
                )
        );
    }
}
