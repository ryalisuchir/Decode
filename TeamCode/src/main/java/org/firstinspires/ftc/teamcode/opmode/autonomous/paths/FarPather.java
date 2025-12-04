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
    public PathChain intake1;
    public PathChain shoot1;
    public PathChain intake2;
    public PathChain shoot2;
    public PathChain intake3;
    public PathChain shoot3;
    public PathChain park;

    private int index;

    public FarPather(Robot r) {
        this.f = r.follower;
        if (Globals.side.equals(Globals.Side.BLUE)) {
            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(55.816, 8.507),
                                    new Pose(65.153, 40.254),
                                    new Pose(19.919, 42)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(19.919, 42), new Pose(50, 28))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(50, 28),
                                    new Pose(64.323, 67.643),
                                    new Pose(17, 65)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(17, 65), new Pose(40, 89.637))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(225))
                    .build();

            intake3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(40, 89.637), new Pose(18, 95))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(225), Math.toRadians(180))
                    .build();

            shoot3 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(18, 95), new Pose(40, 90.052))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(40, 90.052), new Pose(12, 70.133))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
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
            case 0: return intake1;
            case 1: return shoot1;
            case 2: return intake2;
            case 3: return shoot2;
            case 4: return intake3;
            case 5: return shoot3;
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
