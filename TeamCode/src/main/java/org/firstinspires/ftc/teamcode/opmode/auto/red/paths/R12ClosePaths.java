package org.firstinspires.ftc.teamcode.opmode.auto.red.paths;

import android.util.Log;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class R12ClosePaths {
    private final Follower f;

    public Pose start = new Pose(134, 138, Math.toRadians(-90));
    public Pose shoot0 = new Pose(102, 102, Math.toRadians(0));
    public Pose intakeMidHold1 = new Pose(100, 65);
    public Pose intakeMid = new Pose(147, 69, Math.toRadians(0));
    public Pose shoot1Hold = new Pose(116, 73);
    public Pose shoot1 = new Pose(102, 102, Math.toRadians(0));
    public Pose intakeRight = new Pose(139, 95, Math.toRadians(0));
    public Pose shoot2 = new Pose(102, 102, Math.toRadians(0));
    public Pose intakeCloseHold = new Pose(100, 42);
    public Pose intakeClose = new Pose(140, 45, Math.toRadians(0));
    public Pose shoot3 = new Pose(102, 102, Math.toRadians(0));
    public Pose park = new Pose(138, 79, Math.toRadians(-90));

    private int index;

    public R12ClosePaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0intake0() { //score preloads + get to intake position
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                start,
                                shoot0
                        )
                )
                .setLinearHeadingInterpolation(start.getHeading(), shoot0.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.3, () -> f.setMaxPower(0.4))
                .addParametricCallback(0.6, () -> f.setMaxPower(1))
                .addPath(
                        new BezierCurve(
                                shoot0,
                                intakeMidHold1,
                                intakeMid
                        )
                )
                .addParametricCallback(0.5, () -> f.setMaxPower(0.3))
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeMid.getHeading())
                .build();
    }

    public PathChain score1() { //score mid spike
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(intakeMid, shoot1Hold, shoot1)
                )
                .setLinearHeadingInterpolation(intakeMid.getHeading(), shoot1.getHeading())
                .build();
    }

    public PathChain intake1() { //intake from ramp go 1
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot1,
                                intakeRight
                        )
                )
                .addParametricCallback(0, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeRight.getHeading())
                .build();
    }

    public PathChain score2() { //score the balls from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeRight, shoot2)
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeRight.getHeading(), shoot2.getHeading())
                .build();
    }

    public PathChain intake2() { //second go at intaking from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot2,
                                intakeCloseHold,
                                intakeClose
                        )
                )
                .addParametricCallback(0.7, () -> f.setMaxPower(0.45))
                .setLinearHeadingInterpolation(shoot2.getHeading(), intakeClose.getHeading())
                .build();
    }

    public PathChain score3() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeClose, shoot3)
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeClose.getHeading(), shoot3.getHeading())
                .build();
    }

    public PathChain parkX() { //scores the last spike mark
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot3,
                                park
                        )
                )
                .setLinearHeadingInterpolation(shoot3.getHeading(), park.getHeading())
                .build();
    }

    public PathChain next() {
        switch (index++) {
            case 0: return score0intake0();
            case 1: return score1();
            case 2: return intake1();
            case 3: return score2();
            case 4: return intake2();
            case 5: return score3();
            case 6: return parkX();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}
