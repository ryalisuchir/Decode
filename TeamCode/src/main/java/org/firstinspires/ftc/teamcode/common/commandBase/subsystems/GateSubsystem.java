package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class GateSubsystem extends SubsystemBase {

    public final ServoImplEx gate;

    public GateSubsystem(ServoImplEx gate) {
        this.gate = gate;
    }

    public void syncer() {
        if (Globals.gateState == Globals.GateState.OPEN) gate.setPosition(Globals.GATE_OPEN);
        if (Globals.gateState == Globals.GateState.CLOSED) gate.setPosition(Globals.GATE_CLOSED);
    }


}