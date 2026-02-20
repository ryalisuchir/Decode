package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.common.utility.Halo;

public class IntakeCmd extends CommandBase {
    private final Halo r;
    private final double timer;
    private long startTime;

    public IntakeCmd(Halo r, double timer) {
        this.r = r;
        this.timer = timer * 1000; //input is seconds, this converts it to ms
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        CommandScheduler.getInstance().schedule(r.spinner.intake());
    }

    @Override
    public boolean isFinished() {
        if (r.spinner.threeBallsDetected()) return true;
        return System.currentTimeMillis() - startTime >= timer;
    }

    @Override
    public void end(boolean interrupted) {
        CommandScheduler.getInstance().schedule(r.spinner.toggleIn());
    }
}
