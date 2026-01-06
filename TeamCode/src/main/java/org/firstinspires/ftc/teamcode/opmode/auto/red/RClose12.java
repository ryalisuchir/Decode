package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.InitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.ObeliskVision;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B12ClosePaths;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.BCubePaths;
import org.firstinspires.ftc.teamcode.opmode.auto.red.paths.R12ClosePaths;

@Autonomous
public class RClose12 extends OpMode {
    Robot r;
    R12ClosePaths p;
    boolean read = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.RED_CUBE_START, Globals.Side.RED, true);
        p = new R12ClosePaths(r);
        r.shooter.setCustomDistance(p.shoot0.getX(), p.shoot0.getY());
        CommandScheduler.getInstance().schedule(new InitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Globals.turretState = Globals.TurretState.RED_CLOSE_OBELISK;
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Cube Auto (Red)");
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
                                new SequentialCommandGroup(
                                        new WaitCommand(850),
                                        new KickOrderACmd(r),
                                        new ResetShooterAndReadCmd(r, true, 4.2, Globals.Side.RED)
                                )
                        ), // at this point we should have shot 3 and intaken 3
                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.FOLLOWING),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(850),
                        new KickOrderACmd(r),
                        new WaitCommand(850),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 3)
                        ),
                        new FollowPathCmd(r, p.next()),
                        new WaitCommand(850),
                        new KickOrderACmd(r),
                        new WaitCommand(850),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 4.1)
                        ),
                        new FollowPathCmd(r, p.next()), //kick gate sequence 2 (straightens up)
                        new WaitCommand(850),
                        new KickOrderACmd(r),
                        new WaitCommand(850),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
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