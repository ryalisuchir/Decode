package org.firstinspires.ftc.teamcode.common;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.common.utility.functions.Range;

@Configurable
@Config
public class Globals {
    public static Globals.Alliance alliance;
    public static Globals.ObeliskOptions obeliskOptions = ObeliskOptions.PPG;
    public static Globals.Match match;
    public static BallColor[] ballColors = new BallColor[3];

    public static boolean shooterKicking = false;
    public static boolean shootOrderLocked = false; // latches after first full color read

    public static Globals.IntakeState intakeState;
    public static Globals.TransferState transferState;
    public static Globals.ShooterState shooterState;
    public static Globals.Kicker1State kicker1State;
    public static Globals.Kicker2State kicker2State;
    public static Globals.Kicker3State kicker3State;
    public static Globals.TurretState turretState;
    public static Globals.PivotState pivotState;
    public static Globals.HoodState hoodState;
    public static Globals.GateState gateState;
    public static Globals.SetShooterState setShooterState;

    //Shooter Constants:
    public static PIDFCoefficients shooterCoefficients = new PIDFCoefficients(0.003, 0.003, 0, 0.00036);

    //Positions:
    public static class Positions {
        public static Pose BLUE_CUBE_START = new Pose(33.45, 134.83, Math.toRadians(-90));
        public static Pose BLUE_EXODUS_START = new Pose(15.83, 113, Math.toRadians(-90));
        public static Pose BLUE_FAR_START = new Pose(56.91, 8.84, Math.toRadians(180));

        public static Pose RED_CUBE_START = new Pose(110.85, 134.83, Math.toRadians(-90));
        public static Pose RED_EXODUS_START = new Pose(128.5, 113, Math.toRadians(-90));
        public static Pose RED_FAR_START = new Pose(87.42, 8.84, Math.toRadians(0));

        public static Pose BLUE_GOAL = new Pose(12.2, 132.5);
        public static Pose RED_GOAL = new Pose(131.5, 132.5);
    }

    public static double GOAL_CENTER_X_BIAS_CLOSE_RED = 8;
    public static double GOAL_CENTER_Y_BIAS_CLOSE_RED = 8;

    public static double GOAL_CENTER_X_BIAS_FAR_RED = 12.5;
    public static double GOAL_CENTER_Y_BIAS_FAR_RED = 12.5;

    public static double GOAL_CENTER_X_BIAS_CLOSE_BLUE = 8;
    public static double GOAL_CENTER_Y_BIAS_CLOSE_BLUE = 8;

    public static double GOAL_CENTER_X_BIAS_FAR_BLUE = 5;
    public static double GOAL_CENTER_Y_BIAS_FAR_BLUE = 5;

    //Servo Positions:

    public static class Kicker {
        public static Range KICKER1 = new Range(0.685, 0.89);
        public static Range KICKER2 = new Range(0.53, 0.72);
        public static Range KICKER3 = new Range(0.32, 0.5);
    }

    public static Range HOOD = new Range(0.32, 1);

    public static class Pivot {
        public static double PIVOT_INTAKE = 0.90;
        public static double PIVOT_RESTING = 0.93;
        public static double PIVOT_RAISED = 0.95;
    }

    public static class Gate {
        public static double GATE_OPEN = 0.41; //intake
        public static double GATE_CLOSED = 0.635;
    }

    public static class Turret {
        public static double TURRET_FORWARD = 0.03;
        public static double TURRET_EXODUS_RESET_BLUE = 0.5;
        public static double TURRET_EXODUS_RESET_RED = 0.5;
        public static double TURRET_FAR_RESET_BLUE = 0.5;
        public static double TURRET_FAR_RESET_RED = 0.5;

        public static double pivotX = 0.0;
        public static double pivotY = 0.0;

        public static boolean TURRET_RED_CLOSE_MODEL_ENABLED = true;
        public static double TURRET_RED_CLOSE_MODEL_MAX_SPEED = 1.0;
        public static double TURRET_RED_CLOSE_MODEL_BLEND = 1.0;
        public static double TURRET_RED_CLOSE_MODEL_MAX_CORRECTION = 0.16;

        public static boolean TURRET_RED_FAR_MODEL_ENABLED = true;
        public static double TURRET_RED_FAR_MODEL_MAX_SPEED = 3;
        public static double TURRET_RED_FAR_MODEL_BLEND = 1.0;
        public static double TURRET_RED_FAR_MODEL_MAX_CORRECTION = 0.16;

        public static boolean TURRET_BLUE_CLOSE_MODEL_ENABLED = true;
        public static double TURRET_BLUE_CLOSE_MODEL_MAX_SPEED = 1.0;
        public static double TURRET_BLUE_CLOSE_MODEL_BLEND = 1.0;
        public static double TURRET_BLUE_CLOSE_MODEL_MAX_CORRECTION = 0.16;

        public static boolean TURRET_BLUE_FAR_MODEL_ENABLED = true;
        public static double TURRET_BLUE_FAR_MODEL_MAX_SPEED = 3;
        public static double TURRET_BLUE_FAR_MODEL_BLEND = 1.0;
        public static double TURRET_BLUE_FAR_MODEL_MAX_CORRECTION = 0.16;
    }

    //Motor Powers:
    public static double MIN_SHOOTER_POWER = 0.35;
    public static double SHOOTER_VEL_TOLERANCE = 100;

    public static class Intake {
        public static double IN_POWER = 1.0;
        public static double OUT_POWER = -0.7;
    }

    public static class Transfer {
        public static double IN_POWER = 0.4;
        public static double OUT_POWER = -1.0;
    }

    //Timings:
    public static class Timings {
        //Kicks:
        public static long KICK_RAPID = 70;
        public static long KICK_FAR = 130;
        public static long KICK_SORT = 100;

        //Wait:
        public static long GATE_WAIT = 150;
    }

    //SOTM:
    public static class SOTM {
        public static boolean SHOOTER_ENABLED = true;
        public static double SHOOTER_RPM_GAIN = 0.2;
        public static double SHOOTER_WHEEL_DIAMETER_MM = 72.0;

        public static boolean TURRET_TOF_COMP_ENABLED = true;
        public static double TURRET_LINEAR_GAIN = 2.2;
        public static double TURRET_ROTATIONAL_GAIN = 1.0;

        public static double TURRET_MIN_SPEED = 1;
    }

    //Color Sensor Values:
    public static final double ANALOG_VREF = 3.3;
    public static Range noColor = new Range(0,115);
    public static Range greenColor = new Range(115,128);
    public static Range purpleColor = new Range(128,170);

    //Enums:
    public enum Alliance {
        RED,
        BLUE
    }

    public enum Match {
        AUTO,
        TELEOP,
        TESTING
    }

    public enum SetShooterState {
        Sorting_Close,
        Regular_Close,
        Last_Close,
        Extremely_Close,
        Far
    }

    public enum ObeliskOptions {
        NOT_FOUND,
        PPG,
        PGP,
        GPP
    }

    public enum BallColor {
        NONE,
        PRESENT,
        G,
        P
    }

    public enum IntakeState {
        INTAKING,
        EJECTING,
        STOPPED
    }

    public enum TransferState {
        TRANSFERRING,
        INTAKING,
        STOPPED
    }

    public enum GateState {
        OPEN,
        CLOSED
    }

    public enum PivotState {
        LOWERED,
        RESTING,
        RAISED
    }

    public enum TurretState {
        FOLLOWING_GOAL,
        FOLLOWING_OBELISK,
        RESET,
        SET_POSITION
    }

    public enum ShooterState{
        SHOOTING,
        IDLING,
        STOPPED
    }

    public enum HoodState {
        FOLLOWING,
        RESET
    }

    public enum Kicker1State {
        RESET,
        KICK
    }

    public enum Kicker2State {
        RESET,
        KICK
    }

    public enum Kicker3State {
        RESET,
        KICK
    }
}