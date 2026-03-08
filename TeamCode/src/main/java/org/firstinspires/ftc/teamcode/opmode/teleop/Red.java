package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOneGreenTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOnePurpleTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOrderTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.RapidAllAndResetCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.RapidFarAndResetCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.Reset;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;

@TeleOp
public class Red extends CommandOpMode {

    Halo r;
    private boolean hasStarted = false;
    private final boolean swethaCrossToggle = false;
    private boolean threeBallRumbleLatched = false;
    Gamepad ahnaf, swetha;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;
    private boolean psLatch = false;
    private int telemetryDivider = 0;

    private boolean driveHoldEnabled = false;

    private boolean drivetrainCommanded(Gamepad gp) {
        return Math.abs(gp.left_stick_x)  > 0.05 ||
                Math.abs(gp.left_stick_y)  > 0.05 ||
                Math.abs(gp.right_stick_x) > 0.05;
    }



    @Override
    public void initialize() {
        r = new Halo(hardwareMap, G.RED_CUBE_START, G.Side.RED, false);

        r.dt.startDrive();
        ahnaf = gamepad1;
        swetha = gamepad2;
        telemetry = new PeacockTelemetry(this);
        intakeTrigger = new Trigger(
                () -> ahnaf.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
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
        r.dt.drive(gamepad1);
        if (!hasStarted) {
            telemetry.addLine("Move to begin.");
            telemetry.update();

            if (drivetrainCommanded(ahnaf)) {
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
            telemetry.addData("Color 1: ", G.ballColors[0]);
            telemetry.addData("Color 2: ", G.ballColors[1]);
            telemetry.addData("Color 3: ", G.ballColors[2]);
            telemetry.addData("Obelisk: ", G.obeliskOptions);
            telemetry.addData("Shooter State: ", G.shooterState);
            telemetry.addData("Shooter Power: ", r.shooter.getShooterPower());
            telemetry.addData("Shooter RPM: ", r.shooter.getShooterRPM());
            telemetry.addData("Shooter Velocity: ", r.shooter.getShooterVelocity());
            telemetry.addData("Loop Time (ms)", "%.2f", loopTimeMs);
            telemetry.addData("Loop Rate (Hz)", "%.1f", loopHz);
            telemetry.addData("Pose: ", r.dt.getPose());
            telemetry.addData("Side: ", G.side);
            telemetry.addData("DT Side: ", r.dt.a);
            telemetry.addData("Transfer State: ", G.transferState);
            telemetry.update();
        }

        if (ahnaf.rightBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOrderTCmd(r))
            );
        }


        boolean psPressed = ahnaf.ps || swetha.ps;
        if (psPressed && !psLatch) {
            schedule(
                    new ParallelCommandGroup(
                            r.dt.resetPose(),
                            new InstantCommand(() -> {
                                ahnaf.rumble(1000);
                                swetha.rumble(1000);
                            })
                    )
            );
        }
        psLatch = psPressed;

        if (ahnaf.left_bumper) { //rapid fire
            if (r.dt.getFollower().getPose().getY() < 40) {
                schedule(new RapidFarAndResetCmd(r));
            } else {
                schedule(
                        new RapidAllAndResetCmd(r)
                );
            }
        }

        if (swetha.circleWasPressed()) {
            G.KICK_WAIT_TELE = 500;
        }

        //Failsafes:
        if (swetha.leftBumperWasPressed() || ahnaf.triangleWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOneGreenTCmd(r))
            );
        }

        if (ahnaf.circleWasPressed()) {
            driveHoldEnabled = !driveHoldEnabled;

            if (driveHoldEnabled) {
                schedule(new InstantCommand(() -> r.dt.holdCurrent()));
            } else {
                schedule(new InstantCommand(() -> r.dt.releaseHold()));
            }
        }

        if (swetha.rightBumperWasPressed() || ahnaf.squareWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOnePurpleTCmd(r))
            );
        }

        if (swetha.triangleWasPressed()) {
            schedule(KickCommands.kickAndResetMany(r.kicker, 1, 2, 3));
        }

        if (swetha.crossWasPressed()) {
            G.turretState = G.TurretState.FOLLOWING;
        }

        if (swetha.dpadLeftWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 1))
            );
        }

        if (swetha.dpadRightWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 2))
            );
        }

        if (swetha.dpadDownWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 3))
            );
        }

        if (r.spinner.threeBallsDetected() && !threeBallRumbleLatched) {
            schedule(
                    new InstantCommand(() -> {
                        ahnaf.rumble(1000);
                        swetha.rumble(1000);
                        threeBallRumbleLatched = true;
                    })
            );
        }

        if (!r.spinner.threeBallsDetected()) {
            threeBallRumbleLatched = false;
        }

        r.loop(r);
    }
}
