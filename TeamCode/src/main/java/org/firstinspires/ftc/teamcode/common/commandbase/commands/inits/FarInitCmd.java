package org.firstinspires.ftc.teamcode.common.commandbase.commands.inits;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;


public class FarInitCmd extends ParallelCommandGroup {
    public FarInitCmd(Halo r) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        r.turret.farB(),
                        new InstantCommand(() -> r.hood.setPosition(Globals.HOOD.getMin())),
                        new InstantCommand(() -> r.gate.setPosition(Globals.Gate.GATE_CLOSED))
                )
        );
    }
}