package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.EnsuredOrderCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidOrderCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;
@TeleOp
public class Blue extends CommandOpMode {

    Halo r;
    private boolean hasStarted = false;
    private boolean threeBallRumbleLatched = false;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;
    private boolean psLatch = false;
    private int telemetryDivider = 0;

    private boolean velocityLockEnabled = false;
    private boolean squareLatch = false;

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

    @Override
    public void run() {
        r.dt.drive(gamepad1);

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
            telemetry.addData("Velocity Lock", velocityLockEnabled ? "ON" : "OFF");
            telemetry.addData("Obelisk Order", Globals.obeliskOptions.toString());
            telemetry.addData("Shooter RPM", r.shooter.getShooterRPM());
            telemetry.addData("Loop Time (ms)", "%.2f", loopTimeMs);
            telemetry.addData("Loop Rate (Hz)", "%.1f", loopHz);
            telemetry.update();
        }

        if (gamepad1.rightBumperWasPressed()) {
            schedule(new UninterruptibleCommand(new EnsuredOrderCmd(r)));
        }

        if (gamepad1.leftBumperWasPressed()) {
            if (r.dt.getPose().getY() < 45) {
                schedule (new RapidAllCmd(r, 100));
            } else {
                schedule(new RapidAllCmd(r));
            }
        }

        boolean psPressed = gamepad1.ps || gamepad2.ps;
        if (psPressed && !psLatch) {
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
            Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
        }

        if (gamepad2.dpad_up) {
            r.turret.incrementOffset(true);
        }

        if (gamepad2.dpad_down) {
            r.turret.incrementOffset(true);
        }

        if (gamepad2.squareWasPressed()) {
            velocityLockEnabled = !velocityLockEnabled;
            if (velocityLockEnabled) {
                r.shooter.setVelocityCap(1450);
            } else {
                r.shooter.clearVelocityCap();
            }
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

        r.loop(r);
    }
}