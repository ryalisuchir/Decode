package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.utility.G;

public class Kicker extends SubsystemBase {

    private final ServoImplEx k1;
    private final ServoImplEx k2;
    private final ServoImplEx k3;

    public Kicker(ServoImplEx k1, ServoImplEx k2, ServoImplEx k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;

        resetAll();
    }

    public void kickMany(int... slots) {
        for (int s : slots) kick(s);
    }

    public void resetAll() { resetMany(1,2,3); }

    public void kick(int slot) {
        switch (slot) {
            case 1:
                G.kicker1State = G.Kicker1State.KICK;
                k1.setPosition(G.KICKER1_KICK);
                break;
            case 2:
                G.kicker2State = G.Kicker2State.KICK;
                k2.setPosition(G.KICKER2_KICK);
                break;
            case 3:
                G.kicker3State = G.Kicker3State.KICK;
                k3.setPosition(G.KICKER3_KICK);
                break;
        }
    }

    public void reset(int slot) {
        switch (slot) {
            case 1:
                G.kicker1State = G.Kicker1State.RESET;
                k1.setPosition(G.KICKER1_RESET);
                break;
            case 2:
                G.kicker2State = G.Kicker2State.RESET;
                k2.setPosition(G.KICKER2_RESET);
                break;
            case 3:
                G.kicker3State = G.Kicker3State.RESET;
                k3.setPosition(G.KICKER3_RESET);
                break;
        }
    }

    public void resetMany(int... slots) {
        for (int s : slots) reset(s);
    }
}