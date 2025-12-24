package org.firstinspires.ftc.teamcode.common.utility;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.geometry.Vector2d;

public class Globals {

    public static Globals.Side side;
    public static Globals.ObeliskOptions obeliskOptions = ObeliskOptions.PPG;
    public static Globals.Match match;
    public static BallColor[] ballColors = new BallColor[3];

    public static Globals.RotateState rotateState;
    public static Globals.ShooterState shooterState;
    public static Globals.Kicker1State kicker1State;
    public static Globals.Kicker2State kicker2State;
    public static Globals.Kicker3State kicker3State;
    public static Globals.TurretState turretState;
    public static Globals.HoodState hoodState;
    public static Globals.GateState gateState;
    public static Globals.FailsafeState failsafeState;

    public static Pose DEFAULT_START_POSE = new Pose(8.507204610951009, 9.129682997118156, Math.toRadians(90));
    public static Pose BLUE_CLOSE_START = new Pose(34.149, 134.126, Math.toRadians(270)); //intake faces small triangle
    public static Pose BLUE_FAR_START = new Pose(56.43804034582132, 7.6772334293948195, Math.toRadians(90)); //intake faces big triangle
    public static Pose RED_CLOSE_START = new Pose(0, 0, Math.toRadians(0)); //intake faces small triangle
    public static Pose RED_FAR_START = BLUE_FAR_START.mirror();

    public static Vector2d BLUE_CASTLE = new Vector2d(0, 144);
    public static Vector2d RED_CASTLE = new Vector2d(137, 137);

    public static double ROTATOR_AGGRESSION = 0.15;

    public static double KICKER1_RESET = 0.757;
    public static double KICKER1_KICK = 0.92;
    public static double KICKER2_RESET = 0.4;
    public static double KICKER2_KICK = 0.24;
    public static double KICKER3_RESET = 0.67;
    public static double KICKER3_KICK = 0.84;

    public static double GATE_OPEN = 0;
    public static double GATE_CLOSED = 1;

    public static double FAILSAFE_RESET = 0.24;
    public static double FAILSAFE_KICK = 0.8;

    public static double TURRET_RESET = 0.31;

    public static double MIN_TURRET = 0.31;
    public static double MAX_TURRET = 0.31;

    public static double HOOD_LOWERED = 0.2;
    public static double HOOD_MAX = 0.86;

    public static double MAX_TIME_SPENT_INTAKING = 5000;


    public static double MIN_SHOOTER_POWER = 0.4; //used to prevent current draw issues

    public static double MAX_TRANSFER_POWER = -1;
    public static double MAX_INTAKING_POWER = 1;

    public static double SHOOTER_VELOCITY_TOLERANCE = 80; //degrees, yet to be tuned

    public static long KICK_WAIT_TIME = 1000;
    public static long KICK_FAILSAFE = 700;

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
    public enum RotateState{
        INTAKING,
        TRANSFERRING,
        EJECTING,
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
        BLUE_FAR_OBELISK,
        RED_CLOSE_OBELISK,
        RED_FAR_OBELISK
    }

    public enum HoodState {
        FOLLOWING,
        RESET
    }

}
