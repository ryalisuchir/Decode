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
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.FollowPathCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.inits.CloseInitCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.ResetShooterCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.DeferredCommand;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.telemetry.PeacockTelemetry;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.RedClosePath18;
import org.firstinspires.ftc.teamcode.opmode.auto.paths.reds.SideSpikePaths;

@Autonomous(preselectTeleOp = "Red")
public class SideSpike extends OpMode {
    Halo r;
    SideSpikePaths p;

    boolean yo;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Halo(hardwareMap, G.RED_CUBE_START, G.Side.RED, true);
        p = new SideSpikePaths(r);
        CommandScheduler.getInstance().schedule(new CloseInitCmd(r));
        telemetry = new PeacockTelemetry(this);

    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        telemetry.addData("Initialized:", "18 Ball Auto (Red)");
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
                        new WaitCommand(900),
                        new RapidSlowerCmd(r),
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.redCloseClose())),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next())
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidSlowerCmd(r),
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
                        //end of gate sequence 2 ^^
                        new ParallelCommandGroup(
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()), //intakes spike closest to obelisk
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3,  r.turret.red18Pos2()))
                        ),
                        new InstantCommand(() -> r.spinner.transferStart()),
                        new RapidAllCmd(r),
                        //this is gate sequence 3:
                        new ParallelCommandGroup(
                                new DeferredCommand(() -> new ResetShooterCmd(r, 3, r.turret.red18Pos2())),
                                r.spinner.intake(),
                                new FollowPathCmd(r, p.next()).withStallTimeout(0.04, 600) //this is gate intake
                        ),
                        new FollowPathCmd(r, p.next()), //gate intake's shooting
                        new RapidAllCmd(r),
                        //end of gate sequence 3 ^^
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

        r.failsafeAutoLoop();
    }

    @Override
    public void stop() {
        r.stop();
    }
}
