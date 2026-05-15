package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;

import java.util.ArrayList;
import java.util.List;

public class RapidSpikeCmd extends SequentialCommandGroup {

    public RapidSpikeCmd(Halo r, Globals.ObeliskOptions obelisk, int spikeNumber) {
        List<Integer> firingOrder = computeFiringOrder(obelisk, spikeNumber);

        if (firingOrder.isEmpty()) {
            addCommands(KickCommands.resetAll(r.kicker));
            return;
        }

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(
                new InstantCommand(() -> r.spinner.transferStart()),
                r.shooter.startShooter()
        ));

        for (int slot : firingOrder) {
            sequence.add(kickCommand(r.kicker, slot));
        }

        sequence.add(new WaitCommand(Globals.Timings.KICK_SORT_SLOW));
        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(Kicker kicker, int slot) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> Globals.shooterKicking = true),
                KickCommands.kickOnce(kicker, slot),
                new WaitCommand(Globals.Timings.KICK_SORT_SLOW)
        );
    }

    private List<Integer> computeFiringOrder(Globals.ObeliskOptions obelisk, int spikeNumber) {
        List<Integer> order = new ArrayList<>();
        char[] robotOrder = getRobotOrderForSpike(spikeNumber);
        char[] desiredOrder = getDesiredOrderFromObelisk(obelisk);

        if (robotOrder.length != 3 || desiredOrder.length != 3) {
            order.add(1);
            order.add(2);
            order.add(3);
            return order;
        }

        boolean[] used = new boolean[3];
        for (char desired : desiredOrder) {
            for (int i = 0; i < 3; i++) {
                if (!used[i] && robotOrder[i] == desired) {
                    order.add(i + 1);
                    used[i] = true;
                    break;
                }
            }
        }

        return order;
    }

    private char[] getRobotOrderForSpike(int spikeNumber) {
        switch (spikeNumber) {
            case 1:
                return new char[]{'G', 'P', 'P'};
            case 2:
                return new char[]{'P', 'G', 'P'};
            case 3:
                return new char[]{'P', 'P', 'G'};
            default:
                return new char[0];
        }
    }

    private char[] getDesiredOrderFromObelisk(Globals.ObeliskOptions obelisk) {
        if (obelisk == null) {
            return new char[0];
        }

        switch (obelisk) {
            case GPP:
                return new char[]{'G', 'P', 'P'};
            case PGP:
                return new char[]{'P', 'G', 'P'};
            case PPG:
                return new char[]{'P', 'P', 'G'};
            default:
                return new char[0];
        }
    }
}
