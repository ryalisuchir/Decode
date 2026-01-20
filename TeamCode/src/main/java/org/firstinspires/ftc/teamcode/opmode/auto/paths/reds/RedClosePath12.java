package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class RedClosePath12 {
    private final Follower f;

    public Pose startPos, shootRegularPos, intakeMidInitialPos, intakeMidHoldPos, intakeMidPos, shootHoldPos, intakeFarPos, intakeCloseHold1Pos, intakeCloseHold2Pos, intakeClosePos, lastShootPos;
    private int index;

    public RedClosePath12(Robot r) {
        this.f = r.dt.getFollower();
        startPos = m(Globals.BLUE_CUBE_START);
        shootRegularPos = m(new Pose(50.213, 79.055, Math.toRadians(180)));
        intakeMidInitialPos = m(new Pose(42.750720461095106, 57, Math.toRadians(170)));
        intakeMidHoldPos = m(new Pose(32.989913544668596, 53));
        intakeMidPos = m(new Pose(12, 58.069, Math.toRadians(180)));
        shootHoldPos = m(new Pose(44.70592363112392, 66.82712968299712));
        intakeFarPos = m(new Pose(21, 81, Math.toRadians(180)));
        intakeCloseHold1Pos = m(new Pose(66.73054755043228, 37.583573487031686));
        intakeCloseHold2Pos = m(new Pose(52.96109510086454, 33.22190201729104));
        intakeClosePos = m(new Pose(13.65994236311239, 32.61110086455331, Math.toRadians(180)));
        lastShootPos = m(new Pose(57.46974063400577, 113.66570605187317));

        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPos,
                                shootRegularPos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shootRegularPos.getHeading())
                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootRegularPos,
                                intakeMidInitialPos
                        )
                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), intakeMidInitialPos.getHeading())
                .addPath(
                        new BezierCurve(
                                intakeMidInitialPos,
                                intakeMidHoldPos,
                                intakeMidPos
                        )
                ).setLinearHeadingInterpolation(intakeMidInitialPos.getHeading(), intakeMidPos.getHeading())
                .addParametricCallback(0.7, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierCurve(
                                intakeMidPos,
                                shootHoldPos,
                                shootRegularPos
                        )
                ).setLinearHeadingInterpolation(intakeMidPos.getHeading(), shootRegularPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeFarSequence() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootRegularPos,
                                intakeFarPos
                        )
                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), intakeFarPos.getHeading())
                .addParametricCallback(0.5, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeFarPos,
                                shootRegularPos
                        )
                ).setLinearHeadingInterpolation(intakeFarPos.getHeading(), shootRegularPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shootRegularPos,
                                intakeCloseHold1Pos,
                                intakeCloseHold2Pos,
                                intakeClosePos
                        )
                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), intakeClosePos.getHeading())
                .addParametricCallback(0.9, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeClosePos,
                                lastShootPos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.7, () -> f.setMaxPower(0.5))
                .setReversed()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeMidAndShoot();
            case 2: return intakeFarSequence();
            case 3: return intakeCloseAndShoot();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose m(Pose p) {
        return Globals.side == Globals.Side.BLUE ? p : p.mirror();
    }
}