package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.ObeliskVision;
import org.firstinspires.ftc.teamcode.opmode.auto.blue.paths.BCubePaths;

public class BCubeAuto extends OpMode {
    Robot r;
    BCubePaths p;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
        p = new BCubePaths(r);
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
                                        new WaitCommand(1000),
                                        new KickOrderACmd(r),
                                        new ResetShooterCmd(r, true, 4)
                                )
                        ), // at this point we should have shot 3 and intaken 3
                        new FollowPathCmd(r, p.next()), //gate sequence 1
                        new KickOrderACmd(r),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //gate sequence 2
                        new KickOrderACmd(r),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()), //kick gate sequence 2 (straightens up)
                        new KickOrderACmd(r),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()), //intake far spike
                                new ResetShooterCmd(r, true, 5)
                        ),
                        new FollowPathCmd(r, p.next()),
                        new KickOrderACmd(r),
                        new ParallelCommandGroup(
                                new FollowPathCmd(r, p.next()),
                                new ResetShooterCmd(r, true, 2) //intake from the gate
                        ),
                        new FollowPathCmd(r, p.next()),
                        new KickOrderACmd(r),
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
        ObeliskVision.getObeliskFiducial(r.llResult);
    }

    @Override
    public void stop() {
        r.stop();
    }
}