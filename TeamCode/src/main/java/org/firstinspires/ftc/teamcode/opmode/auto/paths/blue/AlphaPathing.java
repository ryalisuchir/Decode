package org.firstinspires.ftc.teamcode.opmode.auto.paths.blue;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

public class AlphaPathing {
    private final Follower f;
    private final Globals.Alliance side;

    public Pose startPos, leavePos;
    private int index;

    public AlphaPathing(Halo r) {
        this.f = r.dt.getFollower();
        this.side = Globals.alliance;

        startPos = alliancePose(new Pose(89.61383285302594, 9.037463976945247, 0));

       leavePos = alliancePose(new Pose(89.82132564841497, 33.30259365994236, 0));

        index = 0;
    }

    public PathChain leave() {
        return f.pathBuilder().addPath(
                        new BezierLine(
                                startPos,
                                leavePos
                        )
                ).setLinearHeadingInterpolation(startPos.getHeading(), leavePos.getHeading())

                .build();
    }


    public PathChain next() {
        switch (index++) {
            case 0: return leave();
            default: return null;
        }
    }

    public void reset() {
        index = 0;
    }

    private Pose alliancePose(Pose pose) {
        return side == Globals.Alliance.BLUE ? pose.mirror() : pose;
    }
}
