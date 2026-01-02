package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class B12BackPaths {
    private final Follower f;

    public Pose start = new Pose(56, 7.6772334293948195, Math.toRadians(90));
    public Pose shoot0 = new Pose(57, 17, Math.toRadians(109));
    public Pose intakeCloseHold1 = new Pose(62, 27);
    public Pose intakeClose = new Pose(24, 24, Math.toRadians(180));
    public Pose shoot1 = new Pose(59, 10, Math.toRadians(133));
    public Pose intakeMidHold1 = new Pose(62, 51);
    public Pose intakeMid = new Pose(25, 48, Math.toRadians(180));
    public Pose shoot2 = new Pose(67, 3.89, Math.toRadians(180));
    public Pose intakeHp1 = new Pose(30.45, 11, Math.toRadians(-145));
    public Pose intakeHp2 = new Pose(31, 10, Math.toRadians(-109));
    public Pose shoot3 = new Pose(67, 3.89, Math.toRadians(180));
    public Pose park = new Pose(59, 36, Math.toRadians(270));

    private int index;

    public B12BackPaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder()
                .addPath(new BezierLine(start, shoot0))
                .setLinearHeadingInterpolation(start.getHeading(), shoot0.getHeading())
                .build();
    }

    public PathChain intakeClose() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot0,
                                intakeCloseHold1,
                                intakeClose
                        )
                )
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeClose.getHeading())
                .build();
    }

    public PathChain score1() {
        return f.pathBuilder()
                .addPath(new BezierLine(intakeClose, shoot1))
                .setLinearHeadingInterpolation(intakeClose.getHeading(), shoot1.getHeading())
                .build();
    }

    public PathChain intakeMid() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot1,
                                intakeMidHold1,
                                intakeMid
                        )
                )
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeMid.getHeading())
                .build();
    }

    public PathChain score2() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeMid, shoot2)
                )
                .setLinearHeadingInterpolation(intakeMid.getHeading(), shoot2.getHeading())
                .build();
    }

    public PathChain intakeHpX() {
        return f.pathBuilder()
                .addPath(new BezierLine(shoot2, intakeHp1))
                .setLinearHeadingInterpolation(shoot2.getHeading(), intakeHp1.getHeading())
                .addParametricCallback(0.7, () -> f.setMaxPower(0.3))
                .addPath(new BezierLine(intakeHp1, intakeHp2))
                .setLinearHeadingInterpolation(intakeHp1.getHeading(), intakeHp2.getHeading())
                .build();
    }

    public PathChain score3() {
        return f.pathBuilder()
                .addPath(new BezierLine(intakeHp2, shoot3))
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeHp2.getHeading(), shoot3.getHeading())
                .build();
    }

    public PathChain parkX() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot3,
                                park
                        )
                ).setLinearHeadingInterpolation(shoot3.getHeading(), park.getHeading())
                .build();
    }

    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeClose();
            case 2: return score1();
            case 3: return intakeMid();
            case 4: return score2();
            case 5: return intakeHpX();
            case 6: return score3();
            case 7: return parkX();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}