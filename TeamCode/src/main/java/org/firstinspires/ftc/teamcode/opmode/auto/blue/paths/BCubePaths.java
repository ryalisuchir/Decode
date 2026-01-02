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

    public Pose start = new Pose(31.954, 135.908, Math.toRadians(-90));
    public Pose shoot0 = new Pose(62, 73, Math.toRadians(180));
    public Pose intakeMidHold1 = new Pose(55, 56);
    public Pose intakeMid = new Pose(24, 50, Math.toRadians(180));
    public Pose shoot1 = new Pose(63, 74, Math.toRadians(139));
    public Pose intakeRamp1Hold1 = new Pose(43, 49);
    public Pose intakeRamp1 = new Pose(10, 56, Math.toRadians(149));
    public Pose shoot2 = new Pose(62, 75, Math.toRadians(139));
    public Pose intakeRamp2Hold1 = new Pose(43, 49);
    public Pose intakeRamp2 = new Pose(10, 56, Math.toRadians(149));
    public Pose shoot3 = new Pose(62, 75, Math.toRadians(139));
    public Pose intakeRamp3Hold1 = new Pose(43, 49);
    public Pose intakeRamp3 = new Pose(10, 56, Math.toRadians(149));
    public Pose shoot4Hold1 = new Pose(57.476, 67.643);
    public Pose shoot4 = new Pose(59, 76, Math.toRadians(180));
    public Pose intakeRight = new Pose(25, 77, Math.toRadians(180));
    public Pose shoot5 = new Pose(48, 78, Math.toRadians(235));
    public Pose park = new Pose(42, 78, Math.toRadians(270));

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
                .addParametricCallback(0, () -> f.setMaxPower(0.4))
                .setVelocityConstraint(25)
                .setHeadingConstraint(Math.toRadians(15))
                .addPath(
                        new BezierCurve(
                                shoot0,
                                intakeMidHold1,
                                intakeMid
                        )
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(shoot0.getHeading(), intakeMid.getHeading())
                .build();
    }

    public PathChain score1() { //score mid spike
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeMid, shoot1)
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
                                intakeRamp1
                        )
                )
                .addParametricCallback(0.6, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot1.getHeading(), intakeRamp1.getHeading())
                .build();
    }

    public PathChain score2() { //score the balls from the ramp
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeRamp1, shoot2)
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
                                intakeRamp2
                        )
                )
                .addParametricCallback(0.6, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot2.getHeading(), intakeRamp2.getHeading())
                .build();
    }

    public PathChain score3() {
        return f.pathBuilder()
                .addPath(
                        new BezierLine(intakeRamp2, shoot3)
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
                                intakeRamp3Hold1,
                                intakeRamp3
                        )
                )
                .addParametricCallback(0.6, () -> f.setMaxPower(0.4))
                .setLinearHeadingInterpolation(shoot3.getHeading(), intakeRamp3.getHeading())
                .build();
    }

    public PathChain score4() { //score the last ramp balls
        return f.pathBuilder()
                .addPath(
                        new BezierCurve(
                                intakeRamp3,
                                shoot4Hold1,
                                shoot4
                        )
                )
                .addParametricCallback(0, () -> f.setMaxPower(1))
                .setLinearHeadingInterpolation(intakeRamp3.getHeading(), shoot4.getHeading())
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
