package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

@TeleOp
@Config
public class ShooterPositionTuner extends CommandOpMode {

    Halo r;
    Trigger intakeTrigger;

    public static double turretPosition = G.TURRET_RESET;

    public static double hoodPosition = G.HOOD_MAX;
    public static double P = 0.0023;
    public static double I = 0;
    public static double D = 0;
    public static double F = 0.00036;

    public static double TARGET_VEL = 0.0;
    public static double POS_TOLERANCE = 0;

    private static final PIDFController launcherPIDF = new PIDFController(P, I, D, F);

    @Override
    public void initialize() {
        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        r = new Halo(hardwareMap, G.RED_CUBE_START, G.Side.RED, true);

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
        if (gamepad1.circleWasPressed()) {
            telemetry.addLine("Circle!!");
            CommandScheduler.getInstance().schedule(RapidKickCommands.kickAndResetMany(r, 2, 3, 1));
        }

        if (gamepad1.circleWasPressed()) {
            telemetry.addLine("Cross!!");
            CommandScheduler.getInstance().schedule(new RapidSlowerCmd(r));
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

        double currentVel = r.s1.getCorrectedVelocity();

        launcherPIDF.setPIDF(P, I, D, F);

        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        launcherPIDF.setSetPoint(TARGET_VEL);

        double power = launcherPIDF.calculate(currentVel, TARGET_VEL);

        r.s1.set(power);
        r.s2.set(power);

        if (hoodPosition < G.HOOD_LOWERED) hoodPosition = G.HOOD_LOWERED;
        if (hoodPosition > G.HOOD_MAX) hoodPosition = G.HOOD_MAX;

        r.r.setPosition(hoodPosition);
//        r.turret.followGoal();
        r.dt.drive(gamepad1);

        r.t1.setPosition(turretPosition);
        r.t2.setPosition(turretPosition);

        telemetry.addData("Distance: ", r.dt.getGoalDistance());
        telemetry.addData("Pose: ", r.dt.getPose());
        telemetry.addData("Best Turret Value: ", r.turret.getBestTurretPosition());
        telemetry.addData("Best Shooting Value: ", r.shooter.velPos);
        telemetry.addData("Best Hood Value: ", r.shooter.hoodPose);
        telemetry.addData("Shooter Velocity: ", r.s1.getCorrectedVelocity());
        telemetry.addData("Shooter Motor RPM: ", r.s1.getCorrectedVelocity() / 28 * 60);
        telemetry.addData("Hood Value: ", r.r.getPosition());
        telemetry.addData("Section 1: ", G.ballColors[0]);
        telemetry.addData("Section 2: ", G.ballColors[1]);
        telemetry.addData("Section 3: ", G.ballColors[2]);
        telemetry.addData("Three Detected:", r.spinner.threeBallsDetected());
        telemetry.update();

        //pose, turret pos
//93, 7, 86, 0.44
        //82, 17, 65, 0.5
        //74, 10.33, 68, 0.47
        //53, 16.39, 60.82, 0.455
        //73, 28.99, 166.4, 0.135

    //distance, vel, hood
        //31, 1570, 0.65
        //52, 1760, 0.76
        //72, 1850, 0.78
        //82, 1900, 0.78
        //95, 2050, 0.80
        //118, 2100, 0.80
        //125, 2100, 0.84
        //131, 2200, 0.82
        //134, 2200, 0.82
        //140, 2400, 0.85

        //2400, 0.84

        r.noOuttakeLoop(r);
    }
}