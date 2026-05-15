//package org.firstinspires.ftc.teamcode.opmode.tuning;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
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
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.RapidKickCommands;
//import org.firstinspires.ftc.teamcode.common.utility.turret.TurretMath;
//
//@TeleOp
//@Config
//public class ShootTurret extends CommandOpMode {
//
//    // -------------------------------------------------------------------------
//    //  TILE OFFSET TABLE — edit live from FTC Dashboard.
//    //
//    //  Grid (field origin = bottom-left):
//    //
//    //    row 6 (y=120-144):  T31  T32  T33  T34  T35  T36
//    //    row 5 (y= 96-120):  T25  T26  T27  T28  T29  T30
//    //    row 4 (y= 72- 96):  T19  T20  T21  T22  T23  T24
//    //    row 3 (y= 48- 72):  T13  T14  T15  T16  T17  T18
//    //    row 2 (y= 24- 48):   T7   T8   T9  T10  T11  T12
//    //    row 1 (y=  0- 24):   T1   T2   T3   T4   T5   T6
//    //                         x=0  x=24 x=48 x=72 x=96 x=120
//    //
//    //  Each variable is { offsetX, offsetY } in inches added to the goal center.
//    //  Tweak in Dashboard → drive to that tile → fire → repeat.
//    // -------------------------------------------------------------------------
//
//    // row 1
//    public static double T1_X  = 0, T1_Y  = 0;
//    public static double T2_X  = 0, T2_Y  = 0;
//    public static double T3_X  = 19, T3_Y  = -3;
//    public static double T4_X  = 10, T4_Y  = -10;
//    public static double T5_X  = 0, T5_Y  = 0;
//    public static double T6_X  = 0, T6_Y  = 0;
//    // row 2
//    public static double T7_X  = 0, T7_Y  = 0;
//    public static double T8_X  = 0, T8_Y  = 0;
//    public static double T9_X  = 0, T9_Y  = 0;
//    public static double T10_X = 0, T10_Y = 0;
//    public static double T11_X = 0, T11_Y = 0;
//    public static double T12_X = 0, T12_Y = 0;
//    // row 3
//    public static double T13_X = 0, T13_Y = 0;
//    public static double T14_X = 0, T14_Y = 0;
//    public static double T15_X = 0, T15_Y = 0;
//    public static double T16_X = 0, T16_Y = 0;
//    public static double T17_X = 0, T17_Y = 0;
//    public static double T18_X = 0, T18_Y = 0;
//    // row 4
//    public static double T19_X = 0, T19_Y = 0;
//    public static double T20_X = 0, T20_Y = 0;
//    public static double T21_X = 0, T21_Y = 0;
//    public static double T22_X = 0, T22_Y = 0;
//    public static double T23_X = 0, T23_Y = 0;
//    public static double T24_X = 0, T24_Y = 0;
//    // row 5
//    public static double T25_X = 0, T25_Y = 0;
//    public static double T26_X = 0, T26_Y = 0;
//    public static double T27_X = 0, T27_Y = -3;
//    public static double T28_X = 0, T28_Y = 0;
//    public static double T29_X = 0, T29_Y = 0;
//    public static double T30_X = 0, T30_Y = 0;
//    // row 6
//    public static double T31_X = 0, T31_Y = -9;
//    public static double T32_X = 0, T32_Y = -9;
//    public static double T33_X = 0, T33_Y = -9;
//    public static double T34_X = 0, T34_Y = -9;
//    public static double T35_X = 0, T35_Y = -6;
//    public static double T36_X = 0, T36_Y = -6;
//
//    // -------------------------------------------------------------------------
//
//    Halo r;
//    Trigger intakeTrigger;
//
//    @Override
//    public void initialize() {
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//        r = new Halo(hardwareMap, Globals.Positions.RED_FAR_START, Globals.Alliance.RED, Globals.Match.TESTING);
//        r.initLoop(r);
//        r.dt.startDrive();
//
//        // Start shooter running so r.shooter.loop() takes over velocity + hood
//        CommandScheduler.getInstance().schedule(r.shooter.startShooter());
//
//        intakeTrigger = new Trigger(
//                () -> gamepad1.right_trigger > 0.1 && !r.spinner.threeBallsDetected()
//        );
//
//        intakeTrigger
//                .whileActiveContinuous(
//                        new ParallelCommandGroup(
//                                new InstantCommand(() -> r.spinner.intakeIn()),
//                                new InstantCommand(() -> r.spinner.openGate()),
//                                new InstantCommand(() -> r.spinner.pivotIntake()),
//                                KickCommands.resetAll(r.kicker)
//                        )
//                )
//                .whenInactive(
//                        new InstantCommand(() -> {
//                            if (r.spinner.oneBallDetected()) {
//                                CommandScheduler.getInstance().schedule(r.spinner.transfer());
//                            } else {
//                                CommandScheduler.getInstance().schedule(
//                                        new ParallelCommandGroup(
//                                                r.spinner.intakeOut(),
//                                                new InstantCommand(() -> r.spinner.transferStop()),
//                                                new InstantCommand(() -> r.spinner.pivotReady())
//                                        )
//                                );
//                            }
//                        })
//                );
//    }
//
//    /
//
//    @Override
//    public void run() {
//        syncOffsets();
//
//        // Kick controls
//        if (gamepad1.circleWasPressed())    CommandScheduler.getInstance().schedule(RapidKickCommands.kickAndResetMany(r, 2, 3, 1));
//        if (gamepad1.dpadLeftWasPressed())  schedule(new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 1)));
//        if (gamepad1.dpadRightWasPressed()) schedule(new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 2)));
//        if (gamepad1.dpadDownWasPressed())  schedule(new UninterruptibleCommand(KickCommands.kickAndReset(r.kicker, 3)));
//
//        // Shooter subsystem loop handles velocity + hood via LUT + tile offsets
//        r.shooter.loop();
//
//        // Turret + drive
//        r.turret.loop();
//        Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
//        r.dt.drive(gamepad1);
//
//        // Tile info for telemetry
//        double rx = r.dt.getPose().getX();
//        double ry = r.dt.getPose().getY();
//        int col     = (int) Math.max(1, Math.min(6, Math.floor(rx / 24.0) + 1));
//        int row     = (int) Math.max(1, Math.min(6, Math.floor(ry / 24.0) + 1));
//        int tileNum = (row - 1) * 6 + col;
//        double[] activeOffset = TurretMath.getInterpolatedOffset(rx, ry);
//
//        telemetry.addLine("=== POSITION ===");
//        telemetry.addData("Pose",           r.dt.getPose());
//        telemetry.addData("Nearest tile",   tileNum + "  (col " + col + ", row " + row + ")");
//        telemetry.addData("Active offsetX", String.format("%.2f\"", activeOffset[0]));
//        telemetry.addData("Active offsetY", String.format("%.2f\"", activeOffset[1]));
//        telemetry.addData("Goal distance",  r.dt.getGoalDistance());
//        telemetry.addLine("=== SHOOTER ===");
//        telemetry.addData("Shooter vel",    r.shooter.getShooterVelocity());
//        telemetry.addData("Shooter RPM",    r.shooter.getShooterRPM());
//        telemetry.addData("Target vel",     r.shooter.getShooterGoal());
//        telemetry.addData("Best vel (LUT)", r.shooter.velPos);
//        telemetry.addData("Best hood (LUT)",r.shooter.hoodPose);
//        telemetry.addData("Spun up",        r.shooter.shooterIsSpunUp());
//        telemetry.addLine("=== INTAKE ===");
//        telemetry.addData("Ball 1",         Globals.ballColors[0]);
//        telemetry.addData("Ball 2",         Globals.ballColors[1]);
//        telemetry.addData("Ball 3",         Globals.ballColors[2]);
//        telemetry.addData("Three detected", r.spinner.threeBallsDetected());
//        telemetry.update();
//
//        r.noOuttakeLoop(r);
//    }
//}