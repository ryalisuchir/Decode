package org.firstinspires.ftc.teamcode.common.commandbase.commands;

import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class ResetShooterCmd extends ParallelCommandGroup {
        public ResetShooterCmd(Robot r, boolean i) { //boolean is to ask if we want to start running the intake or not
            super(
                    new ParallelCommandGroup(
                            KickCommands.resetAll(r.kicker),
                            r.turret.reset(),
                            r.shooter.stopShooter(),
                            i ? r.rotator.intake() : r.rotator.stop()
                    )
            );
        }
}
