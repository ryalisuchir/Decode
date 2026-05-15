package org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class Red18OpenPathing {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, intakeFarPose, gateHoldPos, gateOpenPos, intakeFarHoldPose, shootClosePose, shootClose2Pose, intakeMidHoldPose, intakeMidPose, shootGatePos, gateIntakePos, intakeAud1HoldPos, intakeAud2HoldPos, intakeAudPos, shootAudPos, initialPark;
    private int index;

    public Red18OpenPathing(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = alliancePose(Globals.Positions.RED_EXODUS_START);

        shootClosePose = alliancePose(new Pose(104.96829971181555, 100.9452449567723, Math.toRadians(-50)));

        intakeFarPose = alliancePose(new Pose(122, 85.73487031700286, Math.toRadians(0)));
        intakeFarHoldPose = alliancePose(new Pose(111.79, 85.73487031700286));

        shootClose2Pose = alliancePose(new Pose(90.24207492795388, 86.22190201729106, Math.toRadians(0)));

        intakeMidPose = alliancePose(new Pose(126.77233429394813, 59.59077809798269));
        intakeMidHoldPose = alliancePose(new Pose(111.79538904899135, 59.265129682997106));

        gateHoldPos = alliancePose(new Pose(117.16426512968303, 62.62536023054753));
        gateOpenPos = alliancePose(new Pose(127.90778097982712, 62.43227665706051, Math.toRadians(0)));

        gateIntakePos = alliancePose(new Pose(131.5, 57.5, Math.toRadians(22.5)));
        shootGatePos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

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

    public PathChain intakeMidAndOpen() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootClose2Pose,
                                intakeMidHoldPose,
                                intakeMidPose
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                intakeMidPose,
                                gateHoldPos,
                                gateOpenPos
                        )
                ).setLinearHeadingInterpolation(intakeMidPose.getHeading(), gateOpenPos.getHeading())
                .build();
    }

    public PathChain shootMiddle() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                gateOpenPos,
                                shootGatePos
                        )
                ).setLinearHeadingInterpolation(gateOpenPos.getHeading(), shootGatePos.getHeading())
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

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootGatePos,
                                initialPark
                        )
                ).setLinearHeadingInterpolation(shootGatePos.getHeading(), initialPark.getHeading())
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeFarSequence();
            case 2: return intakeMidAndOpen();
            case 3: return shootMiddle();
            case 4: return gateSequence();
            case 5: return shootGateSequence();
            case 6: return gateSequence();
            case 7: return shootGateSequence();
            case 8: return gateSequence();
            case 9: return shootGateSequence();
            case 10: return gateSequence();
            case 11: return shootGateSequence();
            case 14: return park();
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
