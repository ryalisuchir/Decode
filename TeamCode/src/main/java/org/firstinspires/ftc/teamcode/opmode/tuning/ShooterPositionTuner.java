package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
@Config
public class ShooterPositionTuner extends CommandOpMode {

    Robot r;
    private final boolean threeBallRumbleLatched = false;
    Gamepad ahnaf;
    Trigger intakeTrigger;
    private long lastLoopTimeNs = 0;
    private double loopTimeMs = 0;
    private double loopHz = 0;

    public static double hoodPosition = Globals.HOOD_MAX;
    public double kV = 0.00045;
    public double kS = 0.02;
    public double kP = 0.0012;
    public static double targetVelocity = 0;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.initLoop(r);
        r.dt.startDrive();
        ahnaf = gamepad1;
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
                                        new InstantCommand(() -> r.spinner.intakeStop()),
                                        new InstantCommand(() -> r.spinner.transferStop())
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

        telemetry.addData("Obelisk: ", Globals.obeliskOptions);
        telemetry.addData("Shooter Velocity: ", r.shooter.getShooterVelocity());
        telemetry.addData("Hood: ", r.r.getPosition());
        telemetry.addData("X: ", r.dt.getPose().getX());
        telemetry.addData("Y: ", r.dt.getPose().getY());
        telemetry.addData("Loop Time (ms)", "%.2f", loopTimeMs);
        telemetry.addData("Loop Rate (Hz)", "%.1f", loopHz);

        telemetry.update();

        if (gamepad1.dpadLeftWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 1))
            );
        }
        if (gamepad1.dpadRightWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 2))
            );
        }
        if (gamepad1.dpadDownWasPressed()) {
            schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 3))
            );
        }

        double currentVel = -r.s2.getCorrectedVelocity();

        double ff = feedforward(targetVelocity);
        double fb = feedback(targetVelocity, currentVel);

        double power = ff + fb;
        power = clamp(power, 0, 1);

        // Apply to both shooter motors
        r.s1.set(power);
        r.s2.set(power);

        r.noOuttakeLoop(r);

        if (hoodPosition < Globals.HOOD_LOWERED) hoodPosition = Globals.HOOD_LOWERED;
        if (hoodPosition > Globals.HOOD_MAX) hoodPosition = Globals.HOOD_MAX;

        r.r.setPosition(hoodPosition);
        r.turret.followGoal();
        r.dt.drive(gamepad1);

        telemetry.addData("Distance: ", r.dt.getGoalDistance());
        telemetry.addData("X: ", r.dt.getPose().getX());
        telemetry.addData("Y: ", r.dt.getPose().getY());
        telemetry.addData("Current Velocity: ", r.s2.getCorrectedVelocity());
        telemetry.addData("Hood Value: ", r.r.getPosition());
        telemetry.update();
    }
    private double feedforward(double targetVel) {
        if (Math.abs(targetVel) < 1e-6) return 0;
        double sign = Math.signum(targetVel);
        return kS * sign + kV * targetVel;
    }

    private double feedback(double targetVel, double currentVel) {
        double error = targetVel - currentVel;
        return kP * error;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}