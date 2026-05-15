package org.firstinspires.ftc.teamcode.opmode.auto.paths.blue;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class DBZPathing {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, leavePos, shootPos, intake3, intake3C, intakeHpC, intakeHp, sweep, sweepC;
    private int index;

    public DBZPathing(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = Globals.Positions.ALPHA_START_BLUE;

        shootPos = alliancePose(new Pose(55.37752161383283, 20.64553314121037, Math.toRadians(180)));
        intake3 = alliancePose(new Pose(12.233429394812676, 33, Math.toRadians(180)));
        intake3C = alliancePose(new Pose(52.30979827089336, 33, Math.toRadians(180)));

        sweep = alliancePose(new Pose(11.979827089337174, 36.56772334293946, Math.toRadians(110)));
        sweepC = alliancePose(new Pose(26.88040345821326, 10.756484149855899, Math.toRadians(180)));

        intakeHp = alliancePose(new Pose(9.057636887608071, 8, Math.toRadians(180)));
        intakeHpC = alliancePose(new Pose(36.05619596541787, 8, Math.toRadians(180)));

        leavePos = alliancePose(new Pose(55.60806916426513, 33.30259365994236, Math.toRadians(180)));

        index = 0;
    }

    public PathChain shoot0() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain intake3P() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                intake3C,
                                intake3
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                intake3,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intake3.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain hpP() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                intakeHpC,
                                intakeHp
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), intakeHp.getHeading())
                .addPath(
                        new BezierLine(
                                intakeHp,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(intakeHp.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain sweepP() {
        return f.pathBuilder().addPath(
                        new BezierCurve(
                                shootPos,
                                sweepC,
                                sweep
                        )
                ).setTangentHeadingInterpolation()
                .addPath(
                        new BezierLine(
                                sweep,
                                shootPos
                        )
                ).setLinearHeadingInterpolation(sweep.getHeading(), shootPos.getHeading())
                .build();
    }

    public PathChain gtfo() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                shootPos,
                                leavePos
                        )
                ).setLinearHeadingInterpolation(shootPos.getHeading(), leavePos.getHeading())
                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return shoot0();
            case 1: return intake3P();
            case 2: return hpP();
            case 3: return sweepP();
            case 4: return sweepP();
            case 5: return hpP();
            case 6: return gtfo();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose alliancePose(Pose pose) {
        return pose;
    }
}
