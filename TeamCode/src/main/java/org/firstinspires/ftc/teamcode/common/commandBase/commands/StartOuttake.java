package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class StartOuttake extends CommandBase {

    public StartOuttake() {}

    @Override
    public void initialize() {
        Globals.shooterState = Globals.ShooterState.SHOOTING;
        Globals.transferState = Globals.TransferState.TRANSFERRING;
    }

}
