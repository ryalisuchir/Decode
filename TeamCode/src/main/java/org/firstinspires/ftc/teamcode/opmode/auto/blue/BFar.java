package org.firstinspires.ftc.teamcode.opmode.auto.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.IntakeCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderAFarCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidFarCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.FarInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.Reset;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.camera.BallZoneCamera;
import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.BlueFarPath;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedFarPath;

@Autonomous(preselectTeleOp = "Blue")
public class BFar extends OpMode {
    private Halo r;
    private BlueFarPath p;
    private BallZoneCamera camera;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, G.BLUE_FAR_START, G.Side.BLUE, true);
        p = new BlueFarPath(r);
        telemetry = new PeacockTelemetry(this);

        CommandScheduler.getInstance().schedule(new FarInitCmd(r, G.Side.BLUE));
    }

    @Override
    public void init_loop() {
        if (camera != null) camera.update();

        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "BFar (Sweep)");
        telemetry.addData("Obelisk Reading:", G.obeliskOptions);

        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.shoot0()),
                                new SequentialCommandGroup(
                                        new WaitCommand(3000),
                                        new DeferredCommand(() -> new RapidFarCmd(r))
                                )
                        ),
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 3.5),
                                new InstantCommand(() -> r.i.setPower(1)),
                                new FollowPathCmd(r, p.intakeSpikeAndShoot()).withStallTimeout(0.03, 2000)
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new DeferredCommand(() -> new RapidFarCmd(r)),
                        new ParallelCommandGroup(
                                new Reset(r),
                                new IntakeCmd(r, 5),
                                new InstantCommand(() -> r.i.setPower(1)),
                                new FollowPathCmd(r, p.intakeHp()).withStallTimeout(0.03, 2000)
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new DeferredCommand(() -> new RapidFarCmd(r)),
                        //Sweep cycle 1:
                        new ParallelCommandGroup(
                                new Reset(r),
                                new IntakeCmd(r, 5),
                                new InstantCommand(() -> r.i.setPower(1)),
                                new FollowPathCmd(r, p.intakeSweepAndShoot()).withStallTimeout(0.03, 2000)
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new DeferredCommand(() -> new RapidFarCmd(r)),
                        //Sweep cycle 2:
                        new ParallelCommandGroup(
                                new Reset(r),
                                new IntakeCmd(r, 5),
                                new InstantCommand(() -> r.i.setPower(1)),
                                new FollowPathCmd(r, p.intakeSweepAndShoot()).withStallTimeout(0.03, 2000)
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new DeferredCommand(() -> new RapidFarCmd(r)),
//                        //Sweep cycle 3:
//                        new ParallelCommandGroup(
//                                new Reset(r),
//                                new IntakeCmd(r, 5),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new FollowPathCmd(r, p.intakeSweepAndShoot()).withStallTimeout(0.03, 2000)
//                        ),
//                        new InstantCommand(() -> r.spinner.transferStart()),
//                        new DeferredCommand(() -> new RapidFarCmd(r)),
                        new FollowPathCmd(r, p.park())
                )
        );
    }

    @Override
    public void loop() {
        if (camera != null) camera.update();

        telemetry.addData("1:", G.ballColors[0]);
        telemetry.addData("2:", G.ballColors[1]);
        telemetry.addData("3:", G.ballColors[2]);

        r.farBlueLoop();
    }

    @Override
    public void stop() {
        if (camera != null) camera.stop();
        r.stop();
    }
}
