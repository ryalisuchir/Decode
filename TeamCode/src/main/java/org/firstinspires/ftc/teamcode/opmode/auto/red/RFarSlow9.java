package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.InitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.SlowKickOrderACmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.ObeliskVision;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B12BackPaths;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B9BackPaths;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.BCubePaths;
import org.firstinspires.ftc.teamcode.opmode.auto.red.paths.R9FarPaths;

@Autonomous
public class RFarSlow9 extends OpMode {
    Robot r;
    R9FarPaths p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.RED_FAR_START, Globals.Side.RED, true);
        p = new R9FarPaths(r);
        r.shooter.setCustomDistance(p.shoot0.getX(), p.shoot0.getY());
        CommandScheduler.getInstance().schedule(new InitCmd(r));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Far Auto (Red)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        Globals.turretState = Globals.TurretState.RESET;
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new SequentialCommandGroup(
                                        new WaitCommand(900),
                                        new InstantCommand(() -> telemetry.addLine("0")),
                                        new WaitUntilCommand(() -> r.shooter.reached),
                                        new InstantCommand(() -> telemetry.addLine("1")),
                                        new SlowKickOrderACmd(r)
                                )
                        ),
                        new InstantCommand(() -> telemetry.addLine("Go")),
                        new ParallelCommandGroup( //resets shooter from preloads and follows to intake close spike
                                new ResetShooterCmd(r, true, 3.5),
                                new FollowPathCmd(r, p.next())
                        ),
                        new FollowPathCmd(r, p.next()), //gets ready to shoot close spike
                        new WaitCommand(500),
                        new SlowKickOrderACmd(r),
                        new WaitCommand(500),
                        new ParallelCommandGroup( //follows to hp pickup
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //gets ready to shoot hp balls
                        new WaitCommand(500),
                        new SlowKickOrderACmd(r),
                        new ParallelCommandGroup( //park and reset
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, false, 0)
                        )
                )
        );
    }

    @Override
    public void loop() {
        r.loop(r);
    }

    @Override
    public void stop() {
        r.stop();
    }
}