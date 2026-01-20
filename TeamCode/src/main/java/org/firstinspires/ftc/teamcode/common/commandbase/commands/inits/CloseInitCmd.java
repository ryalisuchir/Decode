package org.firstinspires.ftc.teamcode.common.commandbase.commands.inits;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class CloseInitCmd extends ParallelCommandGroup {
    public CloseInitCmd(Robot r) {
        super(
                new ParallelCommandGroup(
                        KickCommands.resetAll(r.kicker),
                        r.turret.initClose(),
                        new InstantCommand(() -> r.r.setPosition(Globals.HOOD_LOWERED)),
                        new InstantCommand(() -> r.g.setPosition(Globals.GATE_CLOSED))
                )
        );
    }
}