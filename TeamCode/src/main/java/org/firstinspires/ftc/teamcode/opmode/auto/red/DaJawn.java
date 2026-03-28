package org.firstinspires.ftc.teamcode.opmode.auto.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidSlowerCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.InitExodus;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.DaJawnPaths;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath18;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.SideSpikePaths;

@Autonomous(preselectTeleOp = "Red")
public class DaJawn extends OpMode {
    Halo r;
    DaJawnPaths p;

    boolean yo = false;
    boolean yo2 = false;
    boolean yo3 = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, new Pose(128.5, 113, Math.toRadians(-90)), G.Side.RED, true);
        p = new DaJawnPaths(r);
        CommandScheduler.getInstance().schedule(new InitExodus(r));
        telemetry = new PeacockTelemetry(this);

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "24?? Ball Auto (Red)");
        telemetry.addData("Obelisk Reading:", G.obeliskOptions);
        r.initLoop(r);
        CommandScheduler.getInstance().run();
        telemetry.update();
    }

    public void start() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                r.turret.redCloseClose(),
                                new DeferredCommand(() -> new InstantCommand(() -> G.setShooterState = G.SetShooterState.very_very_close)),
                                r.spinner.transfer(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new WaitCommand(500),
                        new RapidSlowerCmd(r),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redCloseClose())),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidAllCmd(r),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 4, r.turret.red18Pos2(), new InstantCommand(() -> yo = true))),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidAllCmd(r),
                        new DeferredCommand(() -> new InstantCommand(() -> {
                            G.setShooterState = G.SetShooterState.reg_18;
                        })),
                        //this is gate sequence 1:
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3.5, r.turret.red18Pos2(), new InstantCommand(() -> G.setShooterState = G.SetShooterState.reg_18))),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidAllCmd(r),
                        //end of gate sequence 1 ^^
                        //this is gate sequence 2:
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3.5, r.turret.red18Pos2())),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidAllCmd(r),
                        new InstantCommand(() -> yo3 = true),
                        //end of gate sequence 2 ^^
                        new ParallelCommandGroup(
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()), //intakes spike closest to obelisk
                                new DeferredCommand(() ->
                                        new ResetShooterCmd(r, 3, r.turret.redFarNot(),
                                        new InstantCommand(() -> G.setShooterState = G.SetShooterState.far),
                                        new InstantCommand(() -> yo = false),
                                        new InstantCommand(() -> yo3 = true
                                )))),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidAllCmd(r),
                        //skib far
                        new ParallelCommandGroup(
                                new DeferredCommand(() ->
                                        new ResetShooterCmd(r, 3, r.turret.redFar(),
                                                new InstantCommand(() -> G.setShooterState = G.SetShooterState.far),
                                                new InstantCommand(() -> yo = false),
                                                new InstantCommand(() -> yo2 = true
                                                ))),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600)
                        ),
                        new RapidAllCmd(r),
                        //end of skib far ^^
                        //skib far
                        new ParallelCommandGroup(
                                new DeferredCommand(() ->
                                        new ResetShooterCmd(r, 3, r.turret.redFar(),
                                                new InstantCommand(() -> G.setShooterState = G.SetShooterState.far),
                                                new InstantCommand(() -> yo = false),
                                                new InstantCommand(() -> yo2 = true
                                                ))),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600)
                        ),
                        new RapidAllCmd(r),
                        //end of skib far ^^
                        new FollowPathCmd(r, p.next())
                ));
    }

    @Override
    public void loop() {
        telemetry.addData("1: ", G.ballColors[0]);
        telemetry.addData("2: ", G.ballColors[1]);
        telemetry.addData("3: ", G.ballColors[2]);
        telemetry.addData("Intake state: ", G.intakeState);
        telemetry.update();


        if (yo) {
            r.r.setPosition(0.82);
            r.setShooterClass.optimalVelocity = 1630;
            G.setShooterState = G.SetShooterState.reg_18;
        }

        if (yo2) {
            r.r.setPosition(0.845);
            r.setShooterClass.optimalVelocity = 2150;
            G.setShooterState = G.SetShooterState.far;
            r.turret.setPositionOnce(0.71);
        }

        if (yo3) {
            r.r.setPosition(0.845);
            r.setShooterClass.optimalVelocity = 2150;
            G.setShooterState = G.SetShooterState.far;
            r.turret.setPositionOnce(0.62);
        }

        r.failsafeAutoLoop();
    }

    @Override
    public void stop() {
        r.stop();
    }
}
