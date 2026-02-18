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

    public static Pose startPos, shootPos, intakeSpikeHoldPos, intakeSpikePos, intakeHpControlPos, intakeHpPos, intakeHpBackPos;

    public RedFarPath(Halo r) {
        this.follower = r.dt.getFollower();
        this.side = G.side;

        startPos = G.RED_FAR_START;
        shootPos = alliancePose(new Pose(88.847, 17.911, Math.toRadians(0)));
        intakeSpikeHoldPos = alliancePose(new Pose(103.68299711815563, 37.26512968299712));
        intakeSpikePos = alliancePose(new Pose(134.78962536023056, 36.63688760806917, Math.toRadians(0)));
        intakeHpControlPos = alliancePose(new Pose(113.303, 10.568));
        intakeHpPos = alliancePose(new Pose(135, 10.686));
        intakeHpBackPos = alliancePose(new Pose(127, 10.686));
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
                ).setTangentHeadingInterpolation()
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
                ).setLinearHeadingInterpolation(intakeHpPos.getHeading(), shootPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpPos,
                                intakeHpBackPos
                        )
                ).setLinearHeadingInterpolation(intakeHpPos.getHeading(), shootPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpBackPos,
                                intakeHpPos
                        )
                ).setLinearHeadingInterpolation(intakeHpPos.getHeading(), shootPos.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHpPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeHpPos.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain cameraIntakePath(double blend) {
        Pose intake = intakePoseForBlend(blend);
        Pose control = controlPoseForBlend(blend);
        return follower.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                control,
                                intake
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), intake.getHeading())
                .build();
    }

    public PathChain cameraShootPath(double blend) {
        Pose source = intakePoseForBlend(blend);
        Pose control = controlPoseForBlend(blend);
        return follower.pathBuilder().addPath(
                        new BezierCurve(
                                source,
                                control,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(source.getHeading(), shootPos.getHeading())
                .build();
    }

    private Pose intakePoseForBlend(double blend) {
        return interpolateThree(
                alliancePose(CameraConfig.Y_LEFT2_POSE),
                alliancePose(CameraConfig.Y_CENTER_POSE),
                alliancePose(CameraConfig.Y_RIGHT2_POSE),
                clamp01(blend)
        );
    }

    private Pose controlPoseForBlend(double blend) {
        return interpolateThree(
                alliancePose(CameraConfig.Y_LEFT2_CONTROL_POSE),
                alliancePose(CameraConfig.Y_CENTER_CONTROL_POSE),
                alliancePose(CameraConfig.Y_RIGHT2_CONTROL_POSE),
                clamp01(blend)
        );
    }

    private Pose interpolateThree(Pose left, Pose center, Pose right, double blend) {
        if (blend <= 0.5) {
            return lerpPose(left, center, blend * 2.0);
        }
        return lerpPose(center, right, (blend - 0.5) * 2.0);
    }

    private Pose lerpPose(Pose a, Pose b, double t) {
        double clamped = clamp01(t);
        double x = lerp(a.getX(), b.getX(), clamped);
        double y = lerp(a.getY(), b.getY(), clamped);
        double heading = lerp(a.getHeading(), b.getHeading(), clamped);
        return new Pose(x, y, heading);
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private Pose alliancePose(Pose pose) {
        return side == G.Side.BLUE ? pose.mirror() : pose;
    }
}
