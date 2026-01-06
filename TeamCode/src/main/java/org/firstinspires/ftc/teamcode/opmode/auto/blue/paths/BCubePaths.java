package org.firstinspires.ftc.teamcode.opmode.auto.blue.paths;

import android.util.Log;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class BCubePaths {
    private final Follower f;

    public Pose start = new Pose(60, 137, Math.toRadians(-90));
    public Pose shoot0 = new Pose(67, 68, Math.toRadians(180));
    public Pose intakeMidHold1 = new Pose(60, 45);
    public Pose intakeMid = new Pose(23, 46, Math.toRadians(180));
    public Pose shoot1Hold = new Pose(42.500, 50);
    public Pose shoot1 = new Pose(61, 72, Math.toRadians(138));
    public Pose intakeRamp1Hold1 = new Pose(43.000, 49.000);
    public Pose intakeRampI1 = new Pose(18, 48, Math.toRadians(126));
    public Pose intakeRamp1 = new Pose(18, 51.5, Math.toRadians(145));
    public Pose intakeRampBack1 = new Pose(18, 48, Math.toRadians(126));
    public Pose shoot2Hold = new Pose(42.500, 50);
    public Pose shoot2 = new Pose(61, 72, Math.toRadians(138));
    public Pose intakeRamp2Hold1 = new Pose(43.000, 49.000);
    public Pose intakeRampI2 = new Pose(18, 48, Math.toRadians(126));
    public Pose intakeRamp2 = new Pose(18, 51.5, Math.toRadians(145));
    public Pose intakeRampBack2 = new Pose(18, 48, Math.toRadians(126));
    public Pose shoot3Hold = new Pose(42.500, 50);
    public Pose shoot3 = new Pose(61, 72, Math.toRadians(180));
    public Pose intakeCloseHold = new Pose(70, 20);
    public Pose intakeClose = new Pose(24, 23, Math.toRadians(180));
    public Pose shoot4Hold1 = new Pose(42.500, 55.228);
    public Pose shoot4 = new Pose(67, 73, Math.toRadians(180));
    public Pose intakeRight = new Pose(33, 72, Math.toRadians(180));
    public Pose shoot5 = new Pose(63, 75, Math.toRadians(180));
    public Pose park = new Pose(50, 79, Math.toRadians(-90));

    private int index;

    public BCubePaths(Robot r) {
        this.f = r.dt.getFollower();
        index = 0;
    }

    public PathChain score0intake0() { //score preloads + get to intake position
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                start,
                                shoot0
                        )
                )
                .setLinearHeadingInterpolation(start.getHeading(), shoot0.getHeading())
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .addParametricCallback(0.3, () -> f.setMaxPower(0.4))
                .addParametricCallback(0.6, () -> f.setMaxPower(1))
                .addPath(
                        new BezierCurve(
                                shoot0,
                                intakeMidHold1,
                                intakeMid
                        )
                )
                .addParametricCallback(0.6, () -> f.setMaxPower(0.5))
                .addParametricCallback(0.8, () -> f.setMaxPower(0.3))
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeMid.getHeading())
                .build();
    }

    public PathChain score1() { //score mid spike
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(intakeMid, shoot1Hold, shoot1)
                )
                .setLinearHeadingInterpolation(intakeMid.getHeading(), shoot1.getHeading())
                .build();
    }

    public PathChain intake1() { //intake from ramp go 1
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot1,
                                intakeRamp1Hold1,
                                intakeRampI1
                        )
                )
                .addParametricCallback(0.75, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeRampI1.getHeading())
                .addPath(
                        new BezierLine(
                                intakeRampI1,
                                intakeRamp1
                        )
                )
                .addParametricCallback(0.75, () -> f.setMaxPower(0.4))
                .setTValueConstraint(0.75)
                .setLinearHeadingInterpolation(intakeRampI1.getHeading(), intakeRamp1.getHeading())
                .addPath(
                        new BezierLine(
                                intakeRamp1,
                                intakeRampBack1
                        )
                )
                .addParametricCallback(0, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(intakeRamp1.getHeading(), intakeRampBack1.getHeading())
                .build();
    }

    public PathChain score2() { //score the balls from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(intakeRamp1, shoot2Hold, shoot2)
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeRamp1.getHeading(), shoot2.getHeading())
                .build();
    }

    public PathChain intake2() { //second go at intaking from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot2,
                                intakeRamp2Hold1,
                                intakeRampI2
                        )
                )
                .addParametricCallback(0.75, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot2.getHeading(), intakeRampI2.getHeading())
                .addPath(
                        new BezierLine(
                                intakeRampI2,
                                intakeRamp2
                        )
                )
                .setTValueConstraint(0.65)
                .addParametricCallback(0, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(intakeRampI2.getHeading(), intakeRamp2.getHeading())
                .addPath(
                        new BezierLine(
                                intakeRamp2,
                                intakeRampBack2
                        )
                )
                .setLinearHeadingInterpolation(intakeRamp2.getHeading(), intakeRampBack2.getHeading())
                .build();
    }

    public PathChain score3() {
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(intakeRamp2, shoot3Hold, shoot3)
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeRamp2.getHeading(), shoot3.getHeading())
                .build();
    }

    public PathChain intake3() { //third go at intaking from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                shoot3,
                                intakeCloseHold,
                                intakeClose
                        )
                )
                .addParametricCallback(0.6, () -> f.setMaxPower(0.6))
                .addParametricCallback(0.8, () -> f.setMaxPower(0.3))
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeClose.getHeading())
                .build();
    }

    public PathChain score4() { //score the last ramp balls
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                intakeClose,
                                shoot4Hold1,
                                shoot4
                        )
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeClose.getHeading(), shoot4.getHeading())
                .build();
    }

    public PathChain intake4() { //intake far spike mark
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot4,
                                intakeRight
                        )
                )
                .setLinearHeadingInterpolation(shoot4.getHeading(), intakeRight.getHeading())
                .build();
    }

    public PathChain score5() { //scores the last spike mark
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                intakeRight,
                                shoot5
                        )
                )
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();
    }

    public PathChain parkX() { //scores the last spike mark
        return f.pathBuilder()
                .addPath(
                        new BezierLine(
                                shoot5,
                                park
                        )
                )
                .setLinearHeadingInterpolation(shoot5.getHeading(), park.getHeading())
                .build();
    }

    public PathChain next() {
        switch (index++) {
            case 0: return score0intake0();
            case 1: return score1();
            case 2: return intake1();
            case 3: return score2();
            case 4: return intake2();
            case 5: return score3();
            case 6: return intake3();
            case 7: return score4();
            case 8: return intake4();
            case 9: return score5();
            case 10: return parkX();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }
}
