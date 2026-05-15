package org.firstinspires.ftc.teamcode.opmode.auto.playoffs.paths;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class BlueOpenGate {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, intakeFarPose, intakeMidHold2Pose, gateHoldPos, gateOpenPos, intakeFarHoldPose, shootClosePose, shootClose2Pose, intakeMidHoldPose, intakeMidPose, shootGatePos, gateIntakePos, intakeAud1HoldPos, intakeAud2HoldPos, intakeAudPos, shootAudPos, initialPark;
    private int index;

    public BlueOpenGate(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = alliancePose(Globals.Positions.RED_EXODUS_START);

        shootClosePose = alliancePose(new Pose(104.96829971181555, 100.9452449567723, Math.toRadians(-50)));

        intakeFarPose = alliancePose(new Pose(122, 85.73487031700286, Math.toRadians(0)));
        intakeFarHoldPose = alliancePose(new Pose(111.79, 85.73487031700286));

        shootClose2Pose = alliancePose(new Pose(90.24207492795388, 86.22190201729106, Math.toRadians(0)));

        intakeMidPose = alliancePose(new Pose(133.82708933717578, 57));
        intakeMidHoldPose = alliancePose(new Pose(94.80979827089338, 63.55619596541784));
        intakeMidHold2Pose = alliancePose(new Pose(111.8616714697406, 59.04610951008643));

        gateHoldPos = alliancePose(new Pose(117.16426512968303, 62.62536023054753));
        gateOpenPos = alliancePose(new Pose(127.90778097982712, 62.43227665706051, Math.toRadians(0)));

        gateIntakePos = alliancePose(new Pose(130, 58.95244956772334, Math.toRadians(29)));
        shootGatePos = alliancePose(new Pose(88.79250720461096, 80.60230547550432, Math.toRadians(-26)));

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
                                intakeMidHold2Pose,
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