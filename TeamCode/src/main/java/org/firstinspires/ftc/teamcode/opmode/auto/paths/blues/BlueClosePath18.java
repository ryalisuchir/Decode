package org.firstinspires.ftc.teamcode.opmode.auto.paths.blues;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class BlueClosePath18 {
    private final Follower f;
    private final G.Side side;

    public Pose startPos, shoot0Pos, intakeMidHoldPos, intakeMidPos, shoot1Pos, gateIntakePos, shootGatePos, intakeAudHoldPos, intakeAudPos, shootAudPos, intakeObeliskHoldPos, intakeObeliskPos, shootObeliskPos;
    private int index;

    public BlueClosePath18(Halo r) {
        this.f = r.dt.getFollower();
        this.side = G.side;

        startPos = G.BLUE_CUBE_START;
        shoot0Pos = alliancePose(new Pose(91, 92, Math.toRadians(180)));
        intakeMidHoldPos = alliancePose(new Pose(101.27012987012986, 61.11558441558441));
        intakeMidPos = alliancePose(new Pose(127, 58));
        shoot1Pos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));
        gateIntakePos = alliancePose(new Pose(133, 58, Math.toRadians(25)));
        shootGatePos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));
        intakeAudHoldPos = alliancePose(new Pose(95.34025974025975, 30.354545454545455));
        intakeAudPos = alliancePose(new Pose(127, 32, Math.toRadians(-44)));
        shootAudPos = alliancePose(new Pose(88, 77, Math.toRadians(0)));
        intakeObeliskHoldPos = alliancePose(new Pose(108.93636363636365, 80.64155844155844));
        intakeObeliskPos = alliancePose(new Pose(118, 81, Math.toRadians(360)));
        shootObeliskPos = alliancePose(new Pose(90, 110, Math.toRadians(360)));

        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                                shoot0Pos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shoot0Pos.getHeading())

                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve( //to get to the intake pos
                                shoot0Pos,
                                intakeMidHoldPos,
                                intakeMidPos
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine( //to get to the shoot pos
                                intakeMidPos,
                                shoot1Pos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain gateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shoot1Pos,
                                gateIntakePos
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), gateIntakePos.getHeading())
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

    public PathChain intakeFarSequence() {
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
                // .setReversed()
                .build();
    }

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootAudPos,
                                intakeObeliskHoldPos,
                                intakeObeliskPos
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                intakeObeliskPos,
                                shoot1Pos
                        )
                ).setLinearHeadingInterpolation(intakeObeliskPos.getHeading(), shoot1Pos.getHeading())
                //.setReversed()
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot1Pos,
                                shootObeliskPos
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), shootObeliskPos.getHeading())
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeMidAndShoot();
            case 2: return gateSequence();
            case 3: return shootGateSequence();
            case 4: return gateSequence();
            case 5: return shootGateSequence();
            case 6: return intakeFarSequence();
            case 7: return intakeCloseAndShoot();
            case 8: return park();
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
