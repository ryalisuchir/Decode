package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretCalibrator;

@TeleOp
@Config
public class TurretTuner extends CommandOpMode {
    Robot r;
    Trigger intakeTrigger;
    public static double shooterPower = 0;
    public static double hoodAngle = Globals.HOOD_MAX;
    TurretCalibrator t;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.OTHER_DEFAULT_START_POSE, Globals.Side.RED, true);
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