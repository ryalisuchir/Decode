package org.firstinspires.ftc.teamcode.opmode.autonomous.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

public class FarPather {
    private final Follower f;
    public PathChain shoot0;
    public PathChain pretake1;
    public PathChain intake1;
    public PathChain shoot1;
    public PathChain intake2;
    public PathChain shoot2;
    public PathChain park;

    private int index;

    public FarPather(Robot r) {
        this.f = r.follower;
        if (Globals.side.equals(Globals.Side.BLUE)) {
            shoot0 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(54.986, 8.092), new Pose(55, 87))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(128))
                    .build();

            pretake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(55.000, 87.000), new Pose(40.876, 94))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(128), Math.toRadians(180))
                    .build();

            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(40.876, 94), new Pose(14, 86.939))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(14, 94.617), new Pose(55.401, 87.147))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(128))
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(55.401, 87.147),
                                    new Pose(87.354, 61.418),
                                    new Pose(7.055, 65.775)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(128), Math.toRadians(180))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(7.055, 65.775), new Pose(55, 87))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(128))
                    .build();

            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(46.893, 89.429), new Pose(6, 68.888))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(128), Math.toRadians(270))
                    .build();
        }
        if (Globals.side.equals(Globals.Side.RED)) {
            shoot0 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(88.807, 8.092), new Pose(81.545, 74.282))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(42))
                    .build();

            pretake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(81.545, 74.282), new Pose(94.409, 75.112))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(360))
                    .build();

            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(94.409, 75.112), new Pose(109, 75.527))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(109, 75.527), new Pose(81.752, 74.075))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(42))
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(81.752, 74.075),
                                    new Pose(60.795, 42.536),
                                    new Pose(110, 44.196)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(360))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(110, 44.196), new Pose(81.752, 74.282))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(42))
                    .build();

            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(81.752, 74.282), new Pose(135.285, 66.813))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(270))
                    .build();
        }
        index = 0;
    }

    public PathChain next() {
        switch (index++) {
            case 0: return shoot0;
            case 1: return pretake1;
            case 2: return intake1;
            case 3: return shoot1;
            case 4: return intake2;
            case 5: return shoot2;
            case 6: return park;
            default: return null;
        }
    }

    public boolean hasNext() {
        int PATH_COUNT = 6;
        return index < PATH_COUNT;
    }

    public void reset() {
        index = 0;
    }

}
