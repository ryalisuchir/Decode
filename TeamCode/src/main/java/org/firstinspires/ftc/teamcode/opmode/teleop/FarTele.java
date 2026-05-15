package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;

import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.BezierLine;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;

@TeleOp
public class FarTele extends CommandOpMode {

    Halo r;
    private boolean hasStarted = false;
    private boolean threeBallRumbleLatched = false;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;
    private boolean psLatch = false;
    private int telemetryDivider = 0;

    // Explicit flag — true only while right-bumper auto sequence is running
    private boolean isFollowingPath = false;

    // farBoi target angle — adjusted live via gamepad2 dpad_up/down
    private double farBoiDeg = (Globals.alliance == Globals.Alliance.RED) ? -125 : 125;

    private static final Pose SHOOT_POSE = new Pose(93.70939481268012, 11.134207492795397, Math.toRadians(0));

    private boolean drivetrainCommanded(Gamepad gp) {
        return Math.abs(gp.left_stick_x)  > 0.05 ||
                Math.abs(gp.left_stick_y)  > 0.05 ||
                Math.abs(gp.right_stick_x) > 0.05;
    }

    @Override
    public void initialize() {
        r = new Halo(hardwareMap, Globals.Positions.BLUE_CUBE_START, Globals.Alliance.BLUE, Globals.Match.TELEOP);

        r.dt.startDrive();
        telemetry = new PeacockTelemetry(this);

        // Constant turret — set to farBoi position immediately
        Globals.turretState = Globals.TurretState.SET_POSITION;
        applyFarBoiAngle();

        intakeTrigger = new Trigger(
                () -> gamepad1.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
        );

        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> {
                                    if (!r.spinner.threeBallsDetected()) {
                                        r.spinner.intakeIn();
                                        r.spinner.openGate();
                                    }
                                }),
                                new InstantCommand(() -> r.spinner.pivotIntake()),
                                KickCommands.resetAll(r.kicker)
                        )
                )
                .whenInactive(
                        new InstantCommand(() -> {
                            if (r.spinner.oneBallDetected()) {
                                CommandScheduler.getInstance().schedule(
                                        r.spinner.transfer()
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        new ParallelCommandGroup(
                                                r.spinner.intakeOut(),
                                                new InstantCommand(() -> r.spinner.transferStop()),
                                                new InstantCommand(() -> r.spinner.pivotReady())
                                        )
                                );
                            }
                        })
                );
    }

    /** Pushes the current farBoiDeg value to the turret. */
    private void applyFarBoiAngle() {
        double deg = farBoiDeg;
        Globals.turretState = Globals.TurretState.SET_POSITION;
        schedule(new InstantCommand(() -> {
            r.turret.setTargetDeg(deg);
        }));
    }

    @Override
    public void run() {
        // Manual drive only when no auto-path is running
        if (!isFollowingPath) {
            r.dt.drive(gamepad1);
        }

        if (!hasStarted) {
            telemetry.addLine("Move to begin.");
            telemetry.update();

            if (drivetrainCommanded(gamepad1)) {
                hasStarted = true;
                lastLoopTimeNs = 0;
            } else {
                return;
            }
        }

        long now = System.nanoTime();
        if (lastLoopTimeNs != 0) {
            loopTimeMs = (now - lastLoopTimeNs) / 1_000_000.0;
            loopHz = 1000.0 / loopTimeMs;
        }
        lastLoopTimeNs = now;

        if ((telemetryDivider++ & 0x3) == 0) {
            telemetry.addData("FarBoi Angle (deg)", "%.1f", farBoiDeg);
            telemetry.addData("Following Path", isFollowingPath);
            telemetry.addData("Obelisk Order", Globals.obeliskOptions.toString());
            telemetry.addData("Shooter RPM", r.shooter.getShooterRPM());
            telemetry.addData("Loop Time (ms)", "%.2f", loopTimeMs);
            telemetry.addData("Loop Rate (Hz)", "%.1f", loopHz);
            telemetry.update();
        }

        // Right bumper: auto-drive to shoot pose, then fire all 3
        if (gamepad1.rightBumperWasPressed() && !isFollowingPath) {
            Pose current = r.dt.getPose();
            PathChain driveToShoot = r.dt.getFollower().pathBuilder()
                    .addPath(new BezierLine(current, SHOOT_POSE))
                    .setLinearHeadingInterpolation(current.getHeading(), SHOOT_POSE.getHeading())
                    .build();

            isFollowingPath = true;
            schedule(new SequentialCommandGroup(
                    new InstantCommand(() -> isFollowingPath = true),
                    new FollowPathCmd(r, driveToShoot),
                    new RapidAllCmd(r),
                    new InstantCommand(() -> {
                        r.dt.getFollower().breakFollowing();
                        isFollowingPath = false;
                    })
            ));
        }

        if (gamepad1.leftBumperWasPressed()) {
            if (r.dt.getPose().getY() < 45) {
                schedule(new RapidAllCmd(r, 100));
            } else {
                schedule(new RapidAllCmd(r));
            }
        }

        boolean psPressed = gamepad1.ps || gamepad2.ps;
        if (psPressed && !psLatch) {
            // PS button also cancels any active auto-path
            isFollowingPath = false;
            r.dt.getFollower().breakFollowing();
            schedule(new ParallelCommandGroup(
                    r.dt.resetPose(),
                    new InstantCommand(() -> {
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                    })
            ));
        }
        psLatch = psPressed;

        if (r.spinner.threeBallsDetected() && !threeBallRumbleLatched) {
            schedule(new InstantCommand(() -> {
                gamepad1.rumble(1000);
                threeBallRumbleLatched = true;
            }));
        }

        if (gamepad2.crossWasPressed()) {
            // Keep turret in SET_POSITION (constant) — cross does nothing in DBZ
        }

        // dpad_up/down adjusts the farBoi angle live
        if (gamepad2.dpad_up) {
            farBoiDeg += 1;
            applyFarBoiAngle();
        }

        if (gamepad2.dpad_down) {
            farBoiDeg -= 1;
            applyFarBoiAngle();
        }

        if (gamepad2.dpad_right) {
            switch (Globals.obeliskOptions) {
                case PPG: Globals.obeliskOptions = Globals.ObeliskOptions.PGP; break;
                case PGP: Globals.obeliskOptions = Globals.ObeliskOptions.GPP; break;
                case GPP: Globals.obeliskOptions = Globals.ObeliskOptions.PPG; break;
                default:  Globals.obeliskOptions = Globals.ObeliskOptions.PPG; break;
            }
        }

        if (!r.spinner.threeBallsDetected()) {
            threeBallRumbleLatched = false;
        }

        r.farLoop(r);
    }
}