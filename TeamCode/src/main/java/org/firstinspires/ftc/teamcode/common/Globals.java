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
    public static boolean[] ballPresent = new boolean[3];
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
    public static PIDFCoefficients shooterCoefficients = new PIDFCoefficients(0.004, 0.01, 0, 0.00064);

    //Positions:
    public static class Positions {
        public static Pose BLUE_CUBE_START = new Pose(33.45, 134.83, Math.toRadians(-90));
        public static Pose BLUE_EXODUS_START = new Pose(16.1613832853026, 110.59365994236312+2.5, Math.toRadians(180));
        public static Pose BLUE_FAR_START = new Pose(56.91, 8.84, Math.toRadians(180));

        public static Pose ALPHA_START_RED = new Pose(89.61383285302594, 9.037463976945247, 0);
        public static Pose ALPHA_START_BLUE = ALPHA_START_RED.mirror();

        public static Pose RED_CUBE_START = new Pose(110.85, 134.83, Math.toRadians(-90));
        public static Pose RED_EXODUS_START = new Pose(127.19308357348703, 110.59365994236312+2.5, Math.toRadians(0));
        public static Pose RED_FAR_START = new Pose(80.98772334293949, 9.25498559077809, Math.toRadians(0));

        public static Pose BLUE_GOAL = new Pose(12.2, 132.5);
        public static Pose RED_GOAL = new Pose(131.5, 132.5);

        public static Pose PUSH_AUTO_RED = new Pose(110.36311239193083, 134.77809798270894, 90);
        public static Pose PUSH_AUTO_BLUE = PUSH_AUTO_RED.mirror();
    }

    //Servo Positions:

    public static class Kicker {
        public static Range KICKER1 = new Range(0.685, 0.89);
        public static Range KICKER2 = new Range(0.53, 0.72);
        public static Range KICKER3 = new Range(0.32, 0.5);
    }

    public static Range HOOD = new Range(0, 0.9);



    public static class Pivot {
        public static double PIVOT_INTAKE = 0.893;
        public static double PIVOT_RESTING = 0.95;
        public static double PIVOT_RAISED = 1;
//public static double PIVOT_INTAKE = 0.90;
//        public static double PIVOT_RESTING = 0.90;
//        public static double PIVOT_RAISED = 0.90;
    }

    public static class Gate {
        public static double GATE_OPEN = 0.41; //intake
        public static double GATE_CLOSED = 0.69;
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
        public static long KICK_RAPID = 40;
        public static long KICK_FAR = 130;
        public static long KICK_SORT = 60;
        public static long KICK_SORT_SLOW = 250;

        //Wait:
        public static long GATE_WAIT = 150;
    }

    @Config
    public static class SOTM {
        public static boolean SHOOTER_ENABLED = true;
        public static double SHOOTER_RPM_GAIN = 0; //1.6
        public static double SHOOTER_WHEEL_DIAMETER_MM = 72.0;
        public static double COUNTER_ROLLER_DIAMETER_MM = 30.0;

        public static boolean TURRET_TOF_COMP_ENABLED = true;
        public static double TURRET_LINEAR_GAIN = -1.6; //-2.6
        public static double TURRET_ROTATIONAL_GAIN = -0.6;
        public static double TURRET_MIN_SPEED = 1.0;

        public static double VEL_FILTER_ALPHA = 0.6;        // 0 = heavy smooth, 1 = raw
        public static double SHOOTER_ACCEL_GAIN = 0.6;       // second-order RPM correction
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
