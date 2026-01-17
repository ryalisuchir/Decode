package org.firstinspires.ftc.teamcode.common.utility;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Spinner;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.functions.ColorReader;
import org.firstinspires.ftc.teamcode.common.utility.functions.DenoiseFilter;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.ObeliskVision;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;

import java.util.Arrays;
import java.util.List;

public class Robot {
    YawPitchRollAngles orientation;
    public DcMotorEx i, t;
    public Motor s1, s2;
    public DcMotorEx fl, fr, rl, rr;
    public ServoImplEx k1, k2, k3;
    public ServoImplEx g;
    public ServoImplEx r;
    public ServoImplEx t1, t2;

    public Drivetrain dt;
    public Kicker kicker;
    public Spinner spinner;
    public Shooter shooter;
    public Turret turret;

    public static Pose endPose;

    public static Limelight3A l;

    public AnalogInput c1, c2, c3;
    public ColorReader reader1, reader2, reader3;

    public List<LynxModule> allHubs;

    public double gX, gY;

    public Robot(HardwareMap h, Pose p, Globals.Side s, boolean a) {
        Globals.side = s; //Sets blue/red depending on what side we're on. Especially important for turret movement.
        if (!a) Globals.match = Globals.Match.TELEOP; //Sets the match to teleop so we don't have to reset global enums

        if (a) { //If we say that we're running auto, all the global enums will reset
            Globals.intakeState = Globals.IntakeState.STOPPED;
            Globals.transferState = Globals.TransferState.STOPPED;
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
            if (Globals.intakeState == null) {
                Globals.intakeState = Globals.IntakeState.STOPPED;
            }
            if (Globals.transferState == null) {
                Globals.transferState = Globals.TransferState.STOPPED;
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
        r = h.get(ServoImplEx.class, "hood");

        t1 = h.get(ServoImplEx.class, "turret1");
        t2 = h.get(ServoImplEx.class, "turret2");

        g.setDirection(Servo.Direction.REVERSE);
        r.setDirection(Servo.Direction.REVERSE);

        Globals.side = s;

        Arrays.fill(Globals.ballColors, Globals.BallColor.NONE);

        rr.setDirection(DcMotorEx.Direction.FORWARD);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        fl.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.REVERSE);

        if (a) {
            dt = new Drivetrain(h, s, p);
        } else {
            dt = (endPose != null)
                    ? new Drivetrain(h, s, endPose)
                    : new Drivetrain(h, s, p);
        }

        s2.setInverted(true);

        t.setDirection(DcMotorSimple.Direction.REVERSE);
        t.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        i.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        l = h.get(Limelight3A.class, "limelight");
        Vision.init(l);
        l.pipelineSwitch(2);
        l.setPollRateHz(100);
        l.start();

        if (s == Globals.Side.RED) {
            gX = Globals.RED_CASTLE.getX();
            gY = Globals.RED_CASTLE.getY();
        } else {
            gX = Globals.BLUE_CASTLE.getX();
            gY = Globals.BLUE_CASTLE.getY();
        }

        c1 = h.analogInput.get("analog1");
        c2 = h.analogInput.get("analog2");
        c3 = h.analogInput.get("analog3");

        reader1 = new ColorReader(0, new DenoiseFilter(5));
        reader2 = new ColorReader(1, new DenoiseFilter(5));
        reader3 = new ColorReader(2, new DenoiseFilter(5));

        allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        kicker = new Kicker(k1, k2, k3);
        spinner = new Spinner(i, t, g);
        shooter = new Shooter(s1, s2, r, dt.getFollower(), gX, gY, spinner);
        turret = new Turret(s, t1, t2, dt.getFollower(), gX, gY);
    }

    public void initLoop(Robot r) {
        clearCache();
        dt.loop();
        readColors(r);
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void loop(Robot r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        turret.loop();
        readColors(r);
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void noVisionLoop(Robot r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        turret.loop();
        readColors(r);
        CommandScheduler.getInstance().run();
    }

    public void noTurretLoop(Robot r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        readColors(r);
        CommandScheduler.getInstance().run();
    }

    public void nothingLoop(Robot r) {
        clearCache();
        dt.loop();
        CommandScheduler.getInstance().run();
    }

    public void noOuttakeLoop(Robot r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        readColors(r);
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void updateVision() {
        LLResult result = Vision.getLatestResult();
        if (result != null) {
            Globals.obeliskOptions = ObeliskVision.getObeliskFiducial(result);
        }
    }

    private void readColors(Robot r) {
        double hue1 = r.c1.getVoltage() / 3.3 * 360;
        double hue2 = r.c2.getVoltage() / 3.3 * 360;
        double hue3 = r.c3.getVoltage() / 3.3 * 360;

        r.reader1.readColor(hue1);
        r.reader2.readColor(hue2);
        r.reader3.readColor(hue3);
    }

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    public void stop() {
        endPose = dt.getPose();
    }
}
