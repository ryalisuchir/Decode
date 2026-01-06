package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class IntakeCmd extends CommandBase {

    private long startTime;
    private final Robot r;
    private final double timer;

    public IntakeCmd(Robot r, double timer) {
        this.r = r;
        this.timer = timer * 1000; //input is seconds, this converts it to ms
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        CommandScheduler.getInstance().schedule(r.spinner.toggleIn());
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