package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class CloseGateCmd extends SequentialCommandGroup {
    public CloseGateCmd(ServoImplEx g) {
        super(
                new SequentialCommandGroup(
                        new InstantCommand(() -> g.setPosition(Globals.GATE_CLOSED))
                )
        );
    }
}