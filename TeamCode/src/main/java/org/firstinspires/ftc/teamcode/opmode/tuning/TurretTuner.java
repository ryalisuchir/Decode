package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretCalibrator;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;

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
public class TurretTuner extends CommandOpMode {
    Halo r;
    Trigger intakeTrigger;
    public static double shooterPower = 0;
    public static double hoodAngle = G.HOOD_MAX;
    TurretCalibrator t;

    @Override
    public void initialize() {
        r = new Halo(hardwareMap, G.BLUE_CUBE_START, G.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        r.initLoop(r);
        r.dt.startDrive();
        t = new TurretCalibrator();

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
        telemetry.update();
        r.r.setPosition(hoodAngle);
        r.s1.set(shooterPower);
        r.s2.set(shooterPower);

        telemetry.addData("tx: ", Vision.getTx());
        telemetry.update();

        t.update(
                r.turret,
                gamepad1,
                telemetry,
                r.dt.getPose().getX(),
                r.dt.getPose().getY(),
                r.dt.getPose().getHeading()
        );

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

        if (gamepad1.circleWasPressed()) {
            schedule(new KickOrderACmd(r));
        }

        r.dt.drive(gamepad1);
        r.noOuttakeLoop(r);
    }
}