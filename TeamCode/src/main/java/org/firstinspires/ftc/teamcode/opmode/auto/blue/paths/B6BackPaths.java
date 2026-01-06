package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class B6BackPaths {
    private final Follower f;

    public Pose start = new Pose(55, 8, Math.toRadians(90));
    public Pose shoot0 = new Pose(57, 12, Math.toRadians(109));
    public Pose intakeHp1 = new Pose(30.45, 18, Math.toRadians(-145));
    public Pose intakeHp2 = new Pose(23, 10, Math.toRadians(-109));
    public Pose shoot3 = new Pose(67, 3.89, Math.toRadians(180));
    public Pose park = new Pose(59, 36, Math.toRadians(270));

    private int index;

    public B6BackPaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0() {
        return f.pathBuilder()
                .addPath(new BezierLine(start, shoot0))
                .setLinearHeadingInterpolation(start.getHeading(), shoot0.getHeading())
                .build();
    }

    public PathChain intakeHpX() {
        return f.pathBuilder()
                .addPath(new BezierLine(shoot0, intakeHp1))
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeHp1.getHeading())
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
            case 1: return intakeHpX();
            case 2: return score3();
            case 3: return parkX();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}