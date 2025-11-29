package org.firstinspires.ftc.teamcode.common.robot;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.camerastream.PanelsCameraStream;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.ftc.PoseConverter;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
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
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.KickerSubsystem;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.TurretSubsystem;
import org.firstinspires.ftc.teamcode.common.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.common.robot.utility.DenoiseFilter;
import org.firstinspires.ftc.teamcode.common.robot.utility.OneDKalmanFilter;

import java.util.List;

public class Robot {
    public DcMotorEx leftFront, rightFront, leftRear, rightRear; //Drivetrain motors
    public DcMotorEx transfer, intake; //Intake, Transfer, Shooter motors
    public Motor shooterSpinner1, shooterSpinner2; //Intake, Transfer, Shooter motors
    public IntakeSubsystem intakeSubsystem;
    public KickerSubsystem kickerSubsystem;
    public ShooterSubsystem shooterSubsystem;
    public TurretSubsystem turretSubsystem;

    private DenoiseFilter denoise1, denoise2, denoise3;

    public ServoImplEx kicker1, kicker2, kicker3;
    public ServoImplEx turret1, turret2;
    public ServoImplEx hood;

    public Limelight3A ll;

    public List<LynxModule> allHubs;
    public LynxModule ControlHub;
    public Follower follower;
    public static Pose endPose;

    public AnalogInput sensor1, sensor2, sensor3;

    public double goalX, goalY;
    public double turretAngle;

    public OneDKalmanFilter filter;

    public Robot(HardwareMap hardwareMap, Pose initialPose, Globals.Side side, boolean autoBoolean) {
        //Drivetrain Motors:
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        //Shooter Motors:
        shooterSpinner1 = new Motor(hardwareMap, "shooterSpinner1", Motor.GoBILDA.BARE);
        shooterSpinner2 = new Motor(hardwareMap, "shooterSpinner2", Motor.GoBILDA.BARE);
        //Intake + Transfer Motors:
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        //Turret Servos:
        turret1 = hardwareMap.get(ServoImplEx.class, "turret1");
        turret2 = hardwareMap.get(ServoImplEx.class, "turret2");
        //Kicker Servos:
        kicker1 = hardwareMap.get(ServoImplEx.class, "kicker1");
        kicker2 = hardwareMap.get(ServoImplEx.class, "kicker2");
        kicker3 = hardwareMap.get(ServoImplEx.class, "kicker3");
        //Hood Servo:
        hood = hardwareMap.get(ServoImplEx.class, "hood");

        Globals.intakeState = Globals.IntakeState.STOPPED;
        Globals.transferState = Globals.TransferState.STOPPED;
        Globals.shooterState = Globals.ShooterState.STOPPED;
        Globals.kicker1State = Globals.Kicker1State.RESET;
        Globals.kicker2State = Globals.Kicker2State.RESET;
        Globals.kicker3State = Globals.Kicker3State.RESET;
        Globals.turretState = Globals.TurretState.RESET;
        Globals.hoodState = Globals.HoodState.RESET;

        //Reversing of Motors:
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(initialPose);

        shooterSpinner2.setInverted(true);

        turret2.setDirection(Servo.Direction.REVERSE);

        brake(leftFront, leftRear, rightFront, rightRear, transfer, intake);

        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ll = hardwareMap.get(Limelight3A.class, "limelight");
        ll.setPollRateHz(100);
        ll.start();
        ll.pipelineSwitch(2);

        Globals.side = side;

        if (Globals.side == Globals.Side.RED) {
            goalX = Globals.RED_CASTLE.getX();
            goalY = Globals.RED_CASTLE.getY();
        } else {
            goalX = Globals.BLUE_CASTLE.getX();
            goalY = Globals.BLUE_CASTLE.getY();
        }

        //Color Sensor - Digital:
        sensor1 = hardwareMap.analogInput.get("analog1");
        sensor2 = hardwareMap.analogInput.get("analog2");
        sensor3 = hardwareMap.analogInput.get("analog3");

        filter = new OneDKalmanFilter(
                0,
                10,
                0.1, //higher q means we trust odometry less (assumes more drift)
                2.0 //higher r means we trust limelight less (noisy cam)
        );

        denoise1 = new DenoiseFilter(5);
        denoise2 = new DenoiseFilter(5);
        denoise3 = new DenoiseFilter(5);

        intakeSubsystem = new IntakeSubsystem(intake);
        kickerSubsystem = new KickerSubsystem(kicker1, kicker2, kicker3);
        shooterSubsystem = new ShooterSubsystem(shooterSpinner1, shooterSpinner2, transfer, hood, follower, goalX, goalY);
        turretSubsystem = new TurretSubsystem(side, turret1, turret2, follower, goalX, goalY);

        CommandScheduler.getInstance().registerSubsystem(
                intakeSubsystem,
                kickerSubsystem,
                shooterSubsystem,
                turretSubsystem
        );

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent() && LynxConstants.isEmbeddedSerialNumber(hub.getSerialNumber())) {
                ControlHub = hub;
            }
        }
    }

    public void readColor(double hue, DenoiseFilter denoise) {
        Globals.BallColor1 color = null;

        if (hue < 120) {
            color = Globals.BallColor1.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            color = Globals.BallColor1.G;
        }
        else if (hue >= 125 && hue < 180) {
            color = Globals.BallColor1.P;
        }

        if (color == null) {
            denoise.reset();
            color = Globals.BallColor1.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            color = Globals.BallColor1.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            color = Globals.BallColor1.NONE;
        }

        Globals.ballColor1 = color;
    }

    public void readColor2(double hue, DenoiseFilter denoise) {
        Globals.BallColor2 color = null;

        if (hue < 120) {
            color = Globals.BallColor2.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            color = Globals.BallColor2.G;
        }
        else if (hue >= 125 && hue < 180) {
            color = Globals.BallColor2.P;
        }

        if (color == null) {
            denoise.reset();
            color = Globals.BallColor2.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            color = Globals.BallColor2.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            color = Globals.BallColor2.NONE;
        }

        Globals.ballColor2 = color;
    }

    public void readColor3(double hue, DenoiseFilter denoise) {
        Globals.BallColor3 color = null;

        if (hue < 120) {
            color = Globals.BallColor3.NONE;
        }
        else if (hue >= 120 && hue < 125) {
            color = Globals.BallColor3.G;
        }
        else if (hue >= 125 && hue < 180) {
            color = Globals.BallColor3.P;
        }

        if (color == null) {
            denoise.reset();
            color = Globals.BallColor3.NONE;
        }
        else if (denoise.isWindowFull()) {
            int avgIndex = (int)Math.round(denoise.filter(color.ordinal()));
            color = Globals.BallColor3.values()[avgIndex];
        }
        else {
            denoise.filter(color.ordinal());
            color = Globals.BallColor3.NONE;
        }

        Globals.ballColor3 = color;
    }

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

        return turretAngle;
    }

    public void getObeliskFiducial() {
        LLResult result = ll.getLatestResult();
        if (result == null) {
            Globals.obeliskOptions = Globals.ObeliskOptions.NOT_FOUND;
            return;
        }

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) {
            Globals.obeliskOptions = Globals.ObeliskOptions.NOT_FOUND;
            return;
        }

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                    Globals.obeliskOptions = Globals.ObeliskOptions.GPP;
                    return;
                case 22:
                    Globals.obeliskOptions = Globals.ObeliskOptions.PGP;
                    return;
                case 23:
                    Globals.obeliskOptions = Globals.ObeliskOptions.PPG;
                    return;
            }
        }

        Globals.obeliskOptions = Globals.ObeliskOptions.NOT_FOUND;
    }

    public double getGoalDistance() {
        LLResult result = ll.getLatestResult();

        double robotYaw = follower.getHeading();
        ll.updateRobotOrientation(robotYaw);

        if (result == null || !result.isValid()) return 0;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                case 22:
                case 23:
                    return 0;
            }
        }

        Pose3D botpose = result.getBotpose_MT2();
        if (botpose != null) {
            Pose currentDetectedPose = PoseConverter.pose2DToPose(new Pose2D(DistanceUnit.METER, botpose.getPosition().x, botpose.getPosition().y, AngleUnit.RADIANS, robotYaw), InvertedFTCCoordinates.INSTANCE);
            double dx = currentDetectedPose.getX() - goalX;
            double dy = currentDetectedPose.getY() - goalY;

            return Math.hypot(dx, dy);
        }
        return 0;
    }

    public double getCorrectedGoalDistance() {
        double llDistance = getGoalDistance();
        double dxOdo = follower.getPose().getX() - goalX;
        double dyOdo = follower.getPose().getY() - goalY;
        double odoDistance = Math.hypot(dxOdo, dyOdo);

        filter.update(odoDistance);
        return filter.update(llDistance);
    }

    public double getDistanceToGoalPinpoint() {
        double dxOdo = follower.getPose().getX() - goalX;
        double dyOdo = follower.getPose().getY() - goalY;
        return Math.hypot(dxOdo, dyOdo);
    }

    public Pose getLLPosition() {
        LLResult result = ll.getLatestResult();
        double robotYaw = follower.getHeading();
        ll.updateRobotOrientation(robotYaw);

        if (result == null || !result.isValid()) return new Pose(0,0,Math.toRadians(0));

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                case 22:
                case 23:
                    return null;
            }
        }

        Pose3D botpose = result.getBotpose_MT2();
        if (botpose == null) {
            return new Pose(0,0,Math.toRadians(0));
        }

        return PoseConverter.pose2DToPose(new Pose2D(DistanceUnit.INCH, botpose.getPosition().x, botpose.getPosition().y, AngleUnit.RADIANS, robotYaw), InvertedFTCCoordinates.INSTANCE);
    }

    private void brake(DcMotorEx... motors) {
        for (DcMotorEx m : motors) m.setZeroPowerBehavior(BRAKE);
    }

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            if (hub.getDeviceName().equals("Servo Hub") || hub.getDeviceName().equals("pinpoint"))
                return;
            hub.clearBulkCache();
        }
    }

    public void stop() {
        endPose = follower.getPose();
    }

    public void loop(Robot robot) {
        for (LynxModule hub : allHubs) {
            if (hub.getDeviceName().equals("Servo Hub") || hub.getDeviceName().equals("pinpoint"))
                return;
            hub.clearBulkCache();
        }

        double hue1 = sensor1.getVoltage() / 3.3 * 360;
        double hue2 = sensor2.getVoltage() / 3.3 * 360;
        double hue3 = sensor3.getVoltage() / 3.3 * 360;

        follower.update();
        if (intakeSubsystem != null) { robot.intakeSubsystem.syncer(); }
        robot.kickerSubsystem.syncer();
        if (shooterSubsystem != null) { robot.shooterSubsystem.syncer(); }
        if (turretSubsystem != null) { robot.turretSubsystem.syncer(); }
        readColor(hue1, denoise1);
        readColor2(hue2, denoise2);
        readColor3(hue3, denoise3);
    }
}