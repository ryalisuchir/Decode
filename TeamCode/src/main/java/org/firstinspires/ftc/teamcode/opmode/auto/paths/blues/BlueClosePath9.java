//package org.firstinspires.ftc.teamcode.opmode.auto.paths.blues;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierCurve;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.paths.PathChain;
//
//import org.firstinspires.ftc.teamcode.common.utility.Globals;
//import org.firstinspires.ftc.teamcode.common.utility.Robot;
//
//public class BlueClosePath9 {
//    private final Follower f;
//
//    public Pose startPos, shootRegularPos, intakeMidInitialPos, intakeMidHoldPos, intakeMidPos, shootHoldPos, intakeFarPos, parkPos;
//    private int index;
//
//    public BlueClosePath9(Robot r) {
//        this.f = r.dt.getFollower();
//        startPos = m(Globals.BLUE_CUBE_START);
//        shootRegularPos = m(new Pose(50.213, 79.055, Math.toRadians(180)));
//        intakeMidInitialPos = m(new Pose(42.750720461095106, 57, Math.toRadians(170)));
//        intakeMidHoldPos = m(new Pose(32.989913544668596, 53));
//        intakeMidPos = m(new Pose(14, 58.069, Math.toRadians(180)));
//        shootHoldPos = m(new Pose(44.70592363112392, 66.82712968299712));
//        intakeFarPos = m(new Pose(21, 79.055, Math.toRadians(180)));
//        parkPos = m(new Pose(50.04034582132565, 120.72910662824208, Math.toRadians(180)));
//
//        index = 0;
//    }
//
//    public PathChain score0() {
//        return f.pathBuilder()
//                .addPath(
//                        new BezierLine(
//                                startPos,
//                                shootRegularPos
//                        )
//                ).setLinearHeadingInterpolation(startPos.getHeading(), shootRegularPos.getHeading())
//                .build();
//    }
//
//    public PathChain intakeMidAndShoot() {
//        return f.pathBuilder()
//                .addPath(
//                        new BezierLine(
//                                shootRegularPos,
//                                intakeMidInitialPos
//                        )
//                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), intakeMidInitialPos.getHeading())
//                .addPath(
//                        new BezierCurve(
//                                intakeMidInitialPos,
//                                intakeMidHoldPos,
//                                intakeMidPos
//                        )
//                ).setLinearHeadingInterpolation(intakeMidInitialPos.getHeading(), intakeMidPos.getHeading())
//                .addParametricCallback(0.7, () -> f.setMaxPower(0.5))
//                .addPath(
//                        new BezierCurve(
//                                intakeMidPos,
//                                shootHoldPos,
//                                shootRegularPos
//                        )
//                ).setLinearHeadingInterpolation(intakeMidPos.getHeading(), shootRegularPos.getHeading())
//                .addParametricCallback(0, () -> f.setMaxPower(1))
//                .build();
//    }
//
//    public PathChain intakeFarSequence() {
//        return f.pathBuilder()
//                .addPath(
//                        new BezierLine(
//                                shootRegularPos,
//                                intakeFarPos
//                        )
//                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), intakeFarPos.getHeading())
//                .addParametricCallback(0.5, () -> f.setMaxPower(0.5))
//                .addPath(
//                        new BezierLine(
//                                intakeFarPos,
//                                shootRegularPos
//                        )
//                ).setLinearHeadingInterpolation(intakeFarPos.getHeading(), shootRegularPos.getHeading())
//                .addParametricCallback(0, () -> f.setMaxPower(1))
//                .build();
//    }
//
//    public PathChain park() {
//        return f.pathBuilder()
//                .addPath(
//                        new BezierLine(
//                                shootRegularPos,
//                                parkPos
//                        )
//                ).setLinearHeadingInterpolation(shootRegularPos.getHeading(), parkPos.getHeading())
//                .addParametricCallback(0, () -> f.setMaxPower(1))
//                .build();
//    }
//
//
//    public PathChain next() {
//        switch (index++) {
//            case 0: return score0();
//            case 1: return intakeMidAndShoot();
//            case 2: return intakeFarSequence();
//            case 3: return park();
//            default: return null;
//        }
//    }
//
//    public void reset() {
//        index = 0;
//    }
//
//    private Pose m(Pose p) {
//        return Globals.side == Globals.Side.BLUE ? p : p.mirror();
//    }
//}