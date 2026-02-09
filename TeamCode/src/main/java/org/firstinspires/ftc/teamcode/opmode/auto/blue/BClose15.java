//package org.firstinspires.ftc.teamcode.opmode.auto.blue;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.seattlesolvers.solverslib.command.CommandScheduler;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//import com.seattlesolvers.solverslib.command.WaitUntilCommand;
//
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.NoCorrectKickACmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
//import org.firstinspires.ftc.teamcode.common.utility.G;
//import org.firstinspires.ftc.teamcode.common.utility.Halo;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.BlueClosePath15;
//
//@Autonomous
//public class BClose15 extends OpMode {
//    Halo r;
//    BlueClosePath15 p;
//    boolean read = false;
//
//    @Override
//    public void init() {
//        CommandScheduler.getInstance().reset();
//        r = new Halo(hardwareMap, G.BLUE_CUBE_START, G.Side.BLUE, true);
//        p = new BlueClosePath15(r);
//        r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY());
//        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//    }
//
//    public void init_loop() {
//        telemetry.addLine("Created all subsystems.");
//        telemetry.addData("Initialized:", "15 Ball Auto (Blue)");
//        telemetry.addData("Obelisk Reading:", G.obeliskOptions);
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
//                                new InstantCommand(() -> {
//                                    G.turretState = G.TurretState.BLUE_CLOSE_GOAL;
//                                    r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                }),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(2100),
//                                        new NoCorrectKickACmd(r)
//                                )
//                        ),
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterAndReadCmd(r, true, 5, G.Side.BLUE)),
//                                new FollowPathCmd(r, p.next()),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(1300),
//                                        new InstantCommand(() -> {
//                                            G.turretState = G.TurretState.BLUE_CLOSE_GOAL;
//                                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                        })
//                                )
//                        ),
//                        new NoCorrectKickACmd(r), //finished mid, start gate sequence
//                        new ParallelCommandGroup(
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, true, 6)),
//                                        new InstantCommand(() -> {
//                                            G.turretState = G.TurretState.BLUE_CLOSE_GOAL;
//                                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                        })
//                                ),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 2000)
//                        ),
//                        new ParallelRaceGroup(
//                                new WaitUntilCommand(() -> r.spinner.threeBallsDetected()),
//                                new WaitCommand(100)
//                        ),
//                        new FollowPathCmd(r, p.next()),
//                        new NoCorrectKickACmd(r),
//                        new ParallelCommandGroup(
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, true, 6)),
//                                        new InstantCommand(() -> {
//                                            G.turretState = G.TurretState.BLUE_CLOSE_GOAL;
//                                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                        })
//                                ),
//                                new FollowPathCmd(r, p.next())
//                        ),
//                        new ParallelRaceGroup(
//                                new WaitUntilCommand(() -> r.spinner.threeBallsDetected()),
//                                new WaitCommand(100)
//                        ),
//                        new FollowPathCmd(r, p.next()),
//                        new NoCorrectKickACmd(r),
//                        //finished gate sequences, intake far
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new ResetShooterCmd(r, true, 3.5)
//                        ),
//                        new InstantCommand(() -> {
//                            G.turretState = G.TurretState.BLUE_CLOSE_GOAL;
//                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                        }),
//                        new NoCorrectKickACmd(r),
//                        new InstantCommand(() -> r.shooter.setCustomDistance(p.lastShootPos.getX(), p.lastShootPos.getY())),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new SequentialCommandGroup(
//                                        new ResetShooterCmd(r, true, 6),
//                                        new InstantCommand(() -> G.turretState = G.TurretState.BLUE_CLOSE_DIFF_GOAL)
//                                )
//                        ),
//                        new NoCorrectKickACmd(r)
//                )
//        );
//    }
//
//    @Override
//    public void loop() {
//        if (G.obeliskOptions != G.ObeliskOptions.NOT_FOUND) read = true;
//
//        if (read)  {
//            r.noVisionLoop(r);
//        } else {
//            r.loop(r);
//        }
//
//        telemetry.addData("Obelisk Reading:", G.obeliskOptions);
//    }
//
//    @Override
//    public void stop() {
//        r.stop();
//    }
//}