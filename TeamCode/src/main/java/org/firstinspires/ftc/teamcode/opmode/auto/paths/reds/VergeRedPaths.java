package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class VergeRedPaths {
    private final Follower f;

    public Pose startPos, shoot0Pos, intakeCloseHold1Pos, intakeClosePos, shoot1Pos, intakeHp1, intakeHp2, intakeHp3, shoot2Pos, intakeHpAgain1, intakeHpAgain2, intakeHpAgain3, shoot3Pos, intakeHpAgainAgain1, intakeHpAgainAgain2, intakeHpAgainAgain3, shoot4Pos, parkPos;
    private int index;

    public VergeRedPaths(Robot r) {
        this.f = r.dt.getFollower();
        startPos = m(Globals.RED_FAR_START);
        shoot0Pos = m(new Pose(89, 18, Math.toRadians(0)));
        intakeCloseHold1Pos = m(new Pose(91.99423631123919, 39.903458213256485));
        intakeClosePos = m(new Pose(132.9221902017291, 36.22190201729107, Math.toRadians(0)));
        shoot1Pos = m(new Pose(91.1757925072046, 20, Math.toRadians(0)));
        intakeHp1 = m(new Pose(131.51296829971182, 8.60806916426513, Math.toRadians(-10)));
        intakeHp2 = m(new Pose(124.63976945244958, 8.518731988472624, Math.toRadians(-10)));
        intakeHp3 = m(new Pose(132.5878962536023, 10.740634005763688, Math.toRadians(0)));
        shoot2Pos = m(new Pose(89, 18, Math.toRadians(0)));
        intakeHpAgain1 = m(new Pose(134.62536023054756, 15, Math.toRadians(-10)));
        intakeHpAgain2 = m(new Pose(123.18731988472625, 14, Math.toRadians(-10)));
        intakeHpAgain3 = m(new Pose(134.45533141210376, 15, Math.toRadians(0)));
        shoot3Pos = m(new Pose(89, 18, Math.toRadians(0)));
        intakeHpAgainAgain1 = m(new Pose(134.62536023054756, 16, Math.toRadians(-10)));
        intakeHpAgainAgain2 = m(new Pose(123.18731988472625, 11, Math.toRadians(-10)));
        intakeHpAgainAgain3 = m(new Pose(134.45533141210376, 12, Math.toRadians(-0)));
        shoot4Pos = m(new Pose(89, 18, Math.toRadians(0)));
        parkPos = m(new Pose(115, 13, Math.toRadians(0)));

        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPos,
                                shoot0Pos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shoot0Pos.getHeading())
                .addParametricCallback(0.3, () -> f.setMaxPower(0.5))
                .build();
    }

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot0Pos,
                                intakeCloseHold1Pos,
                                intakeClosePos
                        )
                ).setLinearHeadingInterpolation(shoot0Pos.getHeading(), intakeClosePos.getHeading())
                .addParametricCallback(0.6, () -> f.setMaxPower(0.4))
                .addPath(
                        new BezierLine(
                                intakeClosePos,
                                shoot1Pos
                        )
                ).setLinearHeadingInterpolation(intakeClosePos.getHeading(), shoot1Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeHp() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot1Pos,
                                intakeHp1
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), intakeHp1.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHp1,
                                intakeHp2
                        )
                ).setLinearHeadingInterpolation(intakeHp1.getHeading(), intakeHp2.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHp2,
                                intakeHp3
                        )
                ).setLinearHeadingInterpolation(intakeHp2.getHeading(), intakeHp3.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .build();
    }

    public PathChain shoot1() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                intakeHp3,
                                shoot2Pos
                        )
                ).setLinearHeadingInterpolation(intakeHp3.getHeading(), shoot2Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain primary() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot2Pos,
                                intakeHpAgain1
                        )
                ).setLinearHeadingInterpolation(shoot2Pos.getHeading(), intakeHpAgain1.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHpAgain1,
                                intakeHpAgain2
                        )
                ).setLinearHeadingInterpolation(intakeHpAgain1.getHeading(), intakeHpAgain2.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHpAgain2,
                                intakeHpAgain3
                        )
                ).setLinearHeadingInterpolation(intakeHpAgain2.getHeading(), intakeHpAgain3.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .build();
    }

    public PathChain shoot2() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                intakeHpAgain3,
                                shoot3Pos
                        )
                ).setLinearHeadingInterpolation(intakeHpAgain3.getHeading(), shoot3Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setTValueConstraint(0.99)
                .build();
    }

    public PathChain secondary() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot3Pos,
                                intakeHpAgainAgain1
                        )
                ).setLinearHeadingInterpolation(shoot2Pos.getHeading(), intakeHpAgainAgain1.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHpAgainAgain1,
                                intakeHpAgainAgain2
                        )
                ).setLinearHeadingInterpolation(intakeHpAgainAgain1.getHeading(), intakeHpAgainAgain2.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierLine(
                                intakeHpAgainAgain2,
                                intakeHpAgainAgain3
                        )
                ).setLinearHeadingInterpolation(intakeHpAgainAgain2.getHeading(), intakeHpAgainAgain3.getHeading())
                .setHeadingConstraint(Math.toRadians(15))
                .build();
    }

    public PathChain shootlast() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                intakeHpAgainAgain3,
                                shoot4Pos
                        )
                ).setLinearHeadingInterpolation(intakeHpAgainAgain3.getHeading(), shoot4Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setTValueConstraint(0.99)
                .build();
    }

    public PathChain park() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot3Pos,
                                parkPos
                        )
                ).setLinearHeadingInterpolation(shoot3Pos.getHeading(), parkPos.getHeading())
                .build();
    }

    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeCloseAndShoot();
            case 2: return intakeHp();
            case 3: return shoot1();
            case 4: return primary();
            case 5: return shoot2();
            case 6: return secondary();
            case 7: return shootlast();
            case 8: return park();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose m(Pose p) {
        return Globals.side == Globals.Side.RED ? p : p.mirror();
    }
}