package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class GateSubsystem extends SubsystemBase {

    public final ServoImplEx gate;
    double lastSetPosition = -999;

    private void setPositionOnce(double pos) {
        if (pos != lastSetPosition) {
            gate.setPosition(pos);
            lastSetPosition = pos;
        }
    }

    public GateSubsystem(ServoImplEx gate) {
        this.gate = gate;
    }

    public void syncer() {
        if (Globals.gateState == Globals.GateState.OPEN) setPositionOnce(Globals.GATE_OPEN);
        if (Globals.gateState == Globals.GateState.CLOSED) setPositionOnce(Globals.GATE_CLOSED);
    }


}