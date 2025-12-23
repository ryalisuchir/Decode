package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

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

    public void kick(int slot) {
        switch (slot) {
            case 1:
                Globals.kicker1State = Globals.Kicker1State.KICK;
                k1.setPosition(Globals.KICKER1_KICK);
                break;
            case 2:
                Globals.kicker2State = Globals.Kicker2State.KICK;
                k2.setPosition(Globals.KICKER2_KICK);
                break;
            case 3:
                Globals.kicker3State = Globals.Kicker3State.KICK;
                k3.setPosition(Globals.KICKER3_KICK);
                break;
        }
    }

    public void reset(int slot) {
        switch (slot) {
            case 1:
                Globals.kicker1State = Globals.Kicker1State.RESET;
                k1.setPosition(Globals.KICKER1_RESET);
                break;
            case 2:
                Globals.kicker2State = Globals.Kicker2State.RESET;
                k2.setPosition(Globals.KICKER2_RESET);
                break;
            case 3:
                Globals.kicker3State = Globals.Kicker3State.RESET;
                k3.setPosition(Globals.KICKER3_RESET);
                break;
        }
    }


    public void kickMany(int... slots) {
        for (int s : slots) kick(s);
    }

    public void resetMany(int... slots) {
        for (int s : slots) reset(s);
    }

    public void resetAll() {
        reset(1);
        reset(2);
        reset(3);
    }
}