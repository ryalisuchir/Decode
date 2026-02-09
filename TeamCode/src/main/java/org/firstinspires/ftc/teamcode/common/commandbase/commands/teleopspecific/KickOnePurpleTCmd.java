package org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

public class KickOnePurpleTCmd extends SequentialCommandGroup {

    public KickOnePurpleTCmd(Halo r) {

        Integer purpleSlot = findOnePurpleSlot();

        if (purpleSlot == null) {
            return;
        }

        boolean lastBall = isLastBall(purpleSlot);

        addCommands(
                new ParallelCommandGroup(
                        new InstantCommand(() -> r.spinner.transferStart()),
                        r.shooter.startShooter()
                ),
                new SequentialCommandGroup(
                        KickCommands.kickOnce(r.kicker, purpleSlot),
                        new WaitCommand(G.KICK_WAIT_TELE)
                )
        );

        if (lastBall) {
            addCommands(
                    new ParallelCommandGroup(
                            KickCommands.resetAll(r.kicker),
                            r.turret.reset(),
                            r.shooter.stopShooter(),
                            r.spinner.toggleIn()
                    )
            );
        }
    }

    private Integer findOnePurpleSlot() {
        for (int i = 0; i < 3; i++) {
            if (G.ballColors[i] == G.BallColor.P) {
                return i + 1;
            }
        }
        return null;
    }

    private boolean isLastBall(int kickedSlot) {
        for (int i = 0; i < 3; i++) {
            int slot = i + 1;
            if (slot == kickedSlot) continue;

            if (G.ballColors[i] != G.BallColor.NONE) {
                return false;
            }
        }
        return true;
    }
}