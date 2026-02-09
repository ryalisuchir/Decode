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
//import org.firstinspires.ftc.teamcode.common.utility.Globals;
//import org.firstinspires.ftc.teamcode.common.utility.Robot;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.BlueClosePath12;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.BlueClosePath15;
//
//@Autonomous
//public class BClose12 extends OpMode {
//    Robot r;
//    BlueClosePath12 p;
//    boolean read = false;
//
//    @Override
//    public void init() {
//        CommandScheduler.getInstance().reset();
//        r = new Robot(hardwareMap, Globals.BLUE_CUBE_START, Globals.Side.BLUE, true);
//        p = new BlueClosePath12(r);
//        r.shooter.setCustomDistance(p.shootRegularPos.getX(), p.shootRegularPos.getY());
//        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//
//    }
//
//    public void init_loop() {
//        telemetry.addLine("Created all subsystems.");
//        telemetry.addData("Initialized:", "12 Ball Auto (Blue)");
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
//                                new InstantCommand(() -> {
//                                    Globals.turretState = Globals.TurretState.BLUE_CLOSE_GOAL;
//                                    r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                }),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(2600),
//                                        new NoCorrectKickACmd(r)
//                                )
//                        ),
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterAndReadCmd(r, true, 5, Globals.Side.BLUE)),
//                                new FollowPathCmd(r, p.next()),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(1300),
//                                        new InstantCommand(() -> {
//                                            Globals.turretState = Globals.TurretState.BLUE_CLOSE_GOAL;
//                                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                                        })
//                                )
//                        ),
//                        new WaitCommand(800),
//                        new NoCorrectKickACmd(r),
//                        new WaitCommand(200),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new ResetShooterCmd(r, true, 3.5)
//                        ),
//                        new InstantCommand(() -> {
//                            Globals.turretState = Globals.TurretState.BLUE_CLOSE_GOAL;
//                            r.shooter.setCustomDistance(p.shootRegularPos.getX()-5, p.shootRegularPos.getY()+5);
//                        }),
//                        new WaitCommand(800),
//                        new NoCorrectKickACmd(r),
//                        new WaitCommand(200),
//                        new InstantCommand(() -> r.shooter.setCustomDistance(p.lastShootPos.getX(), p.lastShootPos.getY())),
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()),
//                                new SequentialCommandGroup(
//                                        new ResetShooterCmd(r, true, 6.5),
//                                        new InstantCommand(() -> Globals.turretState = Globals.TurretState.BLUE_CLOSE_DIFF_GOAL)
//                                )
//                        ),
//                        new WaitCommand(800),
//                        new NoCorrectKickACmd(r),
//                        new WaitCommand(200)
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