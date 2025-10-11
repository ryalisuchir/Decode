package org.firstinspires.ftc.teamcode.common.robot;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;

import org.firstinspires.ftc.teamcode.opmode.tuning.ColorRangefinder;

import java.util.List;

public class Robot {
    public DcMotorEx leftFront, rightFront, leftRear, rightRear; //Drivetrain motors
    public DcMotorEx shooterSpinner, transfer, intake;

    public ServoImplEx kicker1, kicker2, kicker3;
    public ServoImplEx turret1, turret2;

    public List<LynxModule> allHubs;
    public LynxModule ControlHub;

    public RevColorSensorV3 brushlands1, brushlands2, brushlands3;

    public Robot(HardwareMap hardwareMap, Pose initialPose, boolean autoBoolean) {
        //Drivetrain Motors:
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        //Shooter Motors:
        shooterSpinner = hardwareMap.get(DcMotorEx.class, "shooterSpinner");
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

        //Reversing of Motors:
        rightRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooterSpinner.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Color Sensors:
        brushlands1 = (RevColorSensorV3) hardwareMap.colorSensor.get("color1");
        brushlands2 = (RevColorSensorV3) hardwareMap.colorSensor.get("color2");
        brushlands3 = (RevColorSensorV3) hardwareMap.colorSensor.get("color3");

        kicker3.setDirection(Servo.Direction.REVERSE);

        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
            if (hub.isParent() && LynxConstants.isEmbeddedSerialNumber(hub.getSerialNumber())) {
                ControlHub = hub;
            }
        }
    }

    public void clearCache() {
        for (LynxModule hub : allHubs) {
            if (hub.getDeviceName().equals("Servo Hub") || hub.getDeviceName().equals("pinpoint"))
                return;
            hub.clearBulkCache();
        }
    }
}
