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

        for (int slot : firingOrder) {
            sequence.add(new InstantCommand(() -> kick(slot)));
            sequence.add(new WaitCommand(Globals.KICK_WAIT_TIME));
            sequence.add(new InstantCommand(() -> resetKicker(slot)));
        }

        sequence.add(resetAll());

        addCommands(sequence.toArray(new Command[0]));
    }

    // ------------------------------------------------------------------------
    // COMPUTE FIRING ORDER
    // ------------------------------------------------------------------------

    private List<Integer> computeFiringOrder() {

        char c1 = toChar(Globals.ballColor1);
        char c2 = toChar(Globals.ballColor2);
        char c3 = toChar(Globals.ballColor3);

        // If there are no balls at all
        if (c1 == 'N' && c2 == 'N' && c3 == 'N')
            return new ArrayList<>();

        List<Character> target = getTargetColorSequence();

        // If vision didn't detect anything → fire all available balls in order
        if (target.isEmpty()) {
            return simpleFallback(c1, c2, c3);
        }

        // Prepare slots
        List<SlotInfo> slots = new ArrayList<>();
        slots.add(new SlotInfo(1, c1));
        slots.add(new SlotInfo(2, c2));
        slots.add(new SlotInfo(3, c3));

        List<Integer> order = new ArrayList<>();

        // Try matching each desired color to a real slot
        for (char desired : target) {
            boolean matched = false;

            for (SlotInfo s : slots) {
                if (!s.used && s.color == desired) {
                    order.add(s.slot);
                    s.used = true;
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                // Could not match target sequence → fallback
                return simpleFallback(c1, c2, c3);
            }
        }

        return order;
    }

    // ------------------------------------------------------------------------
    // HELPERS
    // ------------------------------------------------------------------------

    private List<Integer> simpleFallback(char c1, char c2, char c3) {
        List<Integer> fallback = new ArrayList<>();
        if (c1 != 'N') fallback.add(1);
        if (c2 != 'N') fallback.add(2);
        if (c3 != 'N') fallback.add(3);
        return fallback;
    }

    private char toChar(Globals.BallColor1 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor1.P ? 'P' :
                c == Globals.BallColor1.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor2 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor2.P ? 'P' :
                c == Globals.BallColor2.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor3 c) {
        if (c == null) return 'N';
        return c == Globals.BallColor3.P ? 'P' :
                c == Globals.BallColor3.G ? 'G' : 'N';
    }

    private List<Character> getTargetColorSequence() {
        List<Character> seq = new ArrayList<>();

        switch (Globals.obeliskOptions) {
            case PPG: seq.add('P'); seq.add('P'); seq.add('G'); break;
            case PGP: seq.add('P'); seq.add('G'); seq.add('P'); break;
            case GPP: seq.add('G'); seq.add('P'); seq.add('P'); break;
            default: break; // vision didn't see anything
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

    private void resetKicker(int slot) {
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
