package org.firstinspires.ftc.teamcode.common.commandbase.commands.custom;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.Halo;

import java.util.ArrayList;
import java.util.List;

public class KickOrderTCmd extends SequentialCommandGroup {

    public KickOrderTCmd(Halo r) {
        List<Integer> firingOrder = computeFiringOrder();

        if (firingOrder.isEmpty()) {
            addCommands(KickCommands.resetAll(r.kicker));
            return;
        }

        List<Command> sequence = new ArrayList<>();

        sequence.add(new ParallelCommandGroup(
                new InstantCommand(() -> {
                    r.spinner.transferStart();
                    r.spinner.pivotReady();
                }), r.shooter.startShooter()
        ));

        for (int slot : firingOrder) {
            sequence.add(kickCommand(r.kicker, slot));
        }

        sequence.add(
                new SequentialCommandGroup(
                        new WaitCommand(Globals.Timings.KICK_SORT + 150),
                        new ResetTCmd(r)
                )
        );

        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(Kicker kicker, int slot) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> Globals.shooterKicking = true),
                KickCommands.kickOnce(kicker, slot),
                new WaitCommand(Globals.Timings.KICK_SORT)
        );
    }

    private List<Integer> computeFiringOrder() {
        List<SlotInfo> slots = new ArrayList<>();
        slots.add(new SlotInfo(1, Globals.ballColors[0]));
        slots.add(new SlotInfo(2, Globals.ballColors[1]));
        slots.add(new SlotInfo(3, Globals.ballColors[2]));

        List<Integer> order = new ArrayList<>();

        // Pass 1: match target sequence in order
        for (Globals.BallColor desired : getTargetColorSequence()) {
            for (SlotInfo s : slots) {
                if (!s.used && s.color == desired) {
                    order.add(s.slot);
                    s.used = true;
                    break;
                }
            }
        }

        // Pass 2: fire any remaining known-color balls
        for (SlotInfo s : slots) {
            if (!s.used && (s.color == Globals.BallColor.G || s.color == Globals.BallColor.P)) {
                order.add(s.slot);
                s.used = true;
            }
        }

        // Pass 3: fire PRESENT (detected but unclassified) balls last
        for (SlotInfo s : slots) {
            if (!s.used && s.color == Globals.BallColor.PRESENT) {
                order.add(s.slot);
            }
        }

        // NONE slots are simply never added — no empty kicks
        return order;
    }

    private List<Globals.BallColor> getTargetColorSequence() {
        List<Globals.BallColor> seq = new ArrayList<>();
        switch (Globals.obeliskOptions) {
            case PPG:
                seq.add(Globals.BallColor.P);
                seq.add(Globals.BallColor.P);
                seq.add(Globals.BallColor.G);
                break;
            case PGP:
                seq.add(Globals.BallColor.P);
                seq.add(Globals.BallColor.G);
                seq.add(Globals.BallColor.P);
                break;
            case GPP:
                seq.add(Globals.BallColor.G);
                seq.add(Globals.BallColor.P);
                seq.add(Globals.BallColor.P);
                break;
            default:
                break;
        }
        return seq;
    }

    private static class SlotInfo {
        final int slot;
        final Globals.BallColor color;
        boolean used = false;

        SlotInfo(int slot, Globals.BallColor color) {
            this.slot = slot;
            this.color = color;
        }
    }
}