package org.firstinspires.ftc.teamcode.common.commandbase.commands.utility;


import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.utility.Robot;

public class FollowPathCmd extends CommandBase {
    private final Follower follower;
    private final PathChain path;
    private boolean holdEnd = true;
    private double maxPower = 1;
    private double completionThreshold = 0.99;

    public FollowPathCmd(Robot r, PathChain pathChain) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
    }

    public FollowPathCmd(Robot r, PathChain pathChain, double maxPower) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
        this.maxPower = maxPower;
    }

    public FollowPathCmd(Robot r, PathChain pathChain, boolean holdEnd) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
        this.holdEnd = holdEnd;
    }

    public FollowPathCmd(Robot r, PathChain pathChain, boolean holdEnd, double maxPower) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
        this.holdEnd = holdEnd;
        this.maxPower = maxPower;
    }

    /**
     * Decides whether or not to make the robot maintain its position once the path ends.
     *
     * @param holdEnd If the robot should maintain its ending position
     * @return This command for compatibility in command groups
     */
    public FollowPathCmd setHoldEnd(boolean holdEnd) {
        this.holdEnd = holdEnd;
        return this;
    }

    /**
     * Sets the follower's maximum power
     * @param power Between 0 and 1
     * @return This command for compatibility in command groups
     */
    public FollowPathCmd setMaxPower(double power) {
        this.maxPower = power;
        return this;
    }

    /**
     * Sets the T-value at which the follower will consider the path complete
     * @param t Between 0 and 1
     * @return This command for compatibility in command groups
     */
    public FollowPathCmd setCompletionThreshold(double t) {
        this.completionThreshold = t;
        return this;
    }

    @Override
    public void initialize() {
        follower.setMaxPower(this.maxPower);
        follower.followPath(path, holdEnd);
    }

    @Override
    public boolean isFinished() {
        return follower.atParametricEnd();
    }

    @Override
    public void end(boolean interrupted) {
        follower.setMaxPower(1);
    }
}