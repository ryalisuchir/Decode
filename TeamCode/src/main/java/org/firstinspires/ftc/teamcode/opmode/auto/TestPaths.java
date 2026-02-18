package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath21;

@Disabled
@Autonomous
public class TestPaths extends OpMode {
    Halo r;
    RedClosePath21 p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, G.RED_CUBE_START, G.Side.RED, true);
        p = new RedClosePath21(r);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(750),
                        new FollowPathCmd(r, p.next())
                ));
    }

    @Override
    public void loop() {
        r.noOuttakeLoop(r);
    }

    @Override
    public void stop() {
        r.stop();
    }
}