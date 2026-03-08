package org.firstinspires.ftc.teamcode.opmode.auto.paths.blues;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class BlueSortedPath12 {
    private final Follower f;
    private final G.Side side;

    public Pose startPos, shootPos, intakeMidHoldPos, intakeMidPos, openGateHoldPos, openGatePos, shootMidHoldPos, shoot1Pos, intakeAudHoldPos, intakeAudPos, shootAudPos, intakeObeliskHoldPos, intakeObeliskPos, shootObeliskPos, initialPark, finalPark;
    private int index;

    public BlueSortedPath12(Halo r) {
        this.f = r.dt.getFollower();
        this.side = G.side;

        startPos = G.BLUE_CUBE_START;

        shootPos = alliancePose(new Pose(90, 77, Math.toRadians(0)));

        intakeMidHoldPos = alliancePose(new Pose(101.27012987012986, 55));
        intakeMidPos = alliancePose(new Pose(130, 52, Math.toRadians(0)));

        openGatePos = alliancePose(new Pose(127, 64.04322766570606, Math.toRadians(-5)));
        openGateHoldPos = alliancePose(new Pose(111.92507204610952, 62.81412103746395));

        intakeAudHoldPos = alliancePose(new Pose(95.34025974025975, 30));
        intakeAudPos = alliancePose(new Pose(133, 32, Math.toRadians(-44)));

        intakeObeliskHoldPos = alliancePose(new Pose(108.93636363636365, 79));
        intakeObeliskPos = alliancePose(new Pose(124.5, 79, Math.toRadians(0)));

        initialPark = alliancePose(new Pose(105, 120, Math.toRadians(-90)));
        finalPark = alliancePose(new Pose(105, 135.0201729106628, Math.toRadians(-90)));

        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shootPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve( //to get to the intake pos
                                shootPos,
                                intakeMidHoldPos,
                                intakeMidPos
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), intakeMidPos.getHeading())
                .addPath(
                        new BezierCurve(
                                intakeMidPos,
                                openGateHoldPos,
                                openGatePos
                        )
                ).setLinearHeadingInterpolation(intakeMidPos.getHeading(), openGatePos.getHeading())
                .build();
    }

    public PathChain shootGateOpen() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine( //to get to the shoot pos
                                openGatePos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(openGatePos.getHeading(), shootPos.getHeading())
                .build();

    }

    public PathChain intakeFarSequence() {
        return f.pathBuilder().addPath(
                        new BezierCurve( //intake audience spike
                                shootPos,
                                intakeAudHoldPos,
                                intakeAudPos
                        )
                ).setTangentHeadingInterpolation()
                .addPath( //shoot audience spike
                        new BezierLine(
                                intakeAudPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeAudPos.getHeading(), shootPos.getHeading())
                // .setReversed()
                .build();
    }

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                intakeObeliskHoldPos,
                                intakeObeliskPos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeObeliskPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeObeliskPos.getHeading(), shootPos.getHeading())
                .addParametricCallback(0.5, () -> f.setMaxPower(1))
                //.setReversed()
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootPos,
                                initialPark
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), initialPark.getHeading())
                .addPath(
                        new BezierLine(
                                initialPark,
                                finalPark
                        )
                ).setLinearHeadingInterpolation(initialPark.getHeading(), finalPark.getHeading())
                .addParametricCallback(0.8, () -> f.setMaxPower(0.5))
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeMidAndShoot();
            case 2: return shootGateOpen();
            case 3: return intakeFarSequence();
            case 4: return intakeCloseAndShoot();
            case 5: return park();
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
