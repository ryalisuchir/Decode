package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
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
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOneGreenTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOnePurpleTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOrderTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.Reset;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
public class Blue extends CommandOpMode {

    Robot r;
    private boolean hasStarted = false;
    private final boolean swethaCrossToggle = false;
    private boolean threeBallRumbleLatched = false;
    Gamepad ahnaf, swetha;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;

    private boolean driveHoldEnabled = false;

    private boolean drivetrainCommanded(Gamepad gp) {
        return Math.abs(gp.left_stick_x)  > 0.05 ||
                Math.abs(gp.left_stick_y)  > 0.05 ||
                Math.abs(gp.right_stick_x) > 0.05;
    }


    public PathChain intakeHpAndShoot(Pose currPos, Pose shootFarPos) {
        return r.dt.getFollower().pathBuilder()
                .addPath(
                        new BezierLine(
                                currPos,
                                shootFarPos.mirror()
                        )
                ).setLinearHeadingInterpolation(currPos.getHeading(), shootFarPos.mirror().getHeading())
                .build();
    }

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.BLUE_FAR_START, Globals.Side.BLUE, false);

        r.dt.startDrive();
        ahnaf = gamepad1;
        swetha = gamepad2;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
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
        r.dt.loop();

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

        telemetry.addData("Color 1: ", Globals.ballColors[0]);
        telemetry.addData("Color 2: ", Globals.ballColors[1]);
        telemetry.addData("Color 3: ", Globals.ballColors[2]);
        telemetry.addData("Obelisk: ", Globals.obeliskOptions);
        telemetry.addData("Shooter State: ", Globals.shooterState);
        telemetry.addData("Shooter Power: ", r.shooter.getShooterPower());
        telemetry.addData("Shooter RPM: ", r.shooter.getShooterRPM());
        telemetry.addData("Shooter Velocity: ", r.shooter.getShooterVelocity());
        telemetry.addData("Loop Time (ms)", "%.2f", loopTimeMs);
        telemetry.addData("Loop Rate (Hz)", "%.1f", loopHz);
        telemetry.addData("Pose: ", r.dt.getPose());
        telemetry.addData("Side: ", Globals.side);
        telemetry.addData("DT Side: ", r.dt.a);
        telemetry.addData("Transfer State: ", Globals.transferState);

        telemetry.update();

        if (ahnaf.rightBumperWasPressed()) {
            Pose shootFarPos = new Pose(92, 17, Math.toRadians(0));
            Pose currPos = r.dt.getPose();

            r.shooter.setCustomDistance(shootFarPos.getX()-11, shootFarPos.getY()-11);
            Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL;

            schedule(
                    new SequentialCommandGroup(
                            new FollowPathCmd(r, intakeHpAndShoot(currPos, shootFarPos)),
                            new WaitCommand(100),
                            new KickOrderACmd(r),
                            new Reset(r),
                            new InstantCommand(() -> r.dt.startDrive())
                    )
            );
        }

        if (ahnaf.leftBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOrderTCmd(r))
            );
        }

        if (ahnaf.ps || swetha.ps) {
            schedule(
                    new ParallelCommandGroup(
                            r.dt.corner(),
                            new InstantCommand(() -> {
                                ahnaf.rumble(1000);
                                swetha.rumble(1000);
                            })
                    )
            );
        }

        if (ahnaf.crossWasPressed()) { //rapid fire
            schedule(
                    RapidKickCommands.kickAndResetMany(r,3,1,2)
            );
        }

        if (swetha.circleWasPressed()) {
            Globals.KICK_WAIT_TELE = 500;
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
            Globals.turretState = Globals.TurretState.FOLLOWING;
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