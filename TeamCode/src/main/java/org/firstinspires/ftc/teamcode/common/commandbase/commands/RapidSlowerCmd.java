package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

import java.util.ArrayList;
import java.util.List;

public class RapidSlowerCmd extends SequentialCommandGroup {

    public RapidSlowerCmd(Halo r) {
        List<Integer> firingOrder = computeFiringOrder();

        if (firingOrder.isEmpty()) {
            addCommands(KickCommands.resetAll(r.kicker));
            return;
        }

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(new InstantCommand(() -> r.spinner.transferStart()), r.shooter.startShooter()));

        sequence.add(kickCommand(r.kicker, 1));
        sequence.add(kickCommand(r.kicker, 3));
        sequence.add(kickCommand(r.kicker, 2));

        sequence.add(new WaitCommand(G.KICK_WAIT_RAPID));

        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(Kicker kicker, int slot) {
        return new SequentialCommandGroup(new InstantCommand(() -> G.shooterKicking = true), KickCommands.kickOnce(kicker, slot), new WaitCommand(G.KICK_WAIT_RAPID+30));
    }

    private List<Integer> computeFiringOrder() {
        List<Integer> order = new ArrayList<>();
        char c1 = toChar(G.ballColors[0]);
        char c2 = toChar(G.ballColors[1]);
        char c3 = toChar(G.ballColors[2]);

        List<SlotInfo> slots = new ArrayList<>();
        slots.add(new SlotInfo(1, c1));
        slots.add(new SlotInfo(2, c2));
        slots.add(new SlotInfo(3, c3));

        List<Character> target = getTargetColorSequence();

        if (!target.isEmpty()) {
            for (char desired : target) {
                for (SlotInfo s : slots) {
                    if (!s.used && s.color == desired) {
                        order.add(s.slot);
                        s.used = true;
                        break;
                    }
                }
            }
        }

        for (SlotInfo s : slots) {
            if (!s.used && s.color != 'N') {
                order.add(s.slot);
            }
        }

        return order;
    }

    private char toChar(G.BallColor color) {
        if (color == null) return 'N';
        switch (color) {
            case P:
                return 'P';
            case G:
                return 'G';
            default:
                return 'N';
        }
    }

    private List<Character> getTargetColorSequence() {
        List<Character> seq = new ArrayList<>();
        switch (G.obeliskOptions) {
            case PPG:
                seq.add('P');
                seq.add('P');
                seq.add('G');
                break;
            case PGP:
                seq.add('P');
                seq.add('G');
                seq.add('P');
                break;
            case GPP:
                seq.add('G');
                seq.add('P');
                seq.add('P');
                break;
            default:
                break;
        }
        return seq;
    }

    private static class SlotInfo {
        int slot;
        char color;
        boolean used = false;

        SlotInfo(int slot, char color) {
            this.slot = slot;
            this.color = color;
        }
    }
}