package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class B12ClosePaths {
    private final Follower f;

    public final Pose startPos = Globals.BLUE_CUBE_START;
    public final Pose shoot0Pos = new Pose(51.873, 79.055, Math.toRadians(180));
    public final Pose intakeFarPos = new Pose(16.081, 78.017);
    public final Pose shoot1Pos = new Pose(51.666, 79.262);
    public final Pose intakeMidHold1Pos = new Pose(49.174, 57.735);
    public final Pose intakeMidHold2Pos = new Pose(32.990, 58.329);
    public final Pose intakeMidPos = new Pose(10.182, 58.069);
    public final Pose shoot2Pos = new Pose(51.118, 79.340);
    public final Pose intakeCloseHold1Pos = new Pose(49.716, 33.849);
    public final Pose intakeCloseHold2Pos = new Pose(34.909, 35.774);
    public final Pose intakeClosePos = new Pose(10.003, 35.516);
    public final Pose shoot3Pos = new Pose(58.758, 109.291);


    private int index;

    public B12ClosePaths(Robot r) {
        this.f = r.dt.getFollower();
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
                .build();
    }

    public PathChain intakeFarAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot0Pos,
                                intakeFarPos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0.9, () -> f.setMaxPower(0.3))
                .addPath(
                        new BezierLine(
                                intakeFarPos,
                                shoot1Pos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setReversed()
                .build();
    }

    public PathChain intakeMidAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot1Pos,
                                intakeMidHold1Pos,
                                intakeMidHold2Pos,
                                intakeMidPos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0.9, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeMidPos,
                                shoot2Pos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setReversed()
                .build();
    }

    public PathChain intakeCloseAndShoot() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot2Pos,
                                intakeCloseHold1Pos,
                                intakeCloseHold2Pos,
                                intakeClosePos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0.9, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeClosePos,
                                shoot3Pos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setReversed()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0();
            case 1: return intakeFarAndShoot();
            case 2: return intakeMidAndShoot();
            case 3: return intakeCloseAndShoot();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}