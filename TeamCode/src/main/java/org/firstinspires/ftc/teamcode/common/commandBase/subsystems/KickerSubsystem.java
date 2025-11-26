package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class KickerSubsystem extends SubsystemBase {

    public final ServoImplEx kicker1, kicker2, kicker3;

    public KickerSubsystem(ServoImplEx kicker1, ServoImplEx kicker2, ServoImplEx kicker3) {
        this.kicker1 = kicker1;
        this.kicker2 = kicker2;
        this.kicker3 = kicker3;
    }

    public void sync() {
        if (Globals.kicker1State == Globals.Kicker1State.KICK) kicker1.setPosition(Globals.KICKER1_KICK);
        if (Globals.kicker2State == Globals.Kicker2State.KICK) kicker2.setPosition(Globals.KICKER2_KICK);
        if (Globals.kicker3State == Globals.Kicker3State.KICK) kicker3.setPosition(Globals.KICKER3_KICK);
        if (Globals.kicker1State == Globals.Kicker1State.RESET) kicker1.setPosition(Globals.KICKER1_RESET);
        if (Globals.kicker2State == Globals.Kicker2State.RESET) kicker2.setPosition(Globals.KICKER2_RESET);
        if (Globals.kicker3State == Globals.Kicker3State.RESET) kicker3.setPosition(Globals.KICKER3_RESET);
    }


}