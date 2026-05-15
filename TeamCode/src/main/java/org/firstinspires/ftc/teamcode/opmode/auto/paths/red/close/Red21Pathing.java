package org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class Red21Pathing {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, intakeFarPose, intakeFarHoldPose, shootClosePose, shootClose2Pose, intakeMidHoldPose, intakeMidPose, shootGatePos, gateIntakePos, intakeAud1HoldPos, intakeAud2HoldPos, intakeAudPos, shootAudPos, initialPark;
    private int index;

    public Red21Pathing(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = alliancePose(Globals.Positions.RED_EXODUS_START);

        shootClosePose = alliancePose(new Pose(104.96829971181555, 100.9452449567723, Math.toRadians(-50)));

        intakeFarPose = alliancePose(new Pose(122, 85.73487031700286, Math.toRadians(0)));
        intakeFarHoldPose = alliancePose(new Pose(111.79, 85.73487031700286));

        shootClose2Pose = alliancePose(new Pose(100.20172910662822, 85.39193083573485, Math.toRadians(0)));

        intakeMidPose = alliancePose(new Pose(126.77233429394813, 59.59077809798269));
        intakeMidHoldPose = alliancePose(new Pose(111.79538904899135, 59.265129682997106));

        gateIntakePos = alliancePose(new Pose(133, 56.5, Math.toRadians(33)));
        shootGatePos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

        intakeAudPos = alliancePose(new Pose(127, 38.011527377521595, Math.toRadians(-44)));
        intakeAud1HoldPos = alliancePose(new Pose(107.64553314121034, 44.74063400576366));
        intakeAud2HoldPos = alliancePose(new Pose(113.52593659942364, 37.96397694524494));
        shootAudPos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

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
                        new BezierCurve(
                                shootClosePose,
                                intakeFarHoldPose,
                                intakeFarPose
                        )
                ).setLinearHeadingInterpolation(shootClosePose.getHeading(), intakeFarPose.getHeading())

                .addPath(
                        new BezierLine(
                                intakeFarPose,
                                shootClose2Pose
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootClose2Pose,
                                intakeMidHoldPose,
                                intakeMidPose
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                intakeMidPose,
                                shootGatePos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
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

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve( //intake audience spike
                                shootGatePos,
                                intakeAud1HoldPos,
                                intakeAud2HoldPos,
                                intakeAudPos
                        )
                ).setTangentHeadingInterpolation()
                .addPath( //shoot audience spike
                        new BezierLine(
                                intakeAudPos,
                                shootAudPos
                        )
                ).setLinearHeadingInterpolation(intakeAudPos.getHeading(), shootAudPos.getHeading())
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootAudPos,
                                initialPark
                        )
                ).setLinearHeadingInterpolation(shootAudPos.getHeading(), initialPark.getHeading())
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
            case 8: return gateSequence();
            case 9: return shootGateSequence();
            case 10: return park();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose alliancePose(Pose pose) {
        return side == Globals.Alliance.BLUE ? pose.mirror() : pose;
    }
}
