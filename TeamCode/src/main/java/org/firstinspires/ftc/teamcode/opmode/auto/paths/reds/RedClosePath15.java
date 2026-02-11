package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class RedClosePath15 {
    private final Follower f;

    public Pose startPos, shootRegularPos, intakeMidInitialPos, intakeMidHoldPos, intakeMidPos, shootHoldPos, slamGateHoldPos, slamGatePos, gateIntakeWraparoundHoldPos, gateIntakeWraparoundPos, gateIntakeWraparoundForwardPos, shootGateBallsHoldPos, intakeFarPos, intakeCloseHold1Pos, intakeCloseHold2Pos, intakeClosePos, lastShootPos;
    private int index;

    public RedClosePath15(Halo r) {
        this.f = r.dt.getFollower();
        startPos = m(G.BLUE_CUBE_START);
        shootRegularPos = m(new Pose(56, 81, Math.toRadians(180)));
        intakeMidInitialPos = m(new Pose(42.750720461095106, 57, Math.toRadians(170)));
        intakeMidHoldPos = m(new Pose(32.989913544668596, 53));
        intakeMidPos = m(new Pose(13, 58.069, Math.toRadians(180)));

        shootHoldPos = m(new Pose(44.70592363112392, 66.82712968299712));
        slamGateHoldPos = m(new Pose(33, 54, Math.toRadians(180)));
        slamGatePos = m(new Pose(12, 60, Math.toRadians(147)));

        gateIntakeWraparoundHoldPos = m(new Pose(14.5, 45));
        gateIntakeWraparoundPos = m(new Pose(14.5, 47, Math.toRadians(112)));
        gateIntakeWraparoundForwardPos = m(new Pose(14.5, 53, Math.toRadians(105)));

        shootGateBallsHoldPos = m(new Pose(40.13976945244957, 60.02737752161382));

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
                .addPath(
                        new BezierCurve(
                                intakeMidPos,
                                shootHoldPos,
                                shootRegularPos
                        )
                ).setLinearHeadingInterpolation(intakeMidPos.getHeading(), shootRegularPos.getHeading())
                .build();
    }

    public PathChain gateSequence() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shootRegularPos,
                                slamGateHoldPos,
                                slamGatePos
                        )
                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), slamGatePos.getHeading())
                .addPath(
                        new BezierCurve(
                                slamGatePos,
                                gateIntakeWraparoundHoldPos,
                                gateIntakeWraparoundPos
                        )
                ).setLinearHeadingInterpolation(slamGatePos.getHeading(), gateIntakeWraparoundPos.getHeading())
//                .addPath(
//                        new BezierLine(
//                                gateIntakeWraparoundPos,
//                                gateIntakeWraparoundForwardPos
//                        )
//                ).setLinearHeadingInterpolation(gateIntakeWraparoundPos.getHeading(), gateIntakeWraparoundForwardPos.getHeading())
//                .addParametricCallback(0, () -> f.setMaxPower(0.5))
                .build();
    }

    public PathChain shootGateSequence() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                gateIntakeWraparoundForwardPos,
                                shootHoldPos,
                                shootRegularPos
                        )
                ).setLinearHeadingInterpolation(gateIntakeWraparoundForwardPos.getHeading(), shootRegularPos.getHeading())
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
                .addPath(
                        new BezierLine(
                                intakeClosePos,
                                lastShootPos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeMidAndShoot();
            case 2: return gateSequence();
            case 3: return shootGateSequence();
//            case 4: return gateSequence();
//            case 5: return shootGateSequence();
            case 4: return intakeFarSequence();
            case 5: return intakeCloseAndShoot();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose m(Pose p) {
        return G.side == G.Side.BLUE ? p : p.mirror();
    }
}