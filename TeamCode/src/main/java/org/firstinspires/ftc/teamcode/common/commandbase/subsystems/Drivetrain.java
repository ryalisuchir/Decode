package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.utility.C;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.BezierPoint;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.math.Vector;

public class Drivetrain extends SubsystemBase {
    private final Follower f;
    public final G.Side a;
    private boolean hold = false, field = true;

    public Drivetrain(HardwareMap hardwareMap, G.Side a, Pose start) {
        f = C.createFollower(hardwareMap);
        f.setStartingPose(start);
        this.a = a;
    }

    public void startDrive() {
        f.startTeleopDrive();
    }

    public void resetDrive() {
        if (a.equals(G.Side.BLUE)) {
            f.setPose(G.BLUE_CUBE_START);
        } else {
            f.setPose(G.RED_CUBE_START);
        }
    }

    public InstantCommand reset() { return new InstantCommand(this::resetDrive); }

    public void loop() {
        f.update();
    }

    public double getGoalDistance() {
        Vector2d goal = (a == G.Side.BLUE)
                ? G.BLUE_CASTLE
                : G.RED_CASTLE;

        return TurretMath.getDistanceToGoalPinpoint(f, goal.getX(), goal.getY());
    }

    public Vector getGoalVector() {
        Vector2d goal = (a == G.Side.BLUE)
                ? G.BLUE_CASTLE
                : G.RED_CASTLE;

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
        if (a.equals(G.Side.BLUE)) {
            f.setPose(G.BLUE_CUBE_START);
        }

        if (a.equals(G.Side.RED)) {
            f.setPose(G.RED_CUBE_START);
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