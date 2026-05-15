package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.Globals;

public class Kicker extends SubsystemBase {

    private final ServoImplEx k1;
    private final ServoImplEx k2;
    private final ServoImplEx k3;

    public Kicker(ServoImplEx k1, ServoImplEx k2, ServoImplEx k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;

//        resetAll();
    }

    public void kickMany(int... slots) {
        for (int s : slots) kick(s);
    }

    public void resetAll() { resetMany(1, 2, 3); }

    public void kick(int slot) {
        switch (slot) {
            case 1:
                Globals.kicker1State = Globals.Kicker1State.KICK;
                k1.setPosition(Globals.Kicker.KICKER1.kickPos());
                break;
            case 2:
                Globals.kicker2State = Globals.Kicker2State.KICK;
                k2.setPosition(Globals.Kicker.KICKER2.kickPos());
                break;
            case 3:
                Globals.kicker3State = Globals.Kicker3State.KICK;
                k3.setPosition(Globals.Kicker.KICKER3.kickPos());
                break;
        }
    }

    public void reset(int slot) {
        switch (slot) {
            case 1:
                Globals.kicker1State = Globals.Kicker1State.RESET;
                k1.setPosition(Globals.Kicker.KICKER1.resetPos());
                break;
            case 2:
                Globals.kicker2State = Globals.Kicker2State.RESET;
                k2.setPosition(Globals.Kicker.KICKER2.resetPos());
                break;
            case 3:
                Globals.kicker3State = Globals.Kicker3State.RESET;
                k3.setPosition(Globals.Kicker.KICKER3.resetPos());
                break;
        }
    }

    public void resetMany(int... slots) {
        for (int s : slots) reset(s);
    }
}