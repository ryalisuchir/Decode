//package org.firstinspires.ftc.teamcode.opmode.tuning;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.bylazar.configurables.annotations.Configurable;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.seattlesolvers.solverslib.command.CommandOpMode;
//import com.seattlesolvers.solverslib.command.CommandScheduler;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
//import com.seattlesolvers.solverslib.command.button.Trigger;
//
//import org.firstinspires.ftc.teamcode.common.Globals;
//import org.firstinspires.ftc.teamcode.common.Halo;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
//import org.firstinspires.ftc.teamcode.common.utility.Vision;
////import org.firstinspires.ftc.teamcode.common.utility.turret.TurretCalibrator;
//
//@TeleOp
//@Config
//@Configurable
//public class TurretTuner extends CommandOpMode {
//    Halo r;
//    Trigger intakeTrigger;
//    public static double shooterPower = 0;
//    public static double hoodAngle = Globals.HOOD.getMax();
//    TurretCalibrator t;
//
//    @Override
//    public void initialize() {
//        r = new Halo(hardwareMap, Globals.Positions.RED_CUBE_START, Globals.Alliance.RED, Globals.Match.AUTO);
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        r.initLoop(r);
//        r.dt.startDrive();
//        t = new TurretCalibrator();
//
//        intakeTrigger = new Trigger(
//                () -> gamepad1.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
//        );
//        intakeTrigger
//                .whileActiveContinuous(
//                        new ParallelCommandGroup(
//                                new InstantCommand(() -> r.spinner.intakeIn()),
//                                new InstantCommand(() -> r.spinner.openGate()),
//                                KickCommands.resetAll(r.kicker)
//                        )
//                )
//                .whenInactive(
//                        new InstantCommand(() -> {
//                            if (r.spinner.oneBallDetected()) {
//                                CommandScheduler.getInstance().schedule(
//                                        r.spinner.transfer()
//                                );
//                            } else {
//                                CommandScheduler.getInstance().schedule(
//                                        new InstantCommand(() -> r.spinner.intakeStop()),
//                                        new InstantCommand(() -> r.spinner.transferStop())
//                                );
//                            }
//                        })
//                );
//    }
//
//    @Override
//    public void run() {
//        telemetry.update();
//        r.hood.setPosition(hoodAngle);
//        r.shooter1.set(shooterPower);
//        r.shooter2.set(shooterPower);
//
//        telemetry.addData("tx: ", Vision.getTx());
//        telemetry.update();
//
//        t.update(
//                r.turret,
//                gamepad1,
//                telemetry,
//                r.dt.getPose().getX(),
//                r.dt.getPose().getY(),
//                r.dt.getPose().getHeading()
//        );
//
//        if (gamepad1.dpadLeftWasPressed()) {
//            schedule(
//                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 1))
//            );
//        }
//        if (gamepad1.dpadRightWasPressed()) {
//            schedule(
//                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 2))
//            );
//        }
//        if (gamepad1.dpadDownWasPressed()) {
//            schedule(
//                    new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 3))
//            );
//        }
//
//        if (gamepad1.circleWasPressed()) {
//            schedule(new RapidAllCmd(r));
//        }
//
//        r.dt.drive(gamepad1);
//        r.noOuttakeLoop(r);
//    }
//}