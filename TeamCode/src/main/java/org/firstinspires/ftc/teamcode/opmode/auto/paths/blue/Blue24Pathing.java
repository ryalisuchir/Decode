package org.firstinspires.ftc.teamcode.opmode.auto.paths.blue;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class Blue24Pathing {
    private final Follower f;
    private final Globals.Alliance alliance;

    public Pose startPos, intakeFarPose, shootClosePose, intakeMidPose, shootGatePos, gateIntakePos, intakeAudHoldPos, intakeAudPos, shootAudPos, initialPark;
    private int index;

    public Blue24Pathing(Halo r) {
        this.f = r.dt.getFollower();
        this.alliance = Globals.alliance;

        startPos = Globals.Positions.BLUE_EXODUS_START;

        shootClosePose = alliancePose(new Pose(119.49279538904898, 106.13256484149855, Math.toRadians(-90)));
        intakeFarPose = alliancePose(new Pose(118, 88, Math.toRadians(-80)));
        intakeMidPose = alliancePose(new Pose(118, 64, Math.toRadians(-80)));

        gateIntakePos = alliancePose(new Pose(132.8, 58, Math.toRadians(22.5)));
        shootGatePos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

        intakeAudHoldPos = alliancePose(new Pose(97, 33));
        intakeAudPos = alliancePose(new Pose(129, 35, Math.toRadians(-44)));
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

    public PathChain intakeCloseAndShoot() {
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
            case 10: return gateSequence();
            case 11: return shootGateSequence();
            case 12: return park();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose alliancePose(Pose pose) {
        return alliance == Globals.Alliance.BLUE ? pose.mirror() : pose;
    }
}
