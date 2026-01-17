package org.firstinspires.ftc.teamcode.opmode.auto.red.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class R9ClosePaths {
    private final Follower f;

    public final Pose startPos = Globals.RED_CUBE_START;
    public final Pose shoot0Pos = new Pose(76, 86, Math.toRadians(0));
    public final Pose intakeMidOG = new Pose(90, 62, Math.toRadians(0));
    public final Pose intakeMidPos = new Pose(118, 62, Math.toRadians(0));
    public final Pose shoot1HoldPos = new Pose(105, 65);
    public final Pose shoot1Pos = new Pose(76, 85, Math.toRadians(0));
    public final Pose intakeFarPos = new Pose(114, 87, Math.toRadians(0));
    public final Pose shoot2Pos = new Pose(76, 86, Math.toRadians(0));
    public final Pose parkPos = new Pose(121, 74, Math.toRadians(-90));


    private int index;

    public R9ClosePaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0() { //score preloads + get to intake position and comes back
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPos,
                                shoot0Pos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shoot0Pos.getHeading())
                .build();
    }

    public PathChain interMediate() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot0Pos,
                                intakeMidOG
                        )
                ).setLinearHeadingInterpolation(shoot0Pos.getHeading(), intakeMidOG.getHeading())
                .addPath(
                        new BezierLine(
                                intakeMidOG,
                                intakeMidPos
                        )
                ).setLinearHeadingInterpolation(intakeMidOG.getHeading(), intakeMidPos.getHeading())
                .addParametricCallback(0.7, () -> f.setMaxPower(0.5))

                .addPath(
                        new BezierCurve(
                                intakeMidPos,
                                shoot1HoldPos,
                                shoot1Pos
                        )
                )
                .setLinearHeadingInterpolation(intakeMidPos.getHeading(), shoot1Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(0.6))
                .addParametricCallback(0.3, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeFar() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot1Pos,
                                intakeFarPos
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), intakeFarPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.3, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeFarPos,
                                shoot2Pos
                        )
                ).setLinearHeadingInterpolation(intakeFarPos.getHeading(), shoot2Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeClose() { //score the last ramp balls
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot2Pos,
                                parkPos
                        )
                ).setLinearHeadingInterpolation(shoot2Pos.getHeading(), parkPos.getHeading())
                .build();
    }



    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return interMediate();
            case 2: return intakeFar();
            case 3: return intakeClose();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}