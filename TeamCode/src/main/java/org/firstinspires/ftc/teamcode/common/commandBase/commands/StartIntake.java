package org.firstinspires.ftc.teamcode.common.commandBase.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class StartIntake extends CommandBase {

    private long startTime;

    public StartIntake() {}

    @Override
    public void initialize() {
        Globals.intakeState = Globals.IntakeState.INTAKING;
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isFinished() {
        boolean threeDetected =
                Globals.ballColor1 != Globals.BallColor1.NONE &&
                        Globals.ballColor2 != Globals.BallColor2.NONE &&
                        Globals.ballColor3 != Globals.BallColor3.NONE;

        if (threeDetected) return true;

        if (Globals.intakeState == Globals.IntakeState.STOPPED) return true;

        if (Globals.match == Globals.Match.AUTO) {
            long elapsed = System.currentTimeMillis() - startTime;
            return elapsed >= Globals.MAX_TIME_SPENT_INTAKING;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Globals.intakeState = Globals.IntakeState.STOPPED;
    }
}
