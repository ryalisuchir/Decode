package org.firstinspires.ftc.teamcode.common.utility;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;

@Config
public class G {

    public static G.Side side;
    public static G.ObeliskOptions obeliskOptions = ObeliskOptions.PPG;
    public static G.Match match;
    public static BallColor[] ballColors = new BallColor[3];

    public static boolean shooterKicking = false;

    public static G.IntakeState intakeState;
    public static G.TransferState transferState;
    public static G.ShooterState shooterState;
    public static G.Kicker1State kicker1State;
    public static G.Kicker2State kicker2State;
    public static G.Kicker3State kicker3State;
    public static G.TurretState turretState;
    public static G.HoodState hoodState;
    public static G.GateState gateState;
    public static G.FailsafeState failsafeState;

    public static Pose DEFAULT_START_POSE = new Pose(8.069164265129686, 9.037463976945268, Math.toRadians(90));

    public static Pose BLUE_CUBE_START = new Pose(32, 134, Math.toRadians(-90));
    public static Pose RED_CUBE_START = BLUE_CUBE_START.mirror();
    public static Pose RED_FAR_START = new Pose(80.71469740634006, 8.922190201729103, Math.toRadians(0));
    public static Pose BLUE_FAR_START = new Pose(52, 8.922190201729103, Math.toRadians(180));

    public static double pivotX = 0.0; //forward length for turret hole
    public static double pivotY = 0; //left length for turret hole. used to be 3

    public static Pose RED_CASTLE = new Pose(131.5, 132.5);
    public static Pose BLUE_CASTLE = RED_CASTLE.mirror();

    public static double POWER_RAMP_PER_SEC = 4.2;

    public static double KICKER1_RESET = 0.769;
    public static double KICKER1_KICK = 0.91;
    public static double KICKER2_RESET = 0.38;
    public static double KICKER2_KICK = 0.25;
    public static double KICKER3_RESET = 0.67;
    public static double KICKER3_KICK = 0.8;

    public static double TILT_RESET = 0;
    public static double TILT_ENGAGED = 0.8;

    public static double GATE_OPEN = 0.13;
    public static double GATE_CLOSED = 0.36;

    public static double TURRET_RESET = 0.5;
    public static double TURRET_BLUE_CLOSE_READ = 0.15;
    public static double TURRET_RED_CLOSE_READ = 0;

    public static double TURRET_BLUE_FAR_READ = 0.03;
    public static double TURRET_RED_FAR_READ = 0.6;

    public static double HOOD_LOWERED = 0.66;
    public static double HOOD_MAX = 0.92;

    public static double MIN_SHOOTER_POWER = 0.4;
    public static double MAX_INTAKING_POWER = 1;

    public static double SHOOTER_VELOCITY_TOLERANCE = 100;

    // Red side goal-center offsets
    public static double GOAL_CENTER_X_BIAS_CLOSE_RED = 12.5;
    public static double GOAL_CENTER_Y_BIAS_CLOSE_RED = 8;

    public static double GOAL_CENTER_X_BIAS_FAR_RED = 12.5;
    public static double GOAL_CENTER_Y_BIAS_FAR_RED = 12.5;

    public static double GOAL_CENTER_X_BIAS_CLOSE_BLUE = 0;
    public static double GOAL_CENTER_Y_BIAS_CLOSE_BLUE = 5;
    public static double GOAL_CENTER_X_BIAS_FAR_BLUE = GOAL_CENTER_X_BIAS_CLOSE_BLUE;
    public static double GOAL_CENTER_Y_BIAS_FAR_BLUE = GOAL_CENTER_Y_BIAS_CLOSE_BLUE;

    public static long KICK_WAIT_TELE = 140;
    public static long KICK_WAIT_RAPID = 70; //65
    public static long KICK_WAIT_AUTO = 150;

    public static long GATE_WAIT_AUTO = 300;
    public static long GATE_WAIT_TELE = 600;

    public static boolean TURRET_TOF_COMP_ENABLED = true;
    public static double TURRET_TOF_COMP_GAIN = 2;
    public static double TURRET_SOTM_MIN_SPEED = 2.3;

    public static boolean TURRET_RED_FAR_MODEL_ENABLED = true;
    public static double TURRET_RED_FAR_MODEL_MAX_SPEED = 3;
    public static double TURRET_RED_FAR_MODEL_BLEND = 0.6;
    public static double TURRET_RED_FAR_MODEL_MAX_CORRECTION = 0.16;

    public static boolean SHOOTER_SOTM_ENABLED = true;
    public static double SHOOTER_SOTM_RPM_GAIN = 0.6;
    public static double SHOOTER_SOTM_WHEEL_DIAMETER_MM = 72.0;


    //Pre-Match Configuration:
    public enum Side {
        RED,
        BLUE
    }

    public enum Match {
        AUTO,
        TELEOP
    }

    //Match Configuration:
    public enum ObeliskOptions{
        NOT_FOUND,
        PPG,
        PGP,
        GPP
    }

    //Ball Color Options:
    public enum BallColor {
        NONE,
        G,
        P
    }

    //Robot Configurations:
    public enum IntakeState{
        INTAKING,
        EJECTING,
        STOPPED
    }
    public enum TransferState{
        TRANSFERRING,
        INTAKING,
        STOPPED
    }

    public enum GateState{
        OPEN,
        CLOSED
    }

    public enum FailsafeState{
        RESET,
        KICK
    }

    public enum ShooterState{
        SHOOTING,
        IDLING,
        STOPPED
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

    public enum TurretState {
        FOLLOWING,
        RESET,
        BLUE_CLOSE_OBELISK,
        RED_CLOSE_OBELISK,
        RED_FAR_GOAL,
        RED_FAR_GOAL_TELE,
        RED_CLOSE_GOAL,
        RED_CLOSE_DIFF_GOAL,
        FAILED,
        BLUE_FAR_GOAL,
        BLUE_CLOSE_GOAL,
        BLUE_CLOSE_DIFF_GOAL,
        SET_POSITION
    }

    public enum HoodState {
        FOLLOWING,
        RESET
    }

}
