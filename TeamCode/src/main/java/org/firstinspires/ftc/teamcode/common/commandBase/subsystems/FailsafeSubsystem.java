package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class FailsafeSubsystem extends SubsystemBase {

    public final ServoImplEx failsafe;
    private double lastSetPosition = -999;

    private void setPositionOnce(double pos) {
        if (pos != lastSetPosition) {
            failsafe.setPosition(pos);
            lastSetPosition = pos;
        }
    }


    public FailsafeSubsystem(ServoImplEx failsafe) {
        this.failsafe = failsafe;
    }

    public void syncer() {
        if (Globals.failsafeState == Globals.FailsafeState.RESET) setPositionOnce(Globals.FAILSAFE_RESET);
        if (Globals.failsafeState == Globals.FailsafeState.KICK) setPositionOnce(Globals.FAILSAFE_KICK);
    }


}