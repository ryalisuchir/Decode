package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.pathing.Constants;
import org.firstinspires.ftc.teamcode.common.utility.turret.TurretMath;

public class Drivetrain extends SubsystemBase {
    private final Follower f;
    public final Globals.Alliance a;
    private boolean hold = false, field = true;

    public Drivetrain(HardwareMap hardwareMap, Globals.Alliance a, Pose start) {
        f = Constants.createFollower(hardwareMap);
        f.setStartingPose(start);
        this.a = a;
    }

    public void startDrive() {
        f.startTeleopDrive();
    }

    public void resetDrive() {
        if (a.equals(Globals.Alliance.BLUE)) {
            f.setPose(Globals.Positions.BLUE_CUBE_START);
        } else {
            f.setPose(Globals.Positions.RED_CUBE_START);
        }
    }

    public InstantCommand reset() { return new InstantCommand(this::resetDrive); }

    public void loop() {
        f.update();
    }

    public double getGoalDistance() {
        Pose goal = (a == Globals.Alliance.BLUE)
                ? Globals.Positions.BLUE_GOAL
                : Globals.Positions.RED_GOAL;

        return TurretMath.getDistanceToGoalPinpoint(f, goal.getX(), goal.getY());
    }

    public Pose getGoalVector() {
        Pose goal = (a == Globals.Alliance.BLUE)
                ? Globals.Positions.BLUE_GOAL
                : Globals.Positions.RED_GOAL;

        return TurretMath.getVectorToGoalPinpoint(f, goal.getX(), goal.getY());
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

    public void resetPosition() {
        if (a.equals(Globals.Alliance.BLUE)) {
            f.setPose(Globals.Positions.BLUE_CUBE_START);
        } else {
            f.setPose(Globals.Positions.RED_CUBE_START);
        }
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
    public InstantCommand resetPose() {
        return new InstantCommand(this::resetPosition);
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