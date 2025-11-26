package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class StartIntake extends CommandBase {

    public StartIntake() {}

    @Override
    public void initialize() {
        Globals.intakeState = Globals.IntakeState.INTAKING;
    }
}