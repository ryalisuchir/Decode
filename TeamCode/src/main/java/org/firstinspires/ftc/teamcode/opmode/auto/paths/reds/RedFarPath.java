package org.firstinspires.ftc.teamcode.opmode.auto.paths.reds;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierCurve;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierLine;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class RedFarPath {
    private final Follower follower;
    private final G.Side side;

    public static Pose startPos, shootPos, intakeSpikeHoldPos, intakeSpikePos, intakeHpControlPos, intakeHpPos, intakeHpMidPos, intakeHpMidderPos, wiggle1, intakeSweep1, intakeSweep2, park;

    public RedFarPath(Halo r) {
        this.follower = r.dt.getFollower();
        this.side = G.side;

        startPos = G.RED_FAR_START;
        shootPos = alliancePose(new Pose(88.847, 17.911, Math.toRadians(8)));

        wiggle1 = alliancePose(new Pose(88.847, 17.911, Math.toRadians(-15)));

        intakeSpikeHoldPos = alliancePose(new Pose(103.06051873198848, 41.20749279538906));
        intakeSpikePos = alliancePose(new Pose(134.78962536023056, 38.50432276657062, Math.toRadians(0)));

        intakeHpControlPos = alliancePose(new Pose(113.303, 10.568));
        intakeHpPos = alliancePose(new Pose(135, 10.686, Math.toRadians(0)));

        intakeHpMidPos = alliancePose(new Pose(119.14094812680115, 12.308694524495676, Math.toRadians(8)));
        intakeHpMidderPos = alliancePose(new Pose(102.70317002881845, 16.028818443804035, Math.toRadians(-8)));

        intakeSweep1 = alliancePose(new Pose(127, 43.469740634005746, Math.toRadians(-8)));
        intakeSweep2 = alliancePose(new Pose(132.05475504322766, 13.158501440922175, Math.toRadians(-20)));
        park = alliancePose(new Pose(114.87031700288189, 45.76657060518731, Math.toRadians(0)));
    }

    public PathChain shoot0() {
        return follower.pathBuilder().addPath(
                new BezierLine(
                        startPos,
                        shootPos
                )
        ).setLinearHeadingInterpolation(startPos.getHeading(), shootPos.getHeading())
        .build();
    }

    public PathChain intakeSpikeAndShoot() {
        return follower.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                intakeSpikeHoldPos,
                                intakeSpikePos
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), intakeSpikePos.getHeading())
                .addPath(
                new BezierLine(
                        intakeSpikePos,
                        shootPos
                )
        ).setLinearHeadingInterpolation(intakeSpikePos.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain intakeHp() {
        return follower.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                intakeHpControlPos,
                                intakeHpPos
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), intakeHpPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpPos,
                                intakeHpMidPos
                        )
                ).setLinearHeadingInterpolation(intakeHpPos.getHeading(), intakeHpMidPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpMidPos,
                                intakeHpMidderPos
                        )
                ).setLinearHeadingInterpolation(intakeHpMidPos.getHeading(), intakeHpMidderPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpMidderPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeHpMidderPos.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain intakeSweepAndShoot() {
        return follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                shootPos,
                                intakeSweep1
                        )
        ).setLinearHeadingInterpolation(shootPos.getHeading(), intakeSweep1.getHeading())
                .addPath(
                        new BezierLine(
                                intakeSweep1,
                                intakeSweep2
                        )
                ).setLinearHeadingInterpolation(intakeSweep1.getHeading(), intakeSweep2.getHeading())
                .addPath(
                        new BezierLine(
                                intakeSweep2,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeSweep2.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain park() {
        return follower.pathBuilder().addPath(
                        new BezierLine(
                                shootPos,
                                park
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), park.getHeading())
                .build();
    }

    private Pose alliancePose(Pose pose) {
        return side == G.Side.BLUE ? pose.mirror() : pose;
    }
}
