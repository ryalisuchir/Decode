package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DeferredCommand implements Command {
    private final Supplier<Command> commandSupplier;
    private final Set<Subsystem> requirements;
    private Command currentCommand;

    public DeferredCommand(Supplier<Command> commandSupplier, Subsystem... requirements) {
        this.commandSupplier = commandSupplier;
        this.requirements = new HashSet<>();
        Collections.addAll(this.requirements, requirements);
    }

    @Override
    public void initialize() {
        currentCommand = commandSupplier.get();
        if (currentCommand != null) {
            currentCommand.initialize();
        } else {
            throw new IllegalStateException("DeferredCommand: Supplied command was null!");
        }
    }

    @Override
    public void execute() {
        if (currentCommand != null) {
            currentCommand.execute();
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (currentCommand != null) {
            currentCommand.end(interrupted);
            currentCommand = null;
        }
    }

    @Override
    public boolean isFinished() {
        return currentCommand != null && currentCommand.isFinished();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }
}
