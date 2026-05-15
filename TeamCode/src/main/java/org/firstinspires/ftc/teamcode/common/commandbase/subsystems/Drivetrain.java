package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.control.PredictiveBrakingController;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.pathing.Constants;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;

public class Drivetrain extends SubsystemBase {
    private final Follower f;
    public final Globals.Alliance a;
    private boolean hold = false, field = true;

    private PredictiveBrakingController brakingController;
    private Pose holdTarget = null;
    private static final double STICK_DEADZONE = 0.05;
    private static final double CURVE = 1.12;
    private static final double SCALE = 0.5;

    public Drivetrain(HardwareMap hardwareMap, Globals.Alliance a, Globals.Match m, Pose start) {
        if (m == Globals.Match.TELEOP || m == Globals.Match.TESTING) {
            f = Constants.createFollower2(hardwareMap);
        } else {
            f = Constants.createFollower(hardwareMap);
        }
        f.setStartingPose(start);
        this.a = a;

        brakingController = new PredictiveBrakingController(
                new PredictiveBrakingCoefficients(0.01, 0.06864131504203407, 0.001492430041070731)
        );
    }

    public void startDrive() {
        f.startTeleopDrive();
    }

    private double curve(double raw) {
        return SCALE * Math.tan(CURVE * raw);
    }

    private boolean driverCommanding(Gamepad g) {
        return Math.abs(g.left_stick_x) > STICK_DEADZONE
                || Math.abs(g.left_stick_y) > STICK_DEADZONE;
    }

    public void loop() {
        f.update();
    }

    public double getGoalDistance() {
        Pose goal = (a == Globals.Alliance.BLUE)
                ? Globals.Positions.BLUE_GOAL
                : Globals.Positions.RED_GOAL;

        return 0;
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

    public void predictiveDrive(Gamepad g) {
        if (hold) return;

        double forward = curve(-g.left_stick_y);
        double strafe = curve(-g.left_stick_x);
        double turn = curve(-g.right_stick_x);

        if (driverCommanding(g)) {
            if (holdTarget != null) {
                holdTarget = null;
                f.startTeleopDrive(); // break out of holdPoint mode
            }
            f.setTeleOpDrive(forward, strafe, turn, true);
            return;
        }

        Vector velocity = f.getVelocity();
        double velMag = velocity.getMagnitude();

        if (velMag > 0.01) {
            double brakingPower = brakingController.computeBrakingDisplacement(velMag, Math.signum(velMag));
            Vector correctionVec = velocity.normalize().times(-brakingPower);
            f.setTeleOpDrive(
                    correctionVec.getXComponent(),
                    correctionVec.getYComponent(),
                    turn,
                    true
            );
        } else {
            if (holdTarget == null) {
                holdTarget = f.getPose();
            }
            // Re-call holdPoint every tick with current heading so turn input isn't fought
            f.holdPoint(new BezierPoint(holdTarget), f.getHeading(), true);
        }
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

//        if (a.equals(Globals.Alliance.BLUE)) { //new reset position
//            f.setPose(new Pose(16.16138328530257, 79.79250720461093, 180));
//        } else {
//            f.setPose(new Pose(128.4149855907781, 79.79250720461093, 0));
//        }
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
