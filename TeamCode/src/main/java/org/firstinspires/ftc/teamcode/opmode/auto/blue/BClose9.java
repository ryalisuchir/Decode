package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B9ClosePaths;

@Autonomous
public class BClose9 extends OpMode {
    Robot r;
    B9ClosePaths p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
        p = new B9ClosePaths(r);
        r.shooter.setCustomDistance(p.shoot0Pos.getX(), p.shoot1Pos.getY());
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "9 Ball Auto (Blue)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new InstantCommand(() -> new ResetShooterAndReadCmd(r, false, 0, Globals.Side.BLUE))
                        ),
                        new WaitCommand(600),
                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.FOLLOWING),
                        new WaitCommand(1000),
                        new DeferredCommand(() -> new KickOrderACmd(r)),
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, true, 4),
                                new FollowPathCmd(r, p.next())
                        ),
                        new WaitCommand(1000),
                        new DeferredCommand(() -> new KickOrderACmd(r)),
                        new WaitCommand(100),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 4)
                        ),
                        new WaitCommand(2000),
                        new DeferredCommand(() -> new KickOrderACmd(r)),
                        new WaitCommand(100),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, false, 0)
                        )
                )
        );
    }

    @Override
    public void loop() {
        if (Globals.obeliskOptions != Globals.ObeliskOptions.NOT_FOUND) read = true;

        if (read)  {
            r.noVisionLoop(r);
        } else {
            r.loop(r);
        }

        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
    }

    @Override
    public void stop() {
        r.stop();
    }
}