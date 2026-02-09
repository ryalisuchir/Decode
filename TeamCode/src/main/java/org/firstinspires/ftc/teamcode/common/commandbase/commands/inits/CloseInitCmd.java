package org.firstinspires.ftc.teamcode.common.commandbase.commands.inits;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

public class CloseInitCmd extends ParallelCommandGroup {
    public CloseInitCmd(Halo r) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        r.turret.initClose(),
                        new InstantCommand(() -> r.r.setPosition(G.HOOD_LOWERED)),
                        new InstantCommand(() -> r.g.setPosition(G.GATE_CLOSED))
                )
        );
    }
}