package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import kotlin.time.Instant;

public class Drivetrain {
    private final Follower f;
    private final Globals.Side a;
    private boolean hold = false, field = true;

    public Drivetrain(HardwareMap hardwareMap, Globals.Side a, Pose start) {
        f = Constants.createFollower(hardwareMap);
        f.setStartingPose(start);
        this.a = a;
    }

    public double getGoalDistance() {
        Vector2d goal = (a == Globals.Side.BLUE)
                ? Globals.BLUE_CASTLE
                : Globals.RED_CASTLE;

        return TurretMath.getDistanceToGoalPinpoint(f, goal.getX(), goal.getY());
    }

    public void startDrive() {
        f.startTeleopDrive();
    }

    public void resetDrive() {
        if (a.equals(Globals.Side.BLUE)) {
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)).mirror());
        } else {
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)));
        }
    }

    public InstantCommand reset() { return new InstantCommand(this::resetDrive); }

    public void loop() {
        f.update();
    }

    public void drive(Gamepad g) {
        if (!hold)
            f.setTeleOpDrive(
                    -0.5 * Math.tan(1.12 * g.left_stick_y),
                    -0.5 * Math.tan(1.12 * g.left_stick_x),
                    -0.5 * Math.tan(1.12 * g.right_stick_x),
                    true
            );
    }

    public void holdCurrent() {
        f.holdPoint(new BezierPoint(f.getPose()), f.getHeading(), true);
        hold = true;
    }

    public void releaseHold() {
        hold = false;
    }

    public void teleToggleCentric() {
        field = !field;
    }

    public void cornerReset() {
        if (a.equals(Globals.Side.BLUE))
            f.setPose(Globals.DEFAULT_START_POSE);
        else
            f.setPose(Globals.DEFAULT_START_POSE.mirror());
    }

    public InstantCommand toggleCentric() {
        return new InstantCommand(this::teleToggleCentric);
    }

    public InstantCommand hold() {
        return new InstantCommand(this::holdCurrent);
    }

    public InstantCommand release() {
        return new InstantCommand(this::releaseHold);
    }

    public InstantCommand corner() {
        return new InstantCommand(this::cornerReset);
    }

    public void setStart(Pose start) {
        f.setStartingPose(start);
    }

    public Pose getPose() {
        return f.getPose();
    }

    public Pose isBusy() {
        return f.getPose();
    }

    public double getT() {
        return f.getCurrentTValue();
    }

    public Follower getFollower() { return f;}
}