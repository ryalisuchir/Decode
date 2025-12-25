package org.firstinspires.ftc.teamcode.common.utility;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Rotator;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.functions.ColorReader;
import org.firstinspires.ftc.teamcode.common.utility.functions.DenoiseFilter;
import org.firstinspires.ftc.teamcode.common.utility.functions.ObeliskVision;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.Arrays;
import java.util.List;

public class Robot {
    public DcMotorEx i, t; //Intake + Transfer Combination
    public Motor s1, s2; //Shooter 1, 2. Note that this is a MotorEx, which is a SolversLib wrapper that allows us to use .getCorrectedVelocity()
    public DcMotorEx fl, fr, rl, rr; //Drive
    public ServoImplEx k1, k2, k3; //Kickers
    public ServoImplEx g; //Gate
    public ServoImplEx fs; //Failsafe
    public ServoImplEx r; //Hood
    public CRServoImplEx p1, p2; //Park
    public ServoImplEx t1, t2; //Turret 1, 2

    public Drivetrain dt;
    public Kicker kicker;
    public Rotator rotator;
    public Shooter shooter;
    public Turret turret;

    public LLResult llResult;

    public static Pose endPose; //End of pose from auto, so we can translate it to tele-op

    public Limelight3A l; //Limelight Camera
    public AnalogInput c1, c2, c3; //Brushlands
    public ColorReader reader1, reader2, reader3;

    public List<LynxModule> allHubs; //Control Hub + Expansion Hub

    public double gX, gY; //Goal Positions depending on Alliance

    public Robot(HardwareMap h, Pose p, Globals.Side s, boolean a) {
        i = h.get(DcMotorEx.class, "intake");
        t = h.get(DcMotorEx.class, "transfer");
        s1 = new Motor(h, "shooterSpinner1", Motor.GoBILDA.BARE);
        s2 = new Motor(h, "shooterSpinner2", Motor.GoBILDA.BARE);
        fl = h.get(DcMotorEx.class, "leftFront");
        fr = h.get(DcMotorEx.class, "rightFront");
        rl = h.get(DcMotorEx.class, "leftRear");
        rr = h.get(DcMotorEx.class, "rightRear");

        k1 = h.get(ServoImplEx.class, "kicker1");
        k2 = h.get(ServoImplEx.class, "kicker2");
        k3 = h.get(ServoImplEx.class, "kicker3");
        g = h.get(ServoImplEx.class, "gate");
        fs = h.get(ServoImplEx.class, "failsafe");
        r = h.get(ServoImplEx.class, "hood");
        t1 = h.get(ServoImplEx.class, "turret1");
        t2 = h.get(ServoImplEx.class, "turret2");

        g.setDirection(Servo.Direction.REVERSE);
        Globals.side = s; //Sets blue/red depending on what side we're on. Especially important for turret movement.
        if (!a) Globals.match = Globals.Match.TELEOP; //Sets the match to teleop so we don't have to reset global enums

        Arrays.fill(Globals.ballColors, Globals.BallColor.NONE);

        if (a) { //If we say that we're running auto, all the global enums will reset
            Globals.rotateState = Globals.RotateState.STOPPED;
            Globals.match = Globals.Match.AUTO;
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.kicker1State = Globals.Kicker1State.RESET;
            Globals.kicker2State = Globals.Kicker2State.RESET;
            Globals.kicker3State = Globals.Kicker3State.RESET;
            Globals.turretState = Globals.TurretState.RESET;
            Globals.hoodState = Globals.HoodState.RESET;
            Globals.gateState = Globals.GateState.CLOSED;
            Globals.failsafeState = Globals.FailsafeState.RESET;
            Globals.obeliskOptions = Globals.ObeliskOptions.PPG;
        } else {
            if (Globals.rotateState == null) {
                Globals.rotateState = Globals.RotateState.STOPPED;
            }
            if (Globals.shooterState == null) {
                Globals.shooterState = Globals.ShooterState.STOPPED;
            }
            if (Globals.kicker1State == null) {
                Globals.kicker1State = Globals.Kicker1State.RESET;
            }
            if (Globals.kicker2State == null) {
                Globals.kicker2State = Globals.Kicker2State.RESET;
            }
            if (Globals.kicker3State == null) {
                Globals.kicker3State = Globals.Kicker3State.RESET;
            }
            if (Globals.turretState == null) {
                Globals.turretState = Globals.TurretState.RESET;
            }
            if (Globals.hoodState == null) {
                Globals.hoodState = Globals.HoodState.RESET;
            }
            if (Globals.obeliskOptions == null) {
                Globals.obeliskOptions = Globals.ObeliskOptions.PPG;
            }
            if (Globals.gateState == null) {
                Globals.gateState = Globals.GateState.CLOSED;
            }
            if (Globals.failsafeState == null) {
                Globals.failsafeState = Globals.FailsafeState.RESET;
            }
        }

        //Reversing drivetrain motors:
        rr.setDirection(DcMotorEx.Direction.FORWARD);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.REVERSE);

        if (a) {
            dt = new Drivetrain(h, s, p); //If we're in auto, this will set the starting pose
        } else {
            if (Robot.endPose != null) { //If we're in teleop, the code will try to find the last location the robot was at
                dt = new Drivetrain(h, s, Robot.endPose);
            } else {
                dt = new Drivetrain(h, s, p);
            }
        }

        s1.setInverted(true); //Different way of reversing a motor using Solvers Lib
        r.setDirection(Servo.Direction.REVERSE);

        t.setDirection(DcMotorSimple.Direction.REVERSE);
        t.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        i.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        l = h.get(Limelight3A.class, "limelight"); //Creates our limelight
        l.setPollRateHz(100);
        l.start();
        l.pipelineSwitch(2); //This is the apriltag pipeline that Nate made

        if (s == Globals.Side.RED) { //Sets our global value of where the goal is depending on the side we've set
            gX = Globals.RED_CASTLE.getX();
            gY = Globals.RED_CASTLE.getY();
        } else {
            gX = Globals.BLUE_CASTLE.getX();
            gY = Globals.BLUE_CASTLE.getY();
        }

        //Brushlands setup:
        c1 = h.analogInput.get("analog1");
        c2 = h.analogInput.get("analog2");
        c3 = h.analogInput.get("analog3");

        //Sets up our denoise filters which will be used for color detection
        reader1 = new ColorReader(0, new DenoiseFilter(5));
        reader2 = new ColorReader(1, new DenoiseFilter(5));
        reader3 = new ColorReader(2, new DenoiseFilter(5));

        //Allows us to call bulk reads. Supposed to speed up loop times.
        allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        kicker = new Kicker(k1, k2, k3);
        rotator = new Rotator(i, t, g);
        shooter = new Shooter(s1, s2, r, dt.getFollower(), gX, gY);
        turret = new Turret(s, t1, t2, dt.getFollower(), gX, gY);

    }

    public void initLoop(Robot r) {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        dt.periodic();

        double hue1 = r.c1.getVoltage() / 3.3 * 360;
        double hue2 = r.c2.getVoltage() / 3.3 * 360;
        double hue3 = r.c3.getVoltage() / 3.3 * 360;

        r.reader1.readColor(hue1);
        r.reader2.readColor(hue2);
        r.reader3.readColor(hue3);
        llResult = l.getLatestResult();

        if (llResult.isValid()) {
            Globals.obeliskOptions = ObeliskVision.getObeliskFiducial(llResult);
        }

        CommandScheduler.getInstance().run();
    }

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    public void loop(Robot r) {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }

        dt.periodic();
        rotator.periodic();
        shooter.loop();
        turret.periodic();

        double hue1 = r.c1.getVoltage() / 3.3 * 360;
        double hue2 = r.c2.getVoltage() / 3.3 * 360;
        double hue3 = r.c3.getVoltage() / 3.3 * 360;

        r.reader1.readColor(hue1);
        r.reader2.readColor(hue2);
        r.reader3.readColor(hue3);

        CommandScheduler.getInstance().run();
    }

    public void stop() {
        endPose = dt.getPose();
    }
}
