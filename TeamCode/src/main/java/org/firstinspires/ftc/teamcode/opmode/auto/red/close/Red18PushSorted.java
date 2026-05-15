//package org.firstinspires.ftc.teamcode.opmode.auto.red.close;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.seattlesolvers.solverslib.command.CommandScheduler;
//import com.seattlesolvers.solverslib.command.DeferredCommand;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//
//import org.firstinspires.ftc.teamcode.common.Globals;
//import org.firstinspires.ftc.teamcode.common.Halo;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.EnsuredOrderCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
//import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.ResetShooterCmd;
//import org.firstinspires.ftc.teamcode.common.utility.PeacockTelemetry;
//import org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close.RedPush18SortedPathing;
//
//import java.util.Collections;
//
//@Autonomous
//public class Red18PushSorted extends OpMode {
//    Halo r;
//    RedPush18SortedPathing p;
//
//    @Override
//    public void init() {
//        CommandScheduler.getInstance().reset();
//        r = new Halo(hardwareMap, Globals.Positions.PUSH_AUTO_RED, Globals.Alliance.RED, Globals.Match.AUTO);
//        p = new RedPush18SortedPathing(r);
//        CommandScheduler.getInstance().schedule(
//                new SequentialCommandGroup(
//                        new CloseInitCmd(r)
//                )
//        );
//        telemetry = new PeacockTelemetry(this);
//
//    }
//
//    public void init_loop() {
//        telemetry.addLine("Created all subsystems.");
//        telemetry.addData("Initialized:", "18 Ball Auto (Red). Pushes other alliance. Sorted.");
//        r.initLoop(r);
//        CommandScheduler.getInstance().run();
//        telemetry.update();
//    }
//
//    public void start() {
//        CommandScheduler.getInstance().schedule(
//                new SequentialCommandGroup(
//                        new ParallelCommandGroup(
//                                r.turret.redPushShoot0(),
//                                r.spinner.transfer(),
//                                new FollowPathCmd(r, p.next()) //score preload path
//                        ),
//                        new RapidAllCmd(r), //score preloads
//                        new ParallelCommandGroup(
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.followObeliskCmd()), Collections.emptyList()),
//                                        r.turret.redSorted2()),
//                                r.spinner.intake(),
//                                new FollowPathCmd(r, p.next()) //intake middle and get back to shoot pos
//                        ),
//                        new RapidAllCmd(r), //rapid middle
//                        new WaitCommand(100),
//                        //this is gate sequence 1:
//                        new ParallelCommandGroup(
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redSortedGate()), Collections.emptyList()),
//                                        r.turret.redSortedGate()),
//                                r.spinner.intake(),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
//                        ),
//                        new FollowPathCmd(r, p.next()), //gate intake's shooting
//                        new RapidAllCmd(r),
//                        new WaitCommand(100),
//                        //end of gate sequence 1 ^^
//                        //this is gate sequence 2:
//                        new ParallelCommandGroup(
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redSortedGate()), Collections.emptyList()),
//                                        r.turret.redSortedGate()),
//                                r.spinner.intake(),
//                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
//                        ),
//                        new FollowPathCmd(r, p.next()), //gate intake's shooting
//                        new EnsuredOrderCmd(r), //slow down shooting for first 3
//                        new WaitCommand(100),
//                        //end of gate sequence 2 ^^
//                        new ParallelCommandGroup(
//                                r.spinner.intake(),
//                                new FollowPathCmd(r, p.next()), //intake spike 3
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, 3,  r.turret.redSortedFar()), Collections.emptyList()),
//                                        r.turret.redSortedFar()
//                                )
//                        ),
//                        new InstantCommand(() -> r.spinner.transferStart()),
//                        new EnsuredOrderCmd(r), //slow down shooting for second 3
//                        new WaitCommand(100),
//                        //Close spike?
//                        new ParallelCommandGroup(
//                                r.spinner.intake(),
//                                new FollowPathCmd(r, p.next()), //intake spike 1
//                                new SequentialCommandGroup(
//                                        new DeferredCommand(() -> new ResetShooterCmd(r, 1.5,  r.turret.redSorted1()), Collections.emptyList()),
//                                        r.turret.redSorted1()
//                                )
//                        ),
//                        new InstantCommand(() -> r.spinner.transferStart()),
//                        new EnsuredOrderCmd(r), //slow down shooting for last 3
//                        new FollowPathCmd(r, p.next()) //park
//                ));
//    }
//
//    @Override
//    public void loop() {
//        telemetry.update();
//        telemetry.addData("Current Position: ", r.dt.getPose());
//        r.loop(r);
//    }
//
//    @Override
//    public void stop() {
//        r.stop();
//    }
//}
