package org.firstinspires.ftc.teamcode.common.commandBase.commands.intake;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.IntakeSubsystem;

import java.util.function.BooleanSupplier;

public class IntakeCommand extends CommandBase {

    private final IntakeSubsystem intake;

    public IntakeCommand(IntakeSubsystem intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.runIntake();
    }

    @Override
    public boolean isFinished() {
        return intake.threeBallsDetected();
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake();
    }
}
