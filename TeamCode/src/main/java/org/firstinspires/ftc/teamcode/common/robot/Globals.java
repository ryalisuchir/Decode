package org.firstinspires.ftc.teamcode.common.robot;

import static org.firstinspires.ftc.teamcode.common.robot.Globals.Side.BLUE;

import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.geometry.Vector2d;

import java.util.Vector;

public class Globals {
    public static Globals.OpMode opMode = OpMode.AUTO;
    public static Globals.Side side = BLUE;
    public static Globals.ObeliskOptions obeliskOptions = ObeliskOptions.NOT_FOUND;
    public static Globals.BallColor1 ballColor1 = BallColor1.NONE;
    public static Globals.BallColor2 ballColor2 = BallColor2.NONE;
    public static Globals.BallColor3 ballColor3 = BallColor3.NONE;

    public static Globals.IntakeState intakeState = IntakeState.STOPPED;
    public static Globals.TransferState transferState = TransferState.STOPPED;
    public static Globals.ShooterState shooterState = ShooterState.STOPPED;
    public static Globals.Kicker1State kicker1State = Kicker1State.RESET;
    public static Globals.Kicker2State kicker2State = Kicker2State.RESET;
    public static Globals.Kicker3State kicker3State = Kicker3State.RESET;
    public static Globals.TurretState turretState = TurretState.RESET;
    public static Globals.HoodState hoodState = HoodState.RESET;

    public static Pose DEFAULT_START_POSE = new Pose(8.507204610951009, 9.129682997118156, Math.toRadians(90));
    public static Pose BLUE_CLOSE_START = new Pose(34.149, 134.126, Math.toRadians(270)); //intake faces small triangle
    public static Pose BLUE_FAR_START = new Pose(57.2, 9.5, Math.toRadians(90)); //intake faces big triangle
    public static Pose RED_CLOSE_START = new Pose(0, 0, Math.toRadians(0)); //intake faces small triangle
    public static Pose RED_FAR_START = new Pose(0, 0, Math.toRadians(0)); //intake faces big triangle

    public static Vector2d BLUE_CASTLE = new Vector2d(15, 133);
    public static Vector2d RED_CASTLE = new Vector2d(130, 133);

    public static double KICKER1_RESET = 0.48;
    public static double KICKER1_KICK = 0.59;
    public static double KICKER2_RESET = 0.045;
    public static double KICKER2_KICK = 0.15;
    public static double KICKER3_RESET = 0.44;
    public static double KICKER3_KICK = 0.35;

    public static double TURRET_RESET = 0.290;
    public static double TURRET_BLUE_OBELISK = 0.21;
    public static double TURRET_RED_OBELISK = 0.32;

    public static double HOOD_LOWERED = 0.2;
    public static double HOOD_MAX = 0.8;

    public static double MAX_TIME_SPENT_INTAKING = 5.0; //seconds

    public static double MIN_SHOOTER_POWER = 0.2; //used to prevent current draw issues

    public static double MAX_TRANSFER_POWER = 1;
    public static double MAX_INTAKING_POWER = 1;
    public static double MIN_INTAKING_POWER = 0.2;

    public static double MAX_SHOOTER_VELOCITY = 150; //degrees, yet to be tuned
    public static double SHOOTER_VELOCITY_TOLERANCE = 10; //degrees, yet to be tuned

    public static double KICK_WAIT_TIME = 200;

    //Pre-Match Configuration:
    public enum OpMode {
        AUTO,
        TELEOP
    }
    public enum Side {
        RED,
        BLUE
    }

    //Match Configuration:
    public enum ObeliskOptions{
        NOT_FOUND,
        PPG,
        PGP,
        GPP
    }

    //Ball Color Options:
    public enum BallColor1{
        P,
        G,
        NONE
    }
    public enum BallColor2{
        P,
        G,
        NONE
    }
    public enum BallColor3{
        P,
        G,
        NONE
    }

    //Robot Configurations:
    public enum IntakeState{
        SLOW_INTAKING,
        INTAKING,
        REVERSING,
        STOPPED
    }

    public enum TransferState{
        TRANSFERRING,
        STOPPED
    }

    public enum ShooterState{
        SHOOTING,
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
        RESET
    }

    public enum HoodState {
        FOLLOWING,
        RESET
    }

}
