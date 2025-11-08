package org.firstinspires.ftc.teamcode.common.robot;

import static org.firstinspires.ftc.teamcode.common.robot.Globals.Side.BLUE;

import com.pedropathing.geometry.Pose;

public class Globals {
    public static Globals.OpMode opMode = OpMode.AUTO;
    public static Globals.Side side = BLUE;
    public static Globals.ObeliskOptions obeliskOptions = ObeliskOptions.PPG;
    public static Globals.IntakeState intakeState = IntakeState.STOPPED;
    public static Globals.TransferState transferState = TransferState.STOPPED;
    public static Globals.ShooterState shooterState = ShooterState.STOPPED;
    public static Globals.Kicker1State kicker1State = Kicker1State.RESET;
    public static Globals.Kicker2State kicker2State = Kicker2State.RESET;
    public static Globals.Kicker3State kicker3State = Kicker3State.RESET;

    public static Pose DEFAULT_START_POSE = new Pose(0, 0, Math.toRadians(0));
    public static Pose BLUE_CLOSE_START = new Pose(34.149, 134.126, Math.toRadians(270)); //intake faces small triangle
    public static Pose BLUE_FAR_START = new Pose(57.2, 9.5, Math.toRadians(90)); //intake faces big triangle
    public static Pose RED_CLOSE_START = new Pose(0, 0, Math.toRadians(0)); //intake faces small triangle
    public static Pose RED_FAR_START = new Pose(0, 0, Math.toRadians(0)); //intake faces big triangle

    public static double KICKER1_RESET = 0.5;
    public static double KICKER1_KICK = 1;
    public static double KICKER2_RESET = 0.5;
    public static double KICKER2_KICK = 1;
    public static double KICKER3_RESET = 0.5;
    public static double KICKER3_KICK = 1;

    public static double MAX_TIME_SPENT_INTAKING = 5.0; //seconds

    public static double MAX_SHOOTER_POWER = 1;
    public static double MAX_TRANSFER_POWER = 1;
    public static double MAX_INTAKING_POWER = 1;

    public static double MIN_TRANSFER_VELOCITY = 5; //degrees, yet to be tuned
    public static double MIN_SHOOTER_VELOCITY = 5; //degrees, yet to be tuned

    public static double MAX_TRANSFER_SPINUP_WAIT = 500; //ms
    public static double KICK_WAIT_TIME = 20;

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
        PPG,
        PGP,
        GPP
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
        REVERSING,
        STOPPED
    }

    public enum ShooterState{
        SHOOTING,
        REGAINING_VELOCITY,
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

}
