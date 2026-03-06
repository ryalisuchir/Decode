package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOnePurpleTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOrderTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;

@TeleOp
public class SubTest extends CommandOpMode {

    Halo r;
    private boolean hasStarted = false;
    private boolean threeBallRumbleLatched = false;
    Gamepad suchir;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;

    private boolean driveHoldEnabled = false;
    private boolean psLatch = false;
    private boolean leftStickLatch = false;
    private int telemetryDivider = 0;
    Telemetry telemetry;

    private boolean drivetrainCommanded(Gamepad gp) {
        return Math.abs(gp.left_stick_x)  > 0.05 ||
                Math.abs(gp.left_stick_y)  > 0.05 ||
                Math.abs(gp.right_stick_x) > 0.05;
    }


    @Override
    public void initialize() {
        r = new Halo(hardwareMap, G.RED_FAR_START, G.Side.RED, true);
        r.init();
        r.dt.startDrive();
        suchir = gamepad1;
        telemetry = new PeacockTelemetry(this);
        intakeTrigger = new Trigger(
                () -> suchir.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
        );
        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> r.spinner.intakeIn()),
                                new InstantCommand(() -> r.spinner.openGate()),
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
                                                new InstantCommand(() -> r.spinner.transferStop())
                                        )
                                );
                            }
                        })
                );
    }

    @Override
    public void run() {
//        r.profiler.start("Full Loop");
        r.dt.drive(gamepad1);

        if (!hasStarted) {
            if (drivetrainCommanded(suchir)) {
                hasStarted = true;
                lastLoopTimeNs = 0;
            } else {
                return;
            }
        }

        long now = System.nanoTime();

        if (lastLoopTimeNs != 0) {
            loopTimeMs = (now - lastLoopTimeNs) / 1_000_000.0;
        }

        lastLoopTimeNs = now;
        if ((telemetryDivider++ & 0x3) == 0) {
            telemetry.addData("Loop time: ", loopTimeMs);
            telemetry.addData("Distance: ", r.dt.getGoalDistance());
            telemetry.addData("1: ", G.ballColors[0]);
            telemetry.addData("2: ", G.ballColors[1]);
            telemetry.addData("3: ", G.ballColors[2]);
            telemetry.update();
        }

        if (suchir.leftBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOrderTCmd(r))
            );
        }

        boolean psPressed = suchir.ps;
        if (psPressed && !psLatch) {
            schedule(
                    new ParallelCommandGroup(
                            r.dt.resetPose(),
                            new InstantCommand(() -> suchir.rumble(1000))
                    )
            );
        }
        psLatch = psPressed;

        if (suchir.crossWasPressed()) { //rapid fire
            if (r.dt.getFollower().getPose().getY() < 40) {
                schedule(new RapidSlowerCmd(r));
            } else {
                schedule(
                        new RapidAllCmd(r)
                );
            }
        }

        if (suchir.circleWasPressed()) {
            driveHoldEnabled = !driveHoldEnabled;

            if (driveHoldEnabled) {
                schedule(new InstantCommand(() -> r.dt.holdCurrent()));
            } else {
                schedule(new InstantCommand(() -> r.dt.releaseHold()));
            }
        }
        
        if (suchir.left_stick_button && !leftStickLatch) {
            schedule(new ResetShooterCmd(r));
        }
        
        leftStickLatch = suchir.left_stick_button;

        if (suchir.squareWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOnePurpleTCmd(r))
            );
        }

        if (r.spinner.threeBallsDetected() && !threeBallRumbleLatched) {
            schedule(
                    new InstantCommand(() -> {
                        suchir.rumble(1000);
                        threeBallRumbleLatched = true;
                    })
            );
        }

        if (!r.spinner.threeBallsDetected()) {
            threeBallRumbleLatched = false;
        }

        r.noSubsystemLoop(r);
        r.dt.loop();
        r.spinner.periodic();
        r.shooter.loop();
        r.turret.loop();
    }

    @Override
    public void end() {
        r.stop();
    }
}
