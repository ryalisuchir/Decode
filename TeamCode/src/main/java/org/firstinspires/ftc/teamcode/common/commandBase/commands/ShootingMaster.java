package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

import java.util.ArrayList;
import java.util.List;

public class ShootingMaster extends CommandBase {

    private final List<Integer> firingOrder = new ArrayList<>();
    private int currentIndex = 0;
    private long waitStart = 0;
    private boolean waiting = false;
    private boolean finished = false;
    ShooterSubsystem shooterSubsystem;

    public ShootingMaster(ShooterSubsystem shooterSubsystem) {this.shooterSubsystem = shooterSubsystem;}

    @Override
    public void initialize() {
        Globals.BallColor1 c1 = Globals.ballColor1;
        Globals.BallColor2 c2 = Globals.ballColor2;
        Globals.BallColor3 c3 = Globals.ballColor3;

        if (c1 == Globals.BallColor1.NONE &&
                c2 == Globals.BallColor2.NONE &&
                c3 == Globals.BallColor3.NONE) {
            finished = true;
            return;
        }

        List<Character> targetColors = getTargetColorSequence();

        // Convert slot colors to list of pairs (slotIndex, colorChar)
        List<SlotInfo> slots = new ArrayList<>();
        slots.add(new SlotInfo(1, toChar(c1)));
        slots.add(new SlotInfo(2, toChar(c2)));
        slots.add(new SlotInfo(3, toChar(c3)));

        List<Integer> matchedOrder = new ArrayList<>();

        for (char desired : targetColors) {
            for (SlotInfo s : slots) {
                if (!s.used && s.color == desired) {
                    matchedOrder.add(s.slot);
                    s.used = true;
                    break;
                }
            }
        }

        if (matchedOrder.size() == targetColors.size()) {
            firingOrder.addAll(matchedOrder);
        } else {
            for (SlotInfo s : slots) {
                if (s.color != 'N') {
                    firingOrder.add(s.slot);
                }
            }
        }

        if (firingOrder.isEmpty() && shooterSubsystem.shooterIsSpunUp()) {
            finished = true;
            Globals.transferState = Globals.TransferState.STOPPED;
            Globals.shooterState  = Globals.ShooterState.STOPPED;
            Globals.kicker1State  = Globals.Kicker1State.RESET;
            Globals.kicker2State  = Globals.Kicker2State.RESET;
            Globals.kicker3State  = Globals.Kicker3State.RESET;
            Globals.turretState   = Globals.TurretState.RESET;
            Globals.hoodState     = Globals.HoodState.RESET;
        }
    }

    @Override
    public void execute() {
        if (finished) return;

        if (waiting) {
            long now = System.currentTimeMillis();
            if (now - waitStart >= Globals.KICK_WAIT_TIME) {
                resetKicker(firingOrder.get(currentIndex));
                waiting = false;
                currentIndex++;

                if (currentIndex >= firingOrder.size() && shooterSubsystem.shooterIsSpunUp()) {
                    finished = true;
                    Globals.transferState = Globals.TransferState.STOPPED;
                    Globals.shooterState  = Globals.ShooterState.STOPPED;
                    Globals.kicker1State  = Globals.Kicker1State.RESET;
                    Globals.kicker2State  = Globals.Kicker2State.RESET;
                    Globals.kicker3State  = Globals.Kicker3State.RESET;
                    Globals.turretState   = Globals.TurretState.RESET;
                    Globals.hoodState     = Globals.HoodState.RESET;
                }
            }
            return;
        }

        if (currentIndex < firingOrder.size()) {
            int slot = firingOrder.get(currentIndex);
            kick(slot);

            waiting = true;
            waitStart = System.currentTimeMillis();
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    private char toChar(Globals.BallColor1 c) {
        return c == Globals.BallColor1.P ? 'P' :
                c == Globals.BallColor1.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor2 c) {
        return c == Globals.BallColor2.P ? 'P' :
                c == Globals.BallColor2.G ? 'G' : 'N';
    }

    private char toChar(Globals.BallColor3 c) {
        return c == Globals.BallColor3.P ? 'P' :
                c == Globals.BallColor3.G ? 'G' : 'N';
    }

    private List<Character> getTargetColorSequence() {
        List<Character> seq = new ArrayList<>();

        switch (Globals.obeliskOptions) {
            case PPG: seq.add('P'); seq.add('P'); seq.add('G'); break;
            case PGP: seq.add('P'); seq.add('G'); seq.add('P'); break;
            case GPP: seq.add('G'); seq.add('P'); seq.add('P'); break;
            default: break; //not found - ll doesn't see anything/obelisk
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
