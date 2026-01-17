package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.FarKickCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.opmode.auto.red.paths.R6FarPaths;

@Autonomous
public class RFar6 extends OpMode {
    Robot r;
    R6FarPaths p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.RED_FAR_START, Globals.Side.RED, true);
        p = new R6FarPaths(r);
//        r.shooter.setCustomDistance(69, 2);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "6 Ball Auto (Red)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new WaitCommand(2500),
                        new DeferredCommand(() -> new FarKickCmd(r)),
                        new WaitCommand(850),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()), //agitate balls
                                new ResetShooterCmd(r, true, 20)
                        ),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()), //get back to pos
                        new WaitCommand(2000),
                        new DeferredCommand(() -> new FarKickCmd(r)),
                        new WaitCommand(100),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()), //park
                                new ResetShooterCmd(r, false, 0)
                        )
                )
        );
    }

    @Override
    public void loop() {
        r.loop(r);
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
    }

    @Override
    public void stop() {
        r.stop();
    }
}