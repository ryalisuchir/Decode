package org.firstinspires.ftc.teamcode.common;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Spinner;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.ObeliskVision;
//import org.firstinspires.ftc.teamcode.common.utility.Vision;
import org.firstinspires.ftc.teamcode.common.utility.Vision;
import org.firstinspires.ftc.teamcode.common.utility.functions.DenoiseFilter;

import java.util.Arrays;
import java.util.List;

@Configurable
@Config
public class Halo {
    //Hardware:
    public DcMotorEx intake, transfer;
    public Motor shooter1, shooter2;
    public DcMotorEx fl, fr, rl, rr;
    public ServoImplEx k1, k2, k3;
    public ServoImplEx gate;
    public ServoImplEx hood;
    public ServoImplEx t1, t2;
    public ServoImplEx pivot;
    public static Limelight3A ll;
    public WebcamName logitech;
    AnalogInput dist1, dist2, dist3;
    NormalizedColorSensor color1, color2, color3;
    public List<LynxModule> allHubs;

    //Subsystems:
    public Drivetrain dt;
    public Kicker kicker;
    public Spinner spinner;
    public Shooter shooter;
    public Turret turret;

    //QoL:
    public static Pose endPose;
    public double gX, gY;

    //Color Sensor/Indexing:
    private static final double DISTANCE_THRESHOLD_ON = 18.0;
    private static final double DISTANCE_THRESHOLD_OFF = 22.0;

    private boolean[] ballPresent = new boolean[3];

    private boolean readDistanceHysteresis(AnalogInput pin, boolean currentState) {
        double dist = pin.getVoltage() / 3.3 * 100.0;
        if (!currentState && dist < DISTANCE_THRESHOLD_ON) return true;
        if (currentState && dist > DISTANCE_THRESHOLD_OFF) return false;
        return currentState;
    }

    private static final int COLOR_SAMPLE_COUNT = 5;

    private static final float HUE_GREEN_MIN = 100f;
    private static final float HUE_GREEN_MAX = 200f;
    private static final float HUE_PURPLE_MIN = 205f;
    private static final float HUE_PURPLE_MAX = 280f;

    private boolean colorReadDone = false;
    private int colorSamplesTaken = 0;
    private float[][] hsvAccumulator = new float[3][3];

    //Main Class:
    public Halo(HardwareMap h, Pose p, Globals.Alliance s, Globals.Match m) {
        Globals.alliance = s;
        if (m == Globals.Match.AUTO) {
            Globals.match = Globals.Match.AUTO;
        } else if (m == Globals.Match.TELEOP) {
            Globals.match = Globals.Match.TELEOP;
        } else if (m == Globals.Match.TESTING) {
            Globals.match = Globals.Match.TESTING;
        }

        if (Globals.match == Globals.Match.AUTO || Globals.match == Globals.Match.TESTING) {
            Globals.intakeState = Globals.IntakeState.STOPPED;
            Globals.transferState = Globals.TransferState.STOPPED;
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.kicker1State = Globals.Kicker1State.RESET;
            Globals.kicker2State = Globals.Kicker2State.RESET;
            Globals.kicker3State = Globals.Kicker3State.RESET;
            Globals.turretState = Globals.TurretState.RESET;
            Globals.hoodState = Globals.HoodState.RESET;
            Globals.gateState = Globals.GateState.CLOSED;
            Globals.obeliskOptions = Globals.ObeliskOptions.PPG;
            Globals.shootOrderLocked = false;
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
        }

        intake = h.get(DcMotorEx.class, "intake");
        transfer = h.get(DcMotorEx.class, "transfer");

        shooter1 = new Motor(h, "shooter1", Motor.GoBILDA.BARE);
        shooter2 = new Motor(h, "shooter2", Motor.GoBILDA.BARE);

        fl = h.get(DcMotorEx.class, "leftFront");
        fr = h.get(DcMotorEx.class, "rightFront");
        rl = h.get(DcMotorEx.class, "leftRear");
        rr = h.get(DcMotorEx.class, "rightRear");

        k1 = h.get(ServoImplEx.class, "k1");
        k2 = h.get(ServoImplEx.class, "k2");
        k3 = h.get(ServoImplEx.class, "k3");

        gate = h.get(ServoImplEx.class, "gate");
        hood = h.get(ServoImplEx.class, "hood");
        pivot = h.get(ServoImplEx.class, "pivot");

        t1 = h.get(ServoImplEx.class, "t1");
        t2 = h.get(ServoImplEx.class, "t2");

        gate.setDirection(Servo.Direction.REVERSE);
        hood.setDirection(Servo.Direction.REVERSE);
        k2.setDirection(Servo.Direction.REVERSE);
        k3.setDirection(Servo.Direction.REVERSE);
        pivot.setDirection(Servo.Direction.REVERSE);

        dist1 = h.get(AnalogInput.class, "dist1");
        dist2 = h.get(AnalogInput.class, "dist2");
        dist3 = h.get(AnalogInput.class, "dist3");

        color1 = h.get(NormalizedColorSensor.class, "color1");
        color2 = h.get(NormalizedColorSensor.class, "color2");
        color3 = h.get(NormalizedColorSensor.class, "color3");

        ll = h.get(Limelight3A.class, "limelight");
        Vision.init(ll);
        ll.pipelineSwitch(2);
        ll.setPollRateHz(100);
        ll.start();

        logitech = h.get(WebcamName.class, "logitech");

        Arrays.fill(Globals.ballColors, Globals.BallColor.NONE);

        rr.setDirection(DcMotorEx.Direction.REVERSE);
        fr.setDirection(DcMotorEx.Direction.REVERSE);
        fl.setDirection(DcMotorEx.Direction.FORWARD);
        fr.setDirection(DcMotorEx.Direction.FORWARD);
        shooter2.setInverted(false);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);

        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (m == Globals.Match.AUTO || m == Globals.Match.TESTING) {
            dt = new Drivetrain(h, s, p);
        } else {
            dt = (endPose != null)
                    ? new Drivetrain(h, s, endPose)
                    : new Drivetrain(h, s, p);
        }

        if (s == Globals.Alliance.RED) {
            gX = Globals.Positions.RED_GOAL.getX();
            gY = Globals.Positions.RED_GOAL.getY();
        } else {
            gX = Globals.Positions.BLUE_GOAL.getX();
            gY = Globals.Positions.BLUE_GOAL.getY();
        }

        allHubs = h.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        kicker = new Kicker(k1, k2, k3);
        spinner = new Spinner(intake, transfer, gate, pivot);
        shooter = new Shooter(shooter1, shooter2, hood, dt.getFollower());
        turret = new Turret(s, t1, t2, dt.getFollower());
    }

    public void initLoop(Halo r) {
        clearCache();
        dt.loop();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
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

    public void noSubsystemLoop(Halo r) {
        clearCache();
        readColors();
        updateVision();
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

    public void noOuttakeLoop(Halo r) {
        clearCache();
        dt.loop();
        spinner.periodic();
        readColors();
        updateVision();
        CommandScheduler.getInstance().run();
    }

    public void nothingLoop(Halo r) {
        clearCache();
        dt.loop();
        CommandScheduler.getInstance().run();
    }

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    public void updateVision() {
//        LLResult result = Vision.getLatestResult();
//        if (result != null) {
//            Globals.ObeliskOptions detected = ObeliskVision.getObeliskFiducial(result);
//            if (detected != Globals.ObeliskOptions.NOT_FOUND) {
//                Globals.obeliskOptions = detected;
//            }
//        }
    }

    public void stop() {
        endPose = dt.getPose();
    }

    //Indexing Functions:
    private double readDistance(AnalogInput pin) {
        return pin.getVoltage() / 3.3 * 100.0;
    }

    private void readColors() {
        ballPresent[0] = readDistanceHysteresis(dist1, ballPresent[0]);
        ballPresent[1] = readDistanceHysteresis(dist2, ballPresent[1]);
        ballPresent[2] = readDistanceHysteresis(dist3, ballPresent[2]);

        boolean ball0 = ballPresent[0];
        boolean ball1 = ballPresent[1];
        boolean ball2 = ballPresent[2];

        // Only clear individual ball colors if shoot order is not locked
        if (!Globals.shootOrderLocked) {
            if (!ball0) { Globals.ballColors[0] = Globals.BallColor.NONE; }
            else if (Globals.ballColors[0] == Globals.BallColor.NONE) { Globals.ballColors[0] = Globals.BallColor.PRESENT; }

            if (!ball1) { Globals.ballColors[1] = Globals.BallColor.NONE; }
            else if (Globals.ballColors[1] == Globals.BallColor.NONE) { Globals.ballColors[1] = Globals.BallColor.PRESENT; }

            if (!ball2) { Globals.ballColors[2] = Globals.BallColor.NONE; }
            else if (Globals.ballColors[2] == Globals.BallColor.NONE) { Globals.ballColors[2] = Globals.BallColor.PRESENT; }
        }

        boolean threeBalls = ball0 && ball1 && ball2;

        if (threeBalls && !colorReadDone) {
            if (colorSamplesTaken < COLOR_SAMPLE_COUNT) {
                sampleColorSensors();
                colorSamplesTaken++;
            }
            if (colorSamplesTaken >= COLOR_SAMPLE_COUNT) {
                applyColorResults(ball0, ball1, ball2);
                colorReadDone = true;
            }
        }

        // Only reset color read state if shoot order is not locked
        if (!threeBalls && !Globals.shootOrderLocked) {
            colorReadDone = false;
            colorSamplesTaken = 0;
            hsvAccumulator = new float[3][3];
        }
    }

    private void sampleColorSensors() {
        sampleOne(color1, 0);
        sampleOne(color2, 1);
        sampleOne(color3, 2);
    }

    private void sampleOne(NormalizedColorSensor sensor, int slot) {
        NormalizedRGBA rgba = sensor.getNormalizedColors();
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(rgba.toColor(), hsv);
        hsvAccumulator[slot][0] += hsv[0]; // hue
        hsvAccumulator[slot][1] += hsv[1]; // saturation
        hsvAccumulator[slot][2] += hsv[2]; // value
    }

    private void applyColorResults(boolean b0, boolean b1, boolean b2) {
        if (b0) Globals.ballColors[0] = classifyColor(hsvAccumulator[0][0] / COLOR_SAMPLE_COUNT);
        if (b1) Globals.ballColors[1] = classifyColor(hsvAccumulator[1][0] / COLOR_SAMPLE_COUNT);
        if (b2) Globals.ballColors[2] = classifyColor(hsvAccumulator[2][0] / COLOR_SAMPLE_COUNT);
        Globals.shootOrderLocked = true; // latch — order is now fixed until all balls are shot
    }

    private Globals.BallColor classifyColor(float avgHue) {
        if (avgHue >= HUE_GREEN_MIN && avgHue <= HUE_GREEN_MAX) return Globals.BallColor.G;
        if (avgHue >= HUE_PURPLE_MIN && avgHue <= HUE_PURPLE_MAX) return Globals.BallColor.P;
        return Globals.BallColor.NONE;
    }
}