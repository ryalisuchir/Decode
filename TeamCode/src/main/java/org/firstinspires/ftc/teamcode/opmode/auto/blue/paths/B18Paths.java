package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class B18Paths {
    private final Follower f;

    public final Pose startPos = Globals.BLUE_CUBE_START;
    public final Pose shoot0Pos = new Pose(49, 66, Math.toRadians(180));
    public final Pose intakeMidHoldPos = new Pose(44, 41);
    public final Pose intakeMidPos = new Pose(2, 41, Math.toRadians(180));

    public final Pose shoot1HoldPos = new Pose(44, 44);
    public final Pose shoot1Pos = new Pose(40, 66, Math.toRadians(139));
    public final Pose intakeGate1HoldPos = new Pose(9, 43);
    public final Pose intakeGate1Pos = new Pose(-3, 46, Math.toRadians(138));
    public final Pose intakeGate1InterPos = new Pose(-3.13, 42, Math.toRadians(124));
    public final Pose intakeGate1ForPos = new Pose(-3, 45, Math.toRadians(133));

    public final Pose shoot2HoldPos = new Pose(44, 44);
    public final Pose shoot2Pos = new Pose(40, 66, Math.toRadians(139));
    public final Pose intakeGate2HoldPos = new Pose(9, 43);
    public final Pose intakeGate2Pos = new Pose(-3, 46, Math.toRadians(138));
    public final Pose intakeGate2InterPos = new Pose(-3.13, 42, Math.toRadians(124));
    public final Pose intakeGate2ForPos = new Pose(-3, 45, Math.toRadians(133));

    public final Pose shoot3HoldPos = new Pose(44, 44);
    public final Pose shoot3Pos = new Pose(50, 68, Math.toRadians(180));
    public final Pose intakeFarPos = new Pose(14, 65, Math.toRadians(180));
    public final Pose shoot4Pos = new Pose(45, 68, Math.toRadians(180));
    public final Pose intakeCloseHoldPos = new Pose(50, 25.65273775216138);
    public final Pose intakeClosePos = new Pose(10, 18, Math.toRadians(180));
    public final Pose shoot5Pos = new Pose(54, 104);


    private int index;

    public B18Paths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0intake0AndGetBack() { //score preloads + get to intake position and comes back
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                startPos,
                                shoot0Pos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shoot0Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.3, () -> f.setMaxPower(0.35))
                .addParametricCallback(0.6, () -> f.setMaxPower(1))
                .addPath(
                        new BezierCurve(
                                shoot0Pos,
                                intakeMidHoldPos,
                                intakeMidPos
                        )
                ).setLinearHeadingInterpolation(shoot0Pos.getHeading(), intakeMidPos.getHeading())
                .addParametricCallback(0.6, () -> f.setMaxPower(0.3))
                .addPath(
                        new BezierCurve(
                                intakeMidPos,
                                shoot1HoldPos,
                                shoot1Pos
                        )
                )
                .setLinearHeadingInterpolation(intakeMidPos.getHeading(), shoot1Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain gate1() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot1Pos,
                                intakeGate1HoldPos,
                                intakeGate1Pos
                        )
                ).setLinearHeadingInterpolation(shoot1Pos.getHeading(), intakeGate1Pos.getHeading())
                .addParametricCallback(0.9, () -> f.setMaxPower(0.3))
                .addPath(
                        new BezierLine(
                                intakeGate1Pos,
                                intakeGate1InterPos
                        )
                )
                .setLinearHeadingInterpolation(intakeGate1Pos.getHeading(), intakeGate1InterPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(0.8))
                .addPath(
                        new BezierLine(
                                intakeGate1InterPos,
                                intakeGate1ForPos
                        )
                )
                .setLinearHeadingInterpolation(intakeGate1InterPos.getHeading(), intakeGate1ForPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(0.8))
                .build();
    }

    public PathChain shoot2() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                intakeGate1Pos,
                                shoot2HoldPos,
                                shoot2Pos
                        )
                ).setLinearHeadingInterpolation(intakeGate1Pos.getHeading(), shoot2Pos.getHeading())
                .build();
    }

    public PathChain gate2() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot2Pos,
                                intakeGate2HoldPos,
                                intakeGate2Pos
                        )
                ).setLinearHeadingInterpolation(shoot2Pos.getHeading(), intakeGate2Pos.getHeading())
                .addParametricCallback(0.8, () -> f.setMaxPower(0.3))
                .addPath(
                        new BezierLine(
                                intakeGate2Pos,
                                intakeGate2InterPos
                        )
                )
                .setLinearHeadingInterpolation(intakeGate2Pos.getHeading(), intakeGate2InterPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(0.8))
                .addPath(
                        new BezierLine(
                                intakeGate2InterPos,
                                intakeGate2ForPos
                        )
                )
                .setLinearHeadingInterpolation(intakeGate2InterPos.getHeading(), intakeGate2ForPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(0.8))
                .build();
    }

    public PathChain shoot3() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                intakeGate2Pos,
                                shoot3HoldPos,
                                shoot3Pos
                        )
                ).setLinearHeadingInterpolation(intakeGate2Pos.getHeading(), shoot3Pos.getHeading())
                .build();
    }

    public PathChain intakeFar() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot3Pos,
                                intakeFarPos
                        )
                ).setLinearHeadingInterpolation(shoot3Pos.getHeading(), intakeFarPos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.3, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeFarPos,
                                shoot4Pos
                        )
                ).setLinearHeadingInterpolation(intakeFarPos.getHeading(), shoot4Pos.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .build();
    }

    public PathChain intakeClose() { //score the last ramp balls
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot4Pos,
                                intakeCloseHoldPos,
                                intakeClosePos
                        )
                ).setLinearHeadingInterpolation(shoot4Pos.getHeading(), intakeClosePos.getHeading())
                .addParametricCallback(0.6, () -> f.setMaxPower(0.5))
                .addPath(
                        new BezierLine(
                                intakeClosePos,
                                shoot5Pos
                        )
                ).setTangentHeadingInterpolation()
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setReversed()
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return score0intake0AndGetBack();
            case 1: return gate1();
            case 2: return shoot2();
            case 3: return gate2();
            case 4: return shoot3();
            case 5: return intakeFar();
            case 6: return intakeClose();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}