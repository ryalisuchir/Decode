package org.firstinspires.ftc.teamcode.common.utility;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.geometry.Vector2d;

@Config
public class Globals {

    public static Globals.Side side;
    public static Globals.ObeliskOptions obeliskOptions = ObeliskOptions.PPG;
    public static Globals.Match match;
    public static BallColor[] ballColors = new BallColor[3];

    public static Globals.IntakeState intakeState;
    public static Globals.TransferState transferState;
    public static Globals.ShooterState shooterState;
    public static Globals.Kicker1State kicker1State;
    public static Globals.Kicker2State kicker2State;
    public static Globals.Kicker3State kicker3State;
    public static Globals.TurretState turretState;
    public static Globals.RobotState robotState;
    public static Globals.HoodState hoodState;
    public static Globals.GateState gateState;
    public static Globals.FailsafeState failsafeState;

    public static Pose DEFAULT_START_POSE = new Pose(16/2, 17.5/2, Math.toRadians(90));
    public static Pose OTHER_DEFAULT_START_POSE = new Pose(134, 10, Math.toRadians(90));

    public static Pose BLUE_CLOSE_START = new Pose(34.149, 134.126, Math.toRadians(270)); //intake faces small triangle
    public static Pose BLUE_CUBE_START = new Pose(55, 137, Math.toRadians(-90));
    public static Pose RED_CUBE_START = new Pose(134, 138, Math.toRadians(-90));
    public static Pose BLUE_FAR_START = new Pose(54, 7.6772334293948195, Math.toRadians(90));
    public static Pose RED_CLOSE_START = new Pose(0, 0, Math.toRadians(0)); //intake faces small triangle
    public static Pose RED_FAR_START = BLUE_FAR_START.mirror();

    public static Vector2d BLUE_CASTLE = new Vector2d(0, 144);
    public static Vector2d RED_CASTLE = new Vector2d(144, 144);

    public static double POWER_RAMP_PER_SEC = 4.2;

    public static final double TURRET_OFFSET_X = 1.1674169;  // forward from center
    public static final double TURRET_OFFSET_Y = 2.9559;  // left from center
    public static final double BARREL_LENGTH = 6.0; // inches (measure this)

    public static double k = 1.0;

    public static double KICKER1_RESET = 0.757;
    public static double KICKER1_KICK = 0.92;
    public static double KICKER2_RESET = 0.385;
    public static double KICKER2_KICK = 0.24;
    public static double KICKER3_RESET = 0.67;
    public static double KICKER3_KICK = 0.84;

    public static double GATE_OPEN = 0.95;
    public static double GATE_CLOSED = 0.27;

    public static double TURRET_RESET = 0.42;
    public static double TURRET_BLUE_CLOSE_READ = 0.15;
    public static double TURRET_RED_CLOSE_READ = 0.78;
    public static double TURRET_BLUE_FAR_READ = 0.42;
    public static double TURRET_RED_FAR_READ = 0.42;

    public static double MIN_TURRET = 0;
    public static double MAX_TURRET = 1;

    public static double HOOD_LOWERED = 0.77;
    public static double HOOD_MAX = 0.98;

    public static double MIN_SHOOTER_POWER = 0.4;

    public static double MAX_TRANSFER_POWER = -1;
    public static double TRANSFER_INTAKING = 1;
    public static double MAX_INTAKING_POWER = 1;

    public static double SHOOTER_VELOCITY_TOLERANCE = 100;

    public static long KICK_WAIT_TELE = 200;
    public static long KICK_WAIT_AUTO = 200;
    public static long GATE_WAIT_AUTO = 100;
    public static long GATE_WAIT_TELE = 600;

    //Pre-Match Configuration:
    public enum Side {
        RED,
        BLUE
    }

    public enum RobotState {
        KICKING,
        NOT_KICKING
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
        BLUE_FAR_OBELISK,
        RED_CLOSE_OBELISK,
        RED_FAR_OBELISK,
        SET_POSITION
    }

    public enum HoodState {
        FOLLOWING,
        RESET
    }

}