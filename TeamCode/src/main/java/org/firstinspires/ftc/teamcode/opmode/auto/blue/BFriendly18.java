//package org.firstinspires.ftc.teamcode.opmode.auto.blue;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.seattlesolvers.solverslib.command.CommandScheduler;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterAndReadCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
//import org.firstinspires.ftc.teamcode.common.utility.G;
//import org.firstinspires.ftc.teamcode.common.utility.Halo;
//import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.blues.FriendlyBlueClosePath18;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.FriendlyRedClosePath18;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath18;
//
//@Autonomous
//public class BFriendly18 extends OpMode {
//    Halo r;
//    FriendlyBlueClosePath18 p;
//
//    @Override
//    public void init() {
//        CommandScheduler.getInstance().reset();
//        r = new Halo(hardwareMap, G.BLUE_CUBE_START, G.Side.BLUE, true);
//        r.init();
//        p = new FriendlyBlueClosePath18(r);
//        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
//        telemetry = new PeacockTelemetry(this);
//
//    }
//
//    public void init_loop() {
//        telemetry.addLine("Created all subsystems.");
//        telemetry.addData("Initialized:", "18 Friendly Ball Auto (Blue)");
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
//                                r.turret.blue18Pos1(),
//                                r.spinner.transfer(),
//                                new FollowPathCmd(r, p.next()), //shoot preloads
//                                new SequentialCommandGroup(
//                                        new WaitCommand(2000),
//                                        new RapidSlowerCmd(r)
//                                )
//                        ),
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.blue18Pos2())),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new FollowPathCmd(r, p.next()), //intake mid and go to shoot
//                                new SequentialCommandGroup(
//                                        new WaitCommand(900),
//                                        new InstantCommand(() -> G.turretState = G.TurretState.FOLLOWING)
//                                )
//                        ),
//                        new RapidSlowerCmd(r),
//                        //this is gate sequence 1:
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterCmd(r, 3.5, r.turret.blue18Pos2())),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1300) //this is gate intake
//                        ),
//                        new FollowPathCmd(r, p.next()), //gate intake's shooting
//                        new RapidSlowerCmd(r),
//                        //end of gate sequence 1 ^^
//                        //this is gate sequence 2:
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterCmd(r, 5, r.turret.blue18Pos2())),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1300) //this is gate intake
//                        ),
//                        new FollowPathCmd(r, p.next()), //gate intake's shooting
//                        new RapidSlowerCmd(r),
//                        //end of gate sequence 2 ^^
//                        new ParallelCommandGroup(
//                                new FollowPathCmd(r, p.next()), //intakes spike closest to audience
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new DeferredCommand(() -> new ResetShooterCmd(r, 2.5, r.turret.blue18Pos2()))
//
//                        ),
//                        new ParallelCommandGroup(
//                                new InstantCommand(() -> r.t.setPower(-1)),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new RapidSlowerCmd(r)
//                        ),
//                        new WaitCommand(800),
//                        new ParallelCommandGroup(
//                                new DeferredCommand(() -> new ResetShooterCmd(r, 5, r.turret.blue18Pos2())),
//                                new InstantCommand(() -> r.i.setPower(1)),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 1300) //this is gate intake
//                        ),
//                        new FollowPathCmd(r, p.next()), //gate intake's shooting
//                        new RapidSlowerCmd(r),
//                        new FollowPathCmd(r, p.next()) //park
//                ));
//    }
//
//    @Override
//    public void loop() {
//        telemetry.addData("1: ", G.ballColors[0]);
//        telemetry.addData("2: ", G.ballColors[1]);
//        telemetry.addData("3: ", G.ballColors[2]);
//        telemetry.update();
//        r.unsortedLoop();
//    }
//
//    @Override
//    public void stop() {
//        r.stop();
//    }
//}
