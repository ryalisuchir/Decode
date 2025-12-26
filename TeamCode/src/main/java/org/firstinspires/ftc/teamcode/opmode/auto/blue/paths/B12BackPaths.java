package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class B12BackPaths {
    private final Follower f;

    public Pose start = new Pose(54.98559077809798, 7.6772334293948195, Math.toRadians(90));
    public Pose shoot0 = new Pose(53.74063400576369, 9.95965417867435, Math.toRadians(90));
    public Pose intakeCloseHold1 = new Pose(52.0806916426513, 37.76368876080692);
    public Pose intakeClose = new Pose(7.469740634005764, 34.65129682997118, Math.toRadians(180));
    public Pose shoot1 = new Pose(53.118155619596536, 9.544668587896261, Math.toRadians(180));
    public Pose intakeMidHold1 = new Pose(54.778097982708935, 68.47262247838617);
    public Pose intakeMid = new Pose(9.752161383285301, 59.757925072046106, Math.toRadians(180));
    public Pose shoot2 = new Pose(52.91066282420749, 15.561959654178676, Math.toRadians(180));
    public Pose intakeHp = new Pose(6.432276657060519, 7.054755043227662, Math.toRadians(180));
    public Pose shoot3 = new Pose(55.19308357348703, 8.507204610951014, Math.toRadians(180));
    public Pose park = new Pose(34.85878962536023, 7.884726224783856);

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
                .addPath(new BezierLine(shoot2, intakeHp))
                .setLinearHeadingInterpolation(shoot2.getHeading(), intakeHp.getHeading())
                .build();
    }

    public PathChain score3() {
        return f.pathBuilder()
                .addPath(new BezierLine(intakeHp, shoot3))
                .setLinearHeadingInterpolation(intakeHp.getHeading(), shoot3.getHeading())
                .build();
    }

    public PathChain parkX() {
        return f.pathBuilder()
                .addPath(new BezierLine(shoot3, park))
                .setTangentHeadingInterpolation()
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