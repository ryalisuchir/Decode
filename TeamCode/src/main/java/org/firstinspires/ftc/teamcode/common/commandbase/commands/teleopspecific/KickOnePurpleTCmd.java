package org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific;

import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class KickOnePurpleTCmd extends SequentialCommandGroup {

    public KickOnePurpleTCmd(Robot r) {

        Integer purpleSlot = findOnePurpleSlot();

        if (purpleSlot == null) {
            return;
        }

        boolean lastBall = isLastBall(purpleSlot);

        addCommands(
                r.rotator.transfer(),
                r.shooter.startShooter(),

                new SequentialCommandGroup(
                        KickCommands.kickOnce(r.kicker, purpleSlot),
                        new WaitCommand(Globals.KICK_WAIT_TIME)
                )
        );

        if (lastBall) {
            addCommands(
                    new ParallelCommandGroup(
                            KickCommands.resetAll(r.kicker),
                            r.turret.reset(),
                            r.shooter.stopShooter(),
                            r.rotator.stop()
                    )
            );
        }
    }

    private Integer findOnePurpleSlot() {
        for (int i = 0; i < 3; i++) {
            if (Globals.ballColors[i] == Globals.BallColor.P) {
                return i + 1;
            }
        }
        return null;
    }

    private boolean isLastBall(int kickedSlot) {
        for (int i = 0; i < 3; i++) {
            int slot = i + 1;
            if (slot == kickedSlot) continue;

            if (Globals.ballColors[i] != Globals.BallColor.NONE) {
                return false;
            }
        }
        return true;
    }
}