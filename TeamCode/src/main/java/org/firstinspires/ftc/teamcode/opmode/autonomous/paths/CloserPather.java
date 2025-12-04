package org.firstinspires.ftc.teamcode.opmode.autonomous.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

public class CloserPather {
    private final Follower f;
    public PathChain shoot0;
    public PathChain intake1;
    public PathChain gate;
    public PathChain shoot1;
    public PathChain intake2;
    public PathChain shoot2;
    public PathChain intake3;
    public PathChain shoot3;
    public PathChain park;

    private int index;

    public CloserPather(Robot r) {
        this.f = r.follower;
        if (Globals.side.equals(Globals.Side.BLUE)) {
            shoot0 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(27.389, 133.418),
                                    new Pose(58.000, 106.000),
                                    new Pose(59.550, 79.262)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(325), Math.toRadians(180))
                    .build();

            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(59.550, 79.262), new Pose(22.202, 80.715))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            gate = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(22.202, 80.715),
                                    new Pose(31.124, 68.058),
                                    new Pose(16.599, 71.585)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(16.599, 71.585), new Pose(59.550, 79.055))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(215))
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(59.550, 79.055),
                                    new Pose(68.265, 54.778),
                                    new Pose(19.712, 56.853)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(215), Math.toRadians(180))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(19.712, 56.853), new Pose(59.550, 78.847))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(215))
                    .build();

            intake3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(59.550, 78.847),
                                    new Pose(77.187, 36.519),
                                    new Pose(15.562, 36.519)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(215), Math.toRadians(180))
                    .build();

            shoot3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(15.562, 36.519), new Pose(59.758, 79.262))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(215))
                    .build();

            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(59.758, 79.262), new Pose(20.334, 73.037))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(215), Math.toRadians(270))
                    .build();
        }

        if (Globals.side == Globals.Side.RED) {
            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(88.392, 8.922),
                                    new Pose(84.450, 45.856),
                                    new Pose(130.098, 38.594)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(360))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(130.098, 38.594), new Pose(85.280, 12.450))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(45))
                    .setReversed()
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(85.280, 12.450),
                                    new Pose(85.902, 71.378),
                                    new Pose(129.683, 62.455)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(360))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(129.683, 62.455), new Pose(87.147, 85.902))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(315))
                    .build();

            intake3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(87.147, 85.902), new Pose(128.646, 86.939))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(315), Math.toRadians(360))
                    .build();

            shoot3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(128.646, 86.939), new Pose(86.939, 86.110))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))
                    .setReversed()
                    .build();

            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(86.939, 86.110), new Pose(123.251, 69.303))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(270))
                    .build();
        }
        index = 0;
    }

    public PathChain next() {
        switch (index++) {
            case 0: return shoot0;
            case 1: return intake1;
            case 2: return gate;
            case 3: return shoot1;
            case 4: return intake2;
            case 5: return shoot2;
            case 6: return intake3;
            case 7: return shoot3;
            case 8: return park;
            default: return null;
        }
    }

    public boolean hasNext() {
        int PATH_COUNT = 8;
        return index < PATH_COUNT;
    }

    public void reset() {
        index = 0;
    }

}
