package org.firstinspires.ftc.teamcode.common.utility;

import android.util.Log;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.RobotLog;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.SetShooterClass;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Spinner;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;
import org.firstinspires.ftc.teamcode.common.utility.functions.DenoiseFilter;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.ObeliskVision;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.photon.PhotonCore;
import org.firstinspires.ftc.teamcode.common.utility.profiler.Profiler;
import org.firstinspires.ftc.teamcode.common.utility.profiler.entry.BasicProfilerEntryFactory;
import org.firstinspires.ftc.teamcode.common.utility.profiler.exporter.CSVProfilerExporter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Halo {
    private static final double ANALOG_VREF = 3.3;
    private static final double HUE_NONE_MAX = 115.0;
    private static final double HUE_GREEN_MAX = 128.0;
    private static final double HUE_PURPLE_MAX = 170.0;

    public DcMotorEx i, t;
    public Motor s1, s2;
    public DcMotorEx fl, fr, rl, rr;
    public ServoImplEx k1, k2, k3;
    public ServoImplEx g;
    public ServoImplEx r;
    public ServoImplEx t1, t2;

    private final DenoiseFilter[] hueFilters = {
            new DenoiseFilter(3),
            new DenoiseFilter(3),
            new DenoiseFilter(3)
    };

    public Drivetrain dt;
    public Kicker kicker;
    public Spinner spinner;
    public Shooter shooter;
    public Turret turret;
    public SetShooterClass setShooterClass;

    public static Pose endPose;

    public static Limelight3A l;
    public WebcamName camera;

//    public Profiler profiler;
//    public File file;

    DigitalChannel dig1a, dig2a;
    AnalogInput analog1;

    DigitalChannel dig3a, dig4a;
    AnalogInput analog2;

    DigitalChannel dig5a, dig6a;
    AnalogInput analog3;

    public List<LynxModule> allHubs;

    public double gX, gY;

    public Halo(HardwareMap h, Pose p, G.Side s, boolean a) {
        G.side = s; //Sets blue/red depending on what side we're on. Especially important for turret movement.
        if (!a) G.match = G.Match.TELEOP; //Sets the match to teleop so we don't have to reset global enums

//        File logsFolder = new File(AppUtil.FIRST_FOLDER, "logs");
//        if (!logsFolder.exists()) logsFolder.mkdirs();

//        long timestamp = System.currentTimeMillis();
//        file = new File(logsFolder, "profiler-" + timestamp + ".csv");
//
//        profiler = Profiler.builder()
//                .factory(new BasicProfilerEntryFactory())
//                .exporter(new CSVProfilerExporter(file))
//                .debugLog(false) // Log EVERYTHING
//                .build();

        if (a) { //If we say that we're running auto, all the global enums will reset
            G.intakeState = G.IntakeState.STOPPED;
            G.transferState = G.TransferState.STOPPED;
            G.match = G.Match.AUTO;
            G.shooterState = G.ShooterState.STOPPED;
            G.kicker1State = G.Kicker1State.RESET;
            G.kicker2State = G.Kicker2State.RESET;
            G.kicker3State = G.Kicker3State.RESET;
            G.turretState = G.TurretState.RESET;
            G.hoodState = G.HoodState.RESET;
            G.gateState = G.GateState.CLOSED;
            G.failsafeState = G.FailsafeState.RESET;
            G.obeliskOptions = G.ObeliskOptions.PPG;
        } else {
            if (G.intakeState == null) {
                G.intakeState = G.IntakeState.STOPPED;
            }
            if (G.transferState == null) {
                G.transferState = G.TransferState.STOPPED;
            }
            if (G.shooterState == null) {
                G.shooterState = G.ShooterState.STOPPED;
            }
            if (G.kicker1State == null) {
                G.kicker1State = G.Kicker1State.RESET;
            }
            if (G.kicker2State == null) {
                G.kicker2State = G.Kicker2State.RESET;
            }
            if (G.kicker3State == null) {
                G.kicker3State = G.Kicker3State.RESET;
            }
            if (G.turretState == null) {
                G.turretState = G.TurretState.RESET;
            }
            if (G.hoodState == null) {
                G.hoodState = G.HoodState.RESET;
            }
            if (G.obeliskOptions == null) {
                G.obeliskOptions = G.ObeliskOptions.PPG;
            }
            if (G.gateState == null) {
                G.gateState = G.GateState.CLOSED;
            }
            if (G.failsafeState == null) {
                G.failsafeState = G.FailsafeState.RESET;
            }
        }

        i = h.get(DcMotorEx.class, "intake");
        t = h.get(DcMotorEx.class, "transfer");

        s1 = new Motor(h, "shooter1", Motor.GoBILDA.BARE);
        s2 = new Motor(h, "shooter2", Motor.GoBILDA.BARE);

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

        G.side = s;

        Arrays.fill(G.ballColors, G.BallColor.NONE);

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

        camera = h.get(WebcamName.class, CameraConfig.WEBCAM_NAME);

        if (s == G.Side.RED) {
            gX = G.RED_CASTLE.getX();
            gY = G.RED_CASTLE.getY();
        } else {
            gX = G.BLUE_CASTLE.getX();
            gY = G.BLUE_CASTLE.getY();
        }

        //set 1:
        analog1 = h.get(AnalogInput.class, "analog1");
        dig1a = h.get(DigitalChannel.class, "dig1a");
        dig2a = h.get(DigitalChannel.class, "dig2a");
        dig1a.setMode(DigitalChannel.Mode.INPUT);
        dig2a.setMode(DigitalChannel.Mode.INPUT);

        //set 2:
        analog2 = h.get(AnalogInput.class, "analog2");
        dig3a = h.get(DigitalChannel.class, "dig3a");
        dig4a = h.get(DigitalChannel.class, "dig4a");
        dig3a.setMode(DigitalChannel.Mode.INPUT);
        dig4a.setMode(DigitalChannel.Mode.INPUT);

        //set 3:
        analog3 = h.get(AnalogInput.class, "analog3");
        dig5a = h.get(DigitalChannel.class, "dig5a");
        dig6a = h.get(DigitalChannel.class, "dig6a");
        dig5a.setMode(DigitalChannel.Mode.INPUT);
        dig6a.setMode(DigitalChannel.Mode.INPUT);


        allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        kicker = new Kicker(k1, k2, k3);
        spinner = new Spinner(i, t, g);
        shooter = new Shooter(s1, s2, r, dt.getFollower(), gX, gY);
        setShooterClass = new SetShooterClass(s1, s2, r, dt.getFollower(), gX, gY);
        turret = new Turret(s, t1, t2, dt.getFollower());
    }

    public void initLoop(Halo r) {
        clearCache();
        dt.loop();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void init() {
//        PhotonCore.CONTROL_HUB.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
//        PhotonCore.EXPANSION_HUB.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
//        PhotonCore.experimental.setMaximumParallelCommands(8);
//        PhotonCore.enable();
    }

    public void loop(Halo r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        turret.loop();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void friedLoop() {
        clearCache();
        dt.loop();
        spinner.periodic();
        setShooterClass.loop(2);
        turret.setPositionOnce(0.5);
        readColors();
        CommandScheduler.getInstance().run();
    }

    public void sortedLoop() {
        clearCache();
        dt.loop();
        spinner.periodic();
        setShooterClass.loop(1);
        turret.loop();
        updateVision();
        readColors();
        CommandScheduler.getInstance().run();
    }

    public void unsortedLoop() {
        clearCache();
        dt.loop();
        spinner.periodic();
        setShooterClass.loop(2);
        turret.loop();
        updateVision();
        readColors();
        CommandScheduler.getInstance().run();
    }

    public void noSubsystemLoop(Halo r) {
        clearCache();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void noVisionLoop(Halo r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        turret.loop();
        readColors();
        CommandScheduler.getInstance().run();
    }

    public void noTurretLoop(Halo r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        shooter.loop();
        readColors();
        CommandScheduler.getInstance().run();
    }

    public void nothingLoop(Halo r) {
        clearCache();
        dt.loop();
        CommandScheduler.getInstance().run();
    }

    public void noOuttakeLoop(Halo r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void updateVision() {
        LLResult result = Vision.getLatestResult();
        if (result != null) {
            G.ObeliskOptions detected = ObeliskVision.getObeliskFiducial(result);
            if (detected != G.ObeliskOptions.NOT_FOUND) {
                G.obeliskOptions = detected;
            }
        }
    }

    private void readColors() {
        updateColorSlot(0, toHue(analog1.getVoltage()), dig1a.getState(), dig2a.getState());
        updateColorSlot(1, toHue(analog2.getVoltage()), dig3a.getState(), dig4a.getState());
        updateColorSlot(2, toHue(analog3.getVoltage()), dig5a.getState(), dig6a.getState());
    }

    private void updateColorSlot(int slot, double rawHue, boolean digA, boolean digB) {
        double filteredHue = hueFilters[slot].filter(rawHue);
        int rawColor = getColor(filteredHue, digA, digB);
        G.ballColors[slot] = toBallColor(rawColor);
    }


    private int getColor(double hue, boolean digA, boolean digB) {
        if (digA ^ digB) {
            return digB ? 2 : 1;
        }
        return classifyHue(hue);
    }

    private G.BallColor toBallColor(int color) {
        switch (color) {
            case 1: return G.BallColor.P;
            case 2: return G.BallColor.G;
            default: return G.BallColor.NONE;
        }
    }

    private double toHue(double voltage) {
        return Math.max(0.0, Math.min(360.0, voltage / ANALOG_VREF * 360.0));
    }

    private int classifyHue(double hue) {
        if (hue < HUE_NONE_MAX) return 0;
        if (hue < HUE_GREEN_MAX) return 2;
        if (hue < HUE_PURPLE_MAX) return 1;
        return 0;
    }

    public void clearCache() {
//        PhotonCore.CONTROL_HUB.clearBulkCache();
//        PhotonCore.EXPANSION_HUB.clearBulkCache();
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

//    public void exportProfiler(File file) {
//        RobotLog.i("Starting async profiler export to: " + file.getAbsolutePath());
//
//        Thread exportThread = new Thread(() -> {
//            try {
//                profiler.export();
//                profiler.shutdown();
//            } catch (Exception e) {
//                Log.e("An error occurred", e.toString());
//                Log.e(e.toString(), Arrays.toString(e.getStackTrace()));
//            }
//        });
//
//        exportThread.setDaemon(true);
//        exportThread.start();
//    }

    public void stop() {
        endPose = dt.getPose();
    }
}
