package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.ObeliskVision;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.B12BackPaths;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.BCubePaths;

@Autonomous
public class BFar12 extends OpMode {
    Robot r;
    B12BackPaths p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
        p = new B12BackPaths(r);
        r.shooter.setCustomDistance(p.shoot0.getX(), p.shoot0.getY());
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "Cube Auto (Blue)");
        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
        r.initLoop(r);
        Globals.turretState = Globals.TurretState.RESET;
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new SequentialCommandGroup(
                                        new WaitUntilCommand(() -> r.shooter.reached),
                                        new KickOrderACmd(r)
                                )
                        ),
                        new ParallelCommandGroup( //resets shooter from preloads and follows to intake close spike
                                new ResetShooterCmd(r, true, 5),
                                new FollowPathCmd(r, p.next())
                        ),
                        new FollowPathCmd(r, p.next()), //gets ready to shoot close spike
                        new KickOrderACmd(r),
                        new ParallelCommandGroup( //resets shooter from close spike and follows to intake middle spike
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 6)
                        ),
                        new FollowPathCmd(r, p.next()), //gets ready to shoot middle spike
                        new KickOrderACmd(r), //kicks middle spike
                        new ParallelCommandGroup( //follows to hp pickup
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //gets ready to shoot hp balls
                        new KickOrderACmd(r),
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
        ObeliskVision.getObeliskFiducial(r.llResult);
    }

    @Override
    public void stop() {
        r.stop();
    }
}