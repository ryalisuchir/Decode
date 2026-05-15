package org.firstinspires.ftc.teamcode.opmode.auto.paths.red.close;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class RedPush18SortedPathing {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, pushPos, shoot0Pos, shoot0CPos, intake2Pos, intake2CPos, shoot2Pos, gateIntakePos, gateShootPos, intake3Pos, intake3CPos, shoot3Pos, intake1Pos, shoot1Pos, parkPos;
    private int index;

    public RedPush18SortedPathing(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = alliancePose(Globals.Positions.PUSH_AUTO_RED);

        pushPos = alliancePose(new Pose(119.9077809798271, 112.14985590778096, Math.toRadians(90)));
        shoot0Pos = alliancePose(new Pose(94.15561959654177, 94.31412103746396, Math.toRadians(-64)));
        shoot0CPos = alliancePose(new Pose(109.63400576368875, 131.6959654178674));

        intake2Pos = alliancePose(new Pose(130.65129682997116, 59.87896253602305));
        intake2CPos = alliancePose(new Pose(100.21902017291066, 60.58789625360231));

        shoot2Pos = alliancePose(new Pose(85.4092219020173, 69.06628242074927, Math.toRadians(-12.5)));

        gateIntakePos = alliancePose(new Pose(131.5, 58.5, Math.toRadians(22.5)));
        gateShootPos = alliancePose(new Pose(89, 75, Math.toRadians(-26)));

        intake3Pos = alliancePose(new Pose(124.28242074927955, 38.011527377521595, Math.toRadians(-44)));
        intake3CPos = alliancePose(new Pose(105.15561959654174, 39.22766570605187));

        shoot3Pos = alliancePose(new Pose(96.71757925072043, 84.23342939481269));

        intake1Pos = alliancePose(new Pose(122.4322766570605, 84.9942363112392));
        shoot1Pos = alliancePose(new Pose(98.30259365994237, 84.43227665706053));

        parkPos = alliancePose(new Pose(109.97118155619596, 84.44956772334294));


        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                startPos,
                                shoot0CPos,
                                shoot0Pos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain spike2Sequence() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shoot0Pos,
                                intake2CPos,
                                intake2Pos
                        )
                ).setLinearHeadingInterpolation(shoot0Pos.getHeading(), intake2Pos.getHeading())
                .addPath(
                        new BezierLine(
                                intake2Pos,
                                shoot2Pos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain firstGateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shoot2Pos,
                                gateIntakePos
                        )
                ).setLinearHeadingInterpolation(shoot2Pos.getHeading(), gateIntakePos.getHeading())
                .build();
    }


    public PathChain gateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                gateShootPos,
                                gateIntakePos
                        )
                ).setLinearHeadingInterpolation(gateShootPos.getHeading(), gateIntakePos.getHeading())
                .build();
    }

    public PathChain shootGateSequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                gateIntakePos,
                                gateShootPos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain spike3Sequence() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                gateShootPos,
                                intake3CPos,
                                intake3Pos
                        )
                ).setLinearHeadingInterpolation(gateShootPos.getHeading(), intake3Pos.getHeading())
                .addPath(
                        new BezierLine(
                                intake3Pos,
                                shoot3Pos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain spike1Sequence() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shoot3Pos,
                                intake1Pos
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                intake1Pos,
                                shoot1Pos
                        )
                ).setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot1Pos,
                                parkPos
                        )
                ).setTangentHeadingInterpolation()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return spike2Sequence();
            case 2: return firstGateSequence();
            case 3: return shootGateSequence();
            case 4: return gateSequence();
            case 5: return shootGateSequence();
            case 6: return spike3Sequence();
            case 7: return spike1Sequence();
            case 8: return park();
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
