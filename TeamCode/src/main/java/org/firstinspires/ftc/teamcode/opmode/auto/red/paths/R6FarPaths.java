package org.firstinspires.ftc.teamcode.opmode.auto.red.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class R6FarPaths {
    private final Follower f;


    private int index;

    public R6FarPaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0() { //score preloads + get to intake position and comes back
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(69.000, 0),

                                new Pose(120, 22)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(-45))
                .setHeadingConstraint(10)
                .addPath(
                        new BezierLine(
                                new Pose(126, 22),

                                new Pose(131.000, 15)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(-53))
                .setHeadingConstraint(Math.toRadians(10))
                .addPath(
                        new BezierLine(
                                new Pose(113.000, 15),

                                new Pose(133, 10)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-53), Math.toRadians(-64))
                .setHeadingConstraint(Math.toRadians(10))
                .addPath(
                        new BezierLine(
                                new Pose(131.000, 19.000),

                                new Pose(130, 10.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-64), Math.toRadians(-71))
                .setHeadingConstraint(Math.toRadians(10))
                .addPath(
                        new BezierLine(
                                new Pose(134.000, 10.000),

                                new Pose(124, 16)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-71), Math.toRadians(-36))
                .setHeadingConstraint(Math.toRadians(10))
                .addPath(
                        new BezierLine(
                                new Pose(124, 16),

                                new Pose(130, 10)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-36), Math.toRadians(-73))
                .setHeadingConstraint(Math.toRadians(10))
                .addPath(
                        new BezierLine(
                                new Pose(130, 10),

                                new Pose(135, 12)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-73), Math.toRadians(-90))
                .setHeadingConstraint(Math.toRadians(10))
                .build();
    }

    public PathChain intakeFar() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(135, 12),

                                new Pose(79.000, -1.000)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(180))
                .build();
    }

    public PathChain intakeClose() { //score the last ramp balls
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(79.000, -1.000),

                                new Pose(110, 27)
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-90))
                .build();
    }



    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeFar();
            case 2: return intakeClose();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}