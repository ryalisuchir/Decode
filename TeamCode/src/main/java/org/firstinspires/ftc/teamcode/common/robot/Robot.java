package org.firstinspires.ftc.teamcode.common.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.seattlesolvers.solverslib.command.CommandScheduler;

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

    public List<LynxModule> allHubs;
    public LynxModule ControlHub;
    public Follower follower;

    DigitalChannel brushlands1pin0, brushlands1pin1; //Close Brushlands (1)
    DigitalChannel brushlands2pin0, brushlands2pin1; //Middle Brushlands (2)
    DigitalChannel brushlands3pin0, brushlands3pin1; //Far Brushlands (3)

    public Robot(HardwareMap hardwareMap, Pose initialPose, boolean autoBoolean) {
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

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterSpinner1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterSpinner2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooterSpinner1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterSpinner2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            if (hub.getDeviceName().equals("Servo Hub") || hub.getDeviceName().equals("pinpoint"))
                return;
            hub.clearBulkCache();
        }
        follower.update();
    }
}