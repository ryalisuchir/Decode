package org.firstinspires.ftc.teamcode.common.commandbase.commands.utility;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.utility.Halo;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathChain;

public class FollowPathCmd extends CommandBase {
    private final Follower follower;
    private final PathChain path;
    private boolean holdEnd = true;
    private double maxPower = 1;
    private double completionThreshold = 0.99;

    private boolean enableStallDetection = false;
    private double minDeltaT = 0.02;
    private long stallTimeoutMs = 2000;

    private double lastT;
    private long lastProgressTime;

    public FollowPathCmd(Halo r, PathChain pathChain) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
    }

    public FollowPathCmd(Halo r, PathChain pathChain, double maxPower) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
        this.maxPower = maxPower;
    }

    public FollowPathCmd(Halo r, PathChain pathChain, boolean holdEnd) {
        this.follower = r.dt.getFollower();
        this.path = pathChain;
        this.holdEnd = holdEnd;
    }

    public FollowPathCmd withStallTimeout(double minDeltaT, long timeoutMs) {
        this.enableStallDetection = true;
        this.minDeltaT = minDeltaT;
        this.stallTimeoutMs = timeoutMs;
        return this;
    }

    public FollowPathCmd(Halo r, PathChain pathChain, boolean holdEnd, double maxPower) {
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

        lastT = follower.getCurrentTValue();
        lastProgressTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        if (!enableStallDetection) return;

        double currentT = follower.getCurrentTValue();
        long now = System.currentTimeMillis();

        if (currentT - lastT > minDeltaT) {
            lastT = currentT;
            lastProgressTime = now;
        }
    }

    @Override
    public boolean isFinished() {
        if (follower.getCurrentTValue() >= completionThreshold) {
            return true;
        }

        // stall condition
        if (enableStallDetection) {
            long now = System.currentTimeMillis();
            if (now - lastProgressTime > stallTimeoutMs) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        follower.setMaxPower(1);
        follower.breakFollowing();
    }
}