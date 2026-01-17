package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B18Paths;

@Autonomous
public class BCubeAuto extends OpMode {
    Robot r;
    B18Paths p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
        p = new B18Paths(r);
        r.shooter.setCustomDistance(39.63112391930836, 105.61383285302594);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Cube Auto (Blue)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        Globals.turretState = Globals.TurretState.FOLLOWING;
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new SequentialCommandGroup(
                                        new WaitCommand(1000),
                                        new KickOrderACmd(r),
                                        new DeferredCommand(() -> new ResetShooterAndReadCmd(r, true, 3.5, Globals.Side.BLUE))
                                )
                        ),
                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.FOLLOWING),
                        new InstantCommand(() -> r.shooter.setCustomDistance(p.shoot0Pos.getX(), p.shoot0Pos.getY())),
                        new WaitCommand(900),
                        new KickOrderACmd(r),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //gate sequence 2
                        new WaitCommand(900),
                        new KickOrderACmd(r),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //kick gate sequence 2 (straightens up)
                        new WaitCommand(900),
                        new KickOrderACmd(r),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()), //intake far spike
                                new ResetShooterCmd(r, true, 3.5)
                        ),
                        new WaitCommand(900),
                        new KickOrderACmd(r),
                        new WaitCommand(300),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 2)
                        ),
                        new WaitCommand(900),
                        new KickOrderACmd(r)
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