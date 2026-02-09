package org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

public class KickOneGreenTCmd extends SequentialCommandGroup {

    public KickOneGreenTCmd(Halo r) {

        Integer greenSlot = findOneGreenSlot();

        if (greenSlot == null) {
            return;
        }

        boolean lastBall = isLastBall(greenSlot);

        addCommands(
                new ParallelCommandGroup(
                        new InstantCommand(() -> r.spinner.transferStart()),
                        r.shooter.startShooter()
                ),
                new SequentialCommandGroup(
                        KickCommands.kickOnce(r.kicker, greenSlot),
                        new WaitCommand(G.KICK_WAIT_TELE)
                )
        );

        if (lastBall) {
            addCommands(
                    new ParallelCommandGroup(
                            KickCommands.resetAll(r.kicker),
                            r.turret.reset(),
                            r.shooter.stopShooter(),
                            new InstantCommand(() -> r.spinner.transferStop())
                    )
            );
        }
    }

    private Integer findOneGreenSlot() {
        for (int i = 0; i < 3; i++) {
            if (G.ballColors[i] == G.BallColor.G) {
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
