package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.*;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

import java.util.ArrayList;
import java.util.List;

public class ShootingMaster extends SequentialCommandGroup {

    public ShootingMaster() {
        List<Integer> firingOrder = computeFiringOrder();

        if (firingOrder.isEmpty()) {
            addCommands(resetAll());
            return;
        }

        List<Command> sequence = new ArrayList<>();
        sequence.add(new InstantCommand(() -> Globals.gateState = Globals.GateState.CLOSED));
        sequence.add(new InstantCommand(() -> Globals.transferState = Globals.TransferState.TRANSFERRING));

        for (int i = 0; i < firingOrder.size(); i++) {
            int slot = firingOrder.get(i);
            boolean isLast = (i == firingOrder.size() - 1);
            sequence.add(kickCommand(slot, isLast));
        }

        // Final failsafe kick and reset
        sequence.add(new WaitCommand(Globals.KICK_FAILSAFE));
        sequence.add(new InstantCommand(() -> Globals.failsafeState = Globals.FailsafeState.KICK));
        sequence.add(new WaitCommand(Globals.KICK_WAIT_TIME));
        sequence.add(resetAll());

        addCommands(sequence.toArray(new Command[0]));
    }

    private Command kickCommand(int slot, boolean skipWait) {
        if (skipWait) {
            return new InstantCommand(() -> kick(slot));
        } else {
            return new SequentialCommandGroup(
                    new InstantCommand(() -> kick(slot)),
                    new WaitCommand(Globals.KICK_WAIT_TIME),
                    new InstantCommand(() -> reset(slot))
            );
        }
    }

    private List<Integer> computeFiringOrder() {
        char c1 = toChar(Globals.ballColor1);
        char c2 = toChar(Globals.ballColor2);
        char c3 = toChar(Globals.ballColor3);

        List<SlotInfo> slots = new ArrayList<>();
        slots.add(new SlotInfo(1, c1));
        slots.add(new SlotInfo(2, c2));
        slots.add(new SlotInfo(3, c3));

        List<Integer> order = new ArrayList<>();
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

    private char toChar(Globals.BallColor1 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor1.P ? 'P' : c == Globals.BallColor1.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor2 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor2.P ? 'P' : c == Globals.BallColor2.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor3 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor3.P ? 'P' : c == Globals.BallColor3.G ? 'G' : 'N';
    }

    private List<Character> getTargetColorSequence() {
        List<Character> seq = new ArrayList<>();
        switch (Globals.obeliskOptions) {
            case PPG: seq.add('P'); seq.add('P'); seq.add('G'); break;
            case PGP: seq.add('P'); seq.add('G'); seq.add('P'); break;
            case GPP: seq.add('G'); seq.add('P'); seq.add('P'); break;
            default: break;
        }
        return seq;
    }

    private void kick(int slot) {
        switch (slot) {
            case 1: Globals.kicker1State = Globals.Kicker1State.KICK; break;
            case 2: Globals.kicker2State = Globals.Kicker2State.KICK; break;
            case 3: Globals.kicker3State = Globals.Kicker3State.KICK; break;
        }
    }

    private void reset(int slot) {
        switch (slot) {
            case 1: Globals.kicker1State = Globals.Kicker1State.RESET; break;
            case 2: Globals.kicker2State = Globals.Kicker2State.RESET; break;
            case 3: Globals.kicker3State = Globals.Kicker3State.RESET; break;
        }
    }

    private InstantCommand resetAll() {
        return new InstantCommand(() -> {
            Globals.transferState = Globals.TransferState.STOPPED;
            Globals.shooterState  = Globals.ShooterState.STOPPED;
            Globals.kicker1State  = Globals.Kicker1State.RESET;
            Globals.kicker2State  = Globals.Kicker2State.RESET;
            Globals.kicker3State  = Globals.Kicker3State.RESET;
            Globals.turretState   = Globals.TurretState.RESET;
            Globals.hoodState     = Globals.HoodState.RESET;
            Globals.failsafeState = Globals.FailsafeState.RESET;
        });
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