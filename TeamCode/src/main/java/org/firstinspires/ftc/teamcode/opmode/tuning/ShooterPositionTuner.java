package org.firstinspires.ftc.teamcode.opmode.tuning;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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
public class ShooterPositionTuner extends OpMode {
    Robot r;
    public static double hoodPosition = Globals.HOOD_MAX;
    Trigger intakeTrigger;

    public double kV = 0.00045;
    public double kS = 0.02;
    public double kP = 0.0012;
    public static double targetVelocity = 0;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intakeTrigger = new Trigger(
                () -> gamepad1.right_trigger > 0.1 && !r.rotator.threeBallsDetected()
        );
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        r.dt.startDrive();
    }


    @Override
    public void loop() {
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
                                        r.rotator.transfer()
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        r.rotator.stop()
                                );
                            }
                        })
                );

        if (gamepad1.dpadLeftWasPressed()) {
            CommandScheduler.getInstance().schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 1))
            );
        }
        if (gamepad1.dpadRightWasPressed()) {
            CommandScheduler.getInstance().schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 2))
            );
        }
        if (gamepad1.dpadDownWasPressed()) {
            CommandScheduler.getInstance().schedule(
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