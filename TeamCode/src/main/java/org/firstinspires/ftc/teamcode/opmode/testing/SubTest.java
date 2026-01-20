package org.firstinspires.ftc.teamcode.opmode.testing;

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
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;

@TeleOp
public class SubTest extends CommandOpMode {

    Robot r;
    private final boolean selfDriving = false;
    private boolean hasStarted = false;
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

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);

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

    public PathChain intakeHpAndShoot(Pose currPos, Pose shootFarPos) {
        return r.dt.getFollower().pathBuilder()
                .addPath(
                        new BezierLine(
                                currPos,
                                shootFarPos
                        )
                ).setLinearHeadingInterpolation(currPos.getHeading(), shootFarPos.getHeading())
                .build();
    }

    @Override
    public void run() {

            r.dt.drive(gamepad1);

        if (ahnaf.rightBumperWasPressed()) {
            Pose shootFarPos = new Pose(72, 20, Math.toRadians(0));
            Pose currPos = r.dt.getPose();

            r.shooter.setCustomDistance(shootFarPos.getX()-5, shootFarPos.getY()-5);
            Globals.turretState = Globals.TurretState.RED_FAR_GOAL_TELE;

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

        telemetry.addData("Turret Status, ", Globals.turretState);


        long now = System.nanoTime();

        if (lastLoopTimeNs != 0) {
            loopTimeMs = (now - lastLoopTimeNs) / 1_000_000.0;
            loopHz = 1000.0 / loopTimeMs;
        }

        lastLoopTimeNs = now;
        telemetry.addData("tx: ", Vision.getTx());
        telemetry.addData("Shooter Power: ", r.shooter.getShooterPower());
        telemetry.addData("Shooter Velocity: ", r.shooter.getShooterVelocity());

        telemetry.update();

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