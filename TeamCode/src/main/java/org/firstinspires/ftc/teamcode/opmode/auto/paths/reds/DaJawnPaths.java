package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class DaJawnPaths {
    private final Follower f;
    private final G.Side side;

    public Pose startPos, intakeFarPose, shootClosePose, intakeMidPose, shootGatePos, gateIntakePos, intakeAudHoldPos, intakeAudPos, shootAudPos, intakeSkibidiArea, shootSkibidiArea, initialPark;
    private int index;

    public DaJawnPaths(Halo r) {
        this.f = r.dt.getFollower();
        this.side = G.side;

        startPos = new Pose(128.5, 113, Math.toRadians(-90));

        shootClosePose = alliancePose(new Pose(119.49279538904898, 106.13256484149855, Math.toRadians(-90)));
        intakeFarPose = alliancePose(new Pose(118, 88, Math.toRadians(-80)));
        intakeMidPose = alliancePose(new Pose(118, 64, Math.toRadians(-80)));

        gateIntakePos = alliancePose(new Pose(132.5, 58, Math.toRadians(25)));
        shootGatePos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

        intakeAudHoldPos = alliancePose(new Pose(97, 33));
        intakeAudPos = alliancePose(new Pose(121, 35, Math.toRadians(-44)));
        shootAudPos = alliancePose(new Pose(93.80403458213257, 12.731988472622497, Math.toRadians(0)));

        intakeSkibidiArea = alliancePose(new Pose(131.1844380403458, 9, Math.toRadians(0)));
        shootSkibidiArea = alliancePose(new Pose(93.80403458213257, 12.731988472622497, Math.toRadians(0)));

        initialPark = alliancePose(new Pose(97, 75, Math.toRadians(0)));

        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                                shootClosePose
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shootClosePose.getHeading())

                .build();
    }

    public PathChain intakeFarSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shootClosePose,
                                intakeFarPose
                        )
                ).setLinearHeadingInterpolation(shootClosePose.getHeading(), intakeFarPose.getHeading())
                .addPath(
                        new BezierLine(
                                intakeFarPose,
                                shootClosePose
                        )
                ).setLinearHeadingInterpolation(intakeFarPose.getHeading(), shootClosePose.getHeading())
                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shootClosePose,
                                intakeMidPose
                        )
                ).setLinearHeadingInterpolation(shootClosePose.getHeading(), intakeMidPose.getHeading())
                .addPath(
                        new BezierLine(
                                intakeMidPose,
                                shootGatePos
                        )
                ).setLinearHeadingInterpolation(intakeMidPose.getHeading(), shootGatePos.getHeading())
                .build();
    }

    public PathChain gateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shootGatePos,
                                gateIntakePos
                        )
                ).setLinearHeadingInterpolation(shootGatePos.getHeading(), gateIntakePos.getHeading())
                .build();
    }

    public PathChain shootGateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                gateIntakePos,
                                shootGatePos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain intakeCloseAndShoot() { //3rd spike
        return f.pathBuilder().addPath(
                        new BezierCurve( //intake audience spike
                                shootGatePos,
                                intakeAudHoldPos,
                                intakeAudPos
                        )
                ).setTangentHeadingInterpolation()
                .addPath( //shoot audience spike
                        new BezierLine(
                                intakeAudPos,
                                shootAudPos
                        )
//                ).setLinearHeadingInterpolation(intakeAudPos.getHeading(), shootAudPos.getHeading())
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain intakeSkibAndShoot() { //hp
        return f.pathBuilder().addPath(
                        new BezierLine( //intake audience spike
                                shootAudPos,
                                intakeSkibidiArea
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                intakeSkibidiArea,
                                shootSkibidiArea
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootSkibidiArea,
                                intakeSkibidiArea
                        )
                ).setTangentHeadingInterpolation()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeFarSequence();
            case 2: return intakeMidAndShoot();
            case 3: return gateSequence();
            case 4: return shootGateSequence();
            case 5: return gateSequence();
            case 6: return shootGateSequence();
            case 7: return intakeCloseAndShoot();
            case 8: return intakeSkibAndShoot();
            case 9: return intakeSkibAndShoot();
            case 10: return park();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose alliancePose(Pose pose) {
        return side == G.Side.BLUE ? pose.mirror() : pose;
    }
}
