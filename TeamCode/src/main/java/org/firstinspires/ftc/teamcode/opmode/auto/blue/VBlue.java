//package org.firstinspires.ftc.teamcode.opmode.auto.blue;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.seattlesolvers.solverslib.command.CommandScheduler;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.FarInitCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
//import org.firstinspires.ftc.teamcode.common.utility.Globals;
//import org.firstinspires.ftc.teamcode.common.utility.Robot;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.VergeRedPaths;
//
//@Autonomous
//public class VBlue extends OpMode {
//    Robot r;
//    VergeRedPaths p;
//    boolean read = false;
//
//    @Override
//    public void init() {
//        CommandScheduler.getInstance().reset();
//        r = new Robot(hardwareMap, Globals.BLUE_FAR_START, Globals.Side.BLUE, true);
//        p = new VergeRedPaths(r);
//        r.shooter.setCustomDistance(p.shoot0Pos.getX()+8, p.shoot0Pos.getY()-8);
//        CommandScheduler.getInstance().schedule(new FarInitCmd(r, Globals.Side.BLUE));
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//    }
//
//    public void init_loop() {
//        telemetry.addLine("Created all subsystems.");
//        telemetry.addData("Initialized:", "Verge Allianced Auto (Blue)");
//        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
//        r.initLoop(r);
//        CommandScheduler.getInstance().run();
//        telemetry.update();
//    }
//
//    public void start() {
//        CommandScheduler.getInstance().schedule(
//                new SequentialCommandGroup(
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(2200),
//                                        new KickOrderACmd(r)
//                                )
//                        ),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new ResetShooterCmd(r, true, 4)
//                        ),
//                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL),
//                        new WaitCommand(900),
//                        new KickOrderACmd(r),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.02, 3000),
//                                new ResetShooterCmd(r, true, 4)
//                        ),
//                        new FollowPathCmd(r, p.next()), //intake hp 1 shoot
//                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL),
//                        new WaitCommand(900),
//                        new KickOrderACmd(r),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.02, 3000),
//                                new ResetShooterCmd(r, true, 4)
//                        ),
//                        new FollowPathCmd(r, p.next()), //intake hp 1 shoot
//                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL),
//                        new WaitCommand(900),
//                        new KickOrderACmd(r),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 3000),
//                                new ResetShooterCmd(r, true, 4)
//                        ),
//                        new FollowPathCmd(r, p.next()), //intake hp 3 shoot
//                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_FAR_GOAL),
//                        new WaitCommand(900),
//                        new KickOrderACmd(r),
//                        new WaitCommand(200),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new ResetShooterCmd(r, false, 0)
//                        )
//                )
//        );
//    }
//
//    @Override
//    public void loop() {
//        if (Globals.obeliskOptions != Globals.ObeliskOptions.NOT_FOUND) read = true;
//
//        if (read)  {
//            r.noVisionLoop(r);
//        } else {
//            r.loop(r);
//        }
//
//        telemetry.addData("Obelisk Reading:", Globals.obeliskOptions);
//    }
//
//    @Override
//    public void stop() {
//        r.stop();
//    }
//}