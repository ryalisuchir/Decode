package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class StopIntake extends CommandBase {

    public StopIntake() {}

    @Override
    public void initialize() {
        Globals.intakeState = Globals.IntakeState.STOPPED;
        Globals.gateState = Globals.GateState.CLOSED;
    }

    @Override
    public boolean isFinished() {
       return true;
    }

}
