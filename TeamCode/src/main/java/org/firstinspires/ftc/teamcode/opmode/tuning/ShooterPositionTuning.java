package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;

@TeleOp
@Config
@Configurable
public class ShooterPositionTuning extends CommandOpMode {

    Halo r;
    Trigger intakeTrigger;

    public static double turretPosition = Globals.Turret.TURRET_FORWARD;

    public static double hoodPosition = Globals.HOOD.getMax();

    public static double TARGET_VEL = 0.0;
    public static double POS_TOLERANCE = 0;

    private static final PIDFController launcherPIDF = new PIDFController(Globals.shooterCoefficients);

    @Override
    public void initialize() {
        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        r = new Halo(hardwareMap, Globals.Positions.RED_CUBE_START, Globals.Alliance.RED, Globals.Match.TESTING);

        r.initLoop(r);
        r.dt.startDrive();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        intakeTrigger = new Trigger(
                () -> gamepad1.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
        );

        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> r.spinner.intakeIn()),
                                new InstantCommand(() -> r.spinner.openGate()),
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
        if (gamepad1.circleWasPressed()) {
            telemetry.addLine("Circle!!");
            CommandScheduler.getInstance().schedule(RapidKickCommands.kickAndResetMany(r, 2, 3, 1));
        }

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

        double currentVel = r.shooter2.getCorrectedVelocity();

        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        launcherPIDF.setSetPoint(TARGET_VEL);

        double power = launcherPIDF.calculate(currentVel, TARGET_VEL);

        r.shooter1.set(power);
        r.shooter2.set(power);

        if (hoodPosition < Globals.HOOD.getMin()) hoodPosition = Globals.HOOD.getMin();
        if (hoodPosition > Globals.HOOD.getMax()) hoodPosition = Globals.HOOD.getMax();

        r.hood.setPosition(hoodPosition);
//        r.turret.followGoal();
        r.dt.drive(gamepad1);

        r.t1.setPosition(turretPosition);
        r.t2.setPosition(turretPosition);

        telemetry.addData("Distance: ", r.dt.getGoalDistance());
        telemetry.addData("Pose: ", r.dt.getPose());
        telemetry.addData("Best Turret Value: ", r.turret.getBestTurretPosition());
        telemetry.addData("Best Shooting Value: ", r.shooter.velPos);
        telemetry.addData("Best Hood Value: ", r.shooter.hoodPose);
        telemetry.addData("Shooter Velocity: ", r.shooter1.getCorrectedVelocity());
        telemetry.addData("Shooter Motor RPM: ", r.shooter1.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Hood Value: ", r.hood.getPosition());
        telemetry.addData("Section 1: ", Globals.ballColors[0]);
        telemetry.addData("Section 2: ", Globals.ballColors[1]);
        telemetry.addData("Section 3: ", Globals.ballColors[2]);
        telemetry.addData("Three Detected:", r.spinner.threeBallsDetected());
        telemetry.update();

        r.noOuttakeLoop(r);
    }
}