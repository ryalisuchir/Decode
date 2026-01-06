package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOneGreenTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOnePurpleTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOrderTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
public class Blue extends CommandOpMode {

    Robot r;
    private boolean threeBallRumbleLatched = false;
    Gamepad ahnaf, swetha;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.OTHER_DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.initLoop(r);
        r.dt.startDrive();
        ahnaf = gamepad1;
        swetha = gamepad2;
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeTrigger = new Trigger(
                () -> ahnaf.right_trigger > 0.1 && !r.rotator.threeBallsDetected()
        );
        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.INTAKING),
                                new InstantCommand(() -> r.rotator.spinIn()),
                                new InstantCommand(() -> r.rotator.openGate()),
                                KickCommands.resetAll(r.kicker)
                        )
                )
                .whenInactive(
                        new InstantCommand(() -> {
                            if (r.rotator.threeBallsDetected()) {
                                CommandScheduler.getInstance().schedule(
                                        new ParallelCommandGroup(
                                                r.rotator.transfer()
                                        )
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        r.rotator.stop()
                                );
                            }
                        })
                );
    }


    @Override
    public void run() {
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

        if (swetha.circleWasPressed()) {
            r.turret.xChange(1);
        }

        if (swetha.squareWasPressed()) {
            r.turret.yChange(1);
        }

        telemetry.addData("x of goal:", r.turret.goalX);
        telemetry.addData("y of goal:", r.turret.goalY);

        //Failsafes:
        if (swetha.leftBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOneGreenTCmd(r))
            );
        }

        if (swetha.rightBumperWasPressed()) {
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

        if (r.rotator.threeBallsDetected() && !threeBallRumbleLatched) {
            schedule(
                    new InstantCommand(() -> {
                        ahnaf.rumble(1000);
                        swetha.rumble(1000);
                        threeBallRumbleLatched = true;
                    })
            );
        }

        if (!r.rotator.threeBallsDetected()) {
            threeBallRumbleLatched = false;
        }

        r.dt.drive(gamepad1);
        r.loop(r);
    }
}