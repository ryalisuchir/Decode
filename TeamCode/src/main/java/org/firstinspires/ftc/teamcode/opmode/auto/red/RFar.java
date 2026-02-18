package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderAFarCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.FarInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.camera.BallZoneCamera;
import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedFarPath;

@Autonomous
public class RFar extends OpMode {
    private Halo r;
    private RedFarPath p;
    private BallZoneCamera camera;

    private double selectedBlend = 0.5;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, G.RED_FAR_START, G.Side.RED, true);
        p = new RedFarPath(r);
        telemetry = new PeacockTelemetry(this);

        camera = new BallZoneCamera();
        if (r.camera != null) {
            camera.start(hardwareMap, r.camera);
        } else {
            try {
                camera.start(hardwareMap, hardwareMap.get(WebcamName.class, CameraConfig.WEBCAM_NAME));
            } catch (Exception ignored) {
                camera = null;
            }
        }

        CommandScheduler.getInstance().schedule(new FarInitCmd(r, G.Side.RED));
    }

    @Override
    public void init_loop() {
        if (camera != null) camera.update();

        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "RFar (Camera)");
        telemetry.addData("Obelisk Reading:", G.obeliskOptions);
        telemetry.addData("Camera Blend:", "%.3f", selectedBlend);
        if (camera != null) {
            telemetry.addData("Live Blend:", "%.3f", camera.getStableTargetNormalized());
            telemetry.addData("Live Y:", camera.getRecommendedY());
            telemetry.addData("Confidence:", camera.hasConfidence());
        }

        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    @Override
    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.turret.clearCustom(),
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.shoot0()),
                                new SequentialCommandGroup(
                                        new WaitCommand(2700),
                                        new KickOrderAFarCmd(r)
                                )
                        ),
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 3.5),
                                new FollowPathCmd(r, p.intakeSpikeAndShoot()).withStallTimeout(0.03, 2000)
                        ),
                        new DeferredCommand(() -> new KickOrderAFarCmd(r)),
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 6),
                                new FollowPathCmd(r, p.intakeHp()).withStallTimeout(0.03, 2000)
                        ),
                        new DeferredCommand(() -> new KickOrderAFarCmd(r)),

                        // Camera cycle 1
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 5.5),
                                new DeferredCommand(this::createCameraIntakeCommand)
                        ),
                        new DeferredCommand(this::createCameraShootCommand),
                        new DeferredCommand(() -> new KickOrderAFarCmd(r)),

                        // Camera cycle 2
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 5.5),
                                new DeferredCommand(this::createCameraIntakeCommand)
                        ),
                        new DeferredCommand(this::createCameraShootCommand),
                        new DeferredCommand(() -> new KickOrderAFarCmd(r)),
                        //3
                        new ParallelCommandGroup(
                                new ResetShooterCmd(r, 5.5),
                                new DeferredCommand(this::createCameraIntakeCommand)
                        ),
                        new DeferredCommand(this::createCameraShootCommand),
                        new DeferredCommand(() -> new KickOrderAFarCmd(r))
                )
        );
    }

    @Override
    public void loop() {
        if (camera != null) camera.update();

        telemetry.addData("1:", G.ballColors[0]);
        telemetry.addData("2:", G.ballColors[1]);
        telemetry.addData("3:", G.ballColors[2]);
        telemetry.addData("Selected Blend:", "%.3f", selectedBlend);
        if (camera != null) {
            telemetry.addData("Live Blend:", "%.3f", camera.getStableTargetNormalized());
            telemetry.addData("Live Y:", camera.getRecommendedY());
            telemetry.addData("Confidence:", camera.hasConfidence());
        }
        telemetry.update();

        r.loop(r);
    }

    @Override
    public void stop() {
        if (camera != null) camera.stop();
        r.stop();
    }

    private FollowPathCmd createCameraIntakeCommand() {
        selectedBlend = getCameraTargetBlend();
        return new FollowPathCmd(r, p.cameraIntakePath(selectedBlend)).withStallTimeout(0.03, 2000);
    }

    private FollowPathCmd createCameraShootCommand() {
        return new FollowPathCmd(r, p.cameraShootPath(selectedBlend));
    }

    private double getCameraTargetBlend() {
        if (camera == null) {
            return 0.5;
        }

        camera.update();
        if (!camera.hasConfidence()) {
            return 0.5;
        }
        return camera.getDriveTargetNormalizedForSide(G.side);
    }
}
