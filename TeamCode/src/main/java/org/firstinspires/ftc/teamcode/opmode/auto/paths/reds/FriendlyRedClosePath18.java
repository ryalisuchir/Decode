package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class FriendlyRedClosePath18 {
    private final Follower f;
    private final G.Side side;

    public Pose startPos, shoot0Pos, intakeMidHoldPos, intakeMidPos, shoot1Pos, gateIntakePos, shootGatePos, intakeAudHoldPos, intakeAudPos, shootAudPos, intakeObeliskHoldPos, intakeObeliskPos, shootObeliskPos, initialPark, finalPark;
    private int index;

    public FriendlyRedClosePath18(Halo r) {
        this.f = r.dt.getFollower();
        this.side = G.side;

        startPos = G.RED_CUBE_START;
        shoot0Pos = alliancePose(new Pose(91, 92, Math.toRadians(-111)));
        intakeMidHoldPos = alliancePose(new Pose(101.27012987012986, 61.11558441558441));
        intakeMidPos = alliancePose(new Pose(127, 58));

        shoot1Pos = alliancePose(new Pose(86, 75, Math.toRadians(-26)));
        gateIntakePos = alliancePose(new Pose(134, 60, Math.toRadians(25)));
        shootGatePos = alliancePose(new Pose(89, 77, Math.toRadians(-26)));

        intakeAudHoldPos = alliancePose(new Pose(95.34025974025975, 33));
        intakeAudPos = alliancePose(new Pose(127, 35, Math.toRadians(-44)));
        shootAudPos = alliancePose(new Pose(88, 77, Math.toRadians(-26)));

        intakeObeliskHoldPos = alliancePose(new Pose(108.93636363636365, 82));
        intakeObeliskPos = alliancePose(new Pose(120.5, 84, Math.toRadians(0)));
        shootObeliskPos = alliancePose(new Pose(90, 114, Math.toRadians(0)));

        initialPark = alliancePose(new Pose(90, 110, Math.toRadians(0)));

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
                                initialPark
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), initialPark.getHeading())
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
            case 6: return intakeCloseAndShoot();
            case 7: return gateSequence();
            case 8: return shootGateSequence();
            case 9: return park();
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
