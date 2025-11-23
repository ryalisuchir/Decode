package org.firstinspires.ftc.teamcode.common.robot;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorGoBildaPinpoint;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.common.pedroPathing.Constants;

import java.util.List;

public class Robot {
    public DcMotorEx leftFront, rightFront, leftRear, rightRear; //Drivetrain motors
    public DcMotorEx shooterSpinner1, shooterSpinner2, transfer, intake; //Intake, Transfer, Shooter motors
    public IntakeSubsystem intakeSubsystem;

    public ServoImplEx kicker1, kicker2, kicker3;
    public ServoImplEx turret1, turret2;
    public ServoImplEx hood;

    public Limelight3A ll;

    public List<LynxModule> allHubs;
    public LynxModule ControlHub;
    public Follower follower;

    DigitalChannel brushlands1pin0, brushlands1pin1; //Close Brushlands (1)
    DigitalChannel brushlands2pin0, brushlands2pin1; //Middle Brushlands (2)
    DigitalChannel brushlands3pin0, brushlands3pin1; //Far Brushlands (3)

    public Robot(HardwareMap hardwareMap, Pose initialPose, Globals.Side side, boolean autoBoolean) {
        //Drivetrain Motors:
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        //Shooter Motors:
        shooterSpinner1 = hardwareMap.get(DcMotorEx.class, "shooterSpinner1");
        shooterSpinner2 = hardwareMap.get(DcMotorEx.class, "shooterSpinner2");
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

        //Reversing of Motors:
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);

        shooterSpinner2.setDirection(DcMotorEx.Direction.REVERSE);

        turret2.setDirection(Servo.Direction.REVERSE);

        brake(leftFront, leftRear, rightFront, rightRear, shooterSpinner1, shooterSpinner2, transfer, intake);

        shooterSpinner1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterSpinner2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ll = hardwareMap.get(Limelight3A.class, "limelight");
        ll.setPollRateHz(100);
        ll.start();
        ll.pipelineSwitch(2);

        //Color Sensor - Digital:
//        brushlands1pin0 = hardwareMap.digitalChannel.get("digital0");
//        brushlands1pin1 = hardwareMap.digitalChannel.get("digital1");
//        brushlands2pin0 = hardwareMap.digitalChannel.get("digital0");
//        brushlands2pin1 = hardwareMap.digitalChannel.get("digital1");
//        brushlands3pin0 = hardwareMap.digitalChannel.get("digital0");
//        brushlands3pin1 = hardwareMap.digitalChannel.get("digital1");

        intakeSubsystem = new IntakeSubsystem(intake);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(initialPose);

        CommandScheduler.getInstance().registerSubsystem(
                intakeSubsystem
        );

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent() && LynxConstants.isEmbeddedSerialNumber(hub.getSerialNumber())) {
                ControlHub = hub;
            }
        }
    }

    public void determineColor() {
        if (brushlands1pin0.getState()) {
            Globals.ballColor1 = Globals.BallColor1.P;
        }
        if (brushlands1pin1.getState()) {
            Globals.ballColor1 = Globals.BallColor1.G;
        }
        if (!brushlands1pin1.getState() && !brushlands1pin0.getState()) {
            Globals.ballColor1 = Globals.BallColor1.NONE;
        }
        if (brushlands2pin0.getState()) {
            Globals.ballColor2 = Globals.BallColor2.P;
        }
        if (brushlands2pin1.getState()) {
            Globals.ballColor2 = Globals.BallColor2.G;
        }
        if (!brushlands2pin1.getState() && !brushlands2pin0.getState()) {
            Globals.ballColor2 = Globals.BallColor2.NONE;
        }
        if (brushlands3pin0.getState()) {
            Globals.ballColor3 = Globals.BallColor3.P;
        }
        if (brushlands3pin1.getState()) {
            Globals.ballColor3 = Globals.BallColor3.G;
        }
        if (!brushlands3pin1.getState() && !brushlands3pin0.getState()) {
            Globals.ballColor3 = Globals.BallColor3.NONE;
        }
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

    public double getGoalDistance(Robot robot) {
        LLResult result = ll.getLatestResult();

        double robotYaw = robot.follower.getHeading();
        ll.updateRobotOrientation(robotYaw);

        if (result == null || !result.isValid()) return -1;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                case 22:
                case 23:
                    return -1;
            }
        }

        Pose3D botpose = result.getBotpose_MT2();
        if (botpose == null) return -1;

        Pose currentDetectedPose = PoseConverter.pose2DToPose(new Pose2D(DistanceUnit.INCH, botpose.getPosition().x, botpose.getPosition().y, AngleUnit.RADIANS, robotYaw), InvertedFTCCoordinates.INSTANCE);

        double goalX, goalY;

        if (Globals.side == Globals.Side.RED) {
            goalX = Globals.RED_CASTLE.getX();
            goalY = Globals.RED_CASTLE.getY();
        } else {
            goalX = Globals.BLUE_CASTLE.getX();
            goalY = Globals.BLUE_CASTLE.getY();
        }

        double dx = currentDetectedPose.getX() - goalX;
        double dy = currentDetectedPose.getY() - goalY;

        return Math.hypot(dx, dy);
    }

    public Pose getLLPosition(Robot robot) {
        LLResult result = ll.getLatestResult();
        double robotYaw = robot.follower.getHeading();
        ll.updateRobotOrientation(robotYaw);

        if (result == null || !result.isValid()) return null;

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
        if (botpose == null) return null;

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
        follower.update();
    }
}