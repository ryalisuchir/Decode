package org.firstinspires.ftc.teamcode.opmode.autonomous.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

public class RedFarPather {
    private final Follower f;
    public PathChain shoot0;
    public PathChain pretake1;
    public PathChain intake1;
    public PathChain shoot1;
    public PathChain pretake2;
    public PathChain intake2;
    public PathChain shoot2;
    public PathChain park;

    private int index;

    public RedFarPather(Robot r) {
        this.f = r.follower;
        if (Globals.side.equals(Globals.Side.RED)) {
            shoot0 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(88.80691642651297, 8.092219020172909), new Pose(85.27953890489913, 86))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(40))
                    .build();

            pretake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(85.27953890489913, 86), new Pose(90.259, 74.490))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(40), Math.toRadians(360))
                    .build();

            intake1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(90.259, 74.490), new Pose(111, 74.075))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))
                    .build();

            shoot1 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(111, 74.075), new Pose(85.27953890489913, 86))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(40))
                    .build();

            pretake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(85.27953890489913, 86), new Pose(85, 27))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(40), Math.toRadians(360))
                    .build();

            intake2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(85, 27), new Pose(115, 27))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))
                    .build();

            shoot2 = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(115, 24.899), new Pose(85.27953890489913, 86))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(40))
                    .build();
            park = f
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(80.300, 74.697), new Pose(100.42651296829972, 49.590778097982714))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(40), Math.toRadians(270))
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
            case 4: return pretake2;
            case 5: return intake2;
            case 6: return shoot2;
            case 7: return park;
            default: return null;
        }
    }

    public boolean hasNext() {
        int PATH_COUNT = 7;
        return index < PATH_COUNT;
    }

    public void reset() {
        index = 0;
    }

}
