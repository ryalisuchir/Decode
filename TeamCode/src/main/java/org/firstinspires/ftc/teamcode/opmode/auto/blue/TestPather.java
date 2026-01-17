package org.firstinspires.ftc.teamcode.opmode.auto.blue;

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
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B18Paths;

@Autonomous
@Disabled
public class TestPather extends OpMode {
    Robot r;
    B18Paths p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
        p = new B18Paths(r);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Cube Auto (Blue)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(600),
                        new FollowPathCmd(r, p.next())
                )
        );
    }

    @Override
    public void loop() {
        r.nothingLoop(r);
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
    }

    @Override
    public void stop() {
        r.stop();
    }
}