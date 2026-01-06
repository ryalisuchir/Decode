package org.firstinspires.ftc.teamcode.opmode.auto.red.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class R9FarPaths {
    private final Follower f;

    public Pose start = new Pose(85, 9, Math.toRadians(90));
    public Pose shoot0 = new Pose(82, 18, Math.toRadians(78));
    public Pose intakeCloseHold1 = new Pose(70, 46);
    public Pose intakeClose = new Pose(140, 49, Math.toRadians(0));
    public Pose shoot1 = new Pose(82, 18, Math.toRadians(78));
    public Pose intakeHp1 = new Pose(150, 28, Math.toRadians(-69));
    public Pose intakeHp2 = new Pose(155, 20, Math.toRadians(-55));
    public Pose shoot3 = new Pose(90, 27, Math.toRadians(0));
    public Pose park = new Pose(103, 35, Math.toRadians(90));

    private int index;

    public R9FarPaths(Robot r) {
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
                .addParametricCallback(0.8, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeClose.getHeading())
                .build();
    }

    public PathChain score1() {
        return f.pathBuilder()
                .addPath(new BezierLine(intakeClose, shoot1))
                .setLinearHeadingInterpolation(intakeClose.getHeading(), shoot1.getHeading())
                .build();
    }


    public PathChain intakeHpX() {
        return f.pathBuilder()
                .addPath(new BezierLine(shoot1, intakeHp1))
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeHp1.getHeading())
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
            case 3: return intakeHpX();
            case 4: return score3();
            case 5: return parkX();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}