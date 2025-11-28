package org.firstinspires.ftc.teamcode.opmode.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class LeoTesting extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLMotor = null;
    private DcMotor BLMotor = null;
    private DcMotor FRMotor = null;
    private DcMotor BRMotor = null;
    private DcMotor shooter1 = null;
    private DcMotor shooter2 = null;
    private DcMotorEx transfer = null;
    private DcMotor intake = null;
    private double power = 1;
    Robot robot;

    public static double kicker1 = Globals.KICKER1_RESET;
    public static double kicker2 = Globals.KICKER2_RESET;
    public static double kicker3 = Globals.KICKER3_RESET;
    public static double turret = Globals.TURRET_RESET;
    public static double hood = Globals.HOOD_LOWERED;

    @Override
    public void runOpMode() {
        shooter1 = hardwareMap.get(DcMotor.class, "shooterSpinner1");
        shooter2 = hardwareMap.get(DcMotor.class, "shooterSpinner2");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        intake = hardwareMap.get(DcMotor.class, "intake");
        FLMotor = hardwareMap.get(DcMotor.class, "leftFront");
        FRMotor = hardwareMap.get(DcMotor.class, "rightFront");
        BLMotor = hardwareMap.get(DcMotor.class, "leftRear");
        BRMotor = hardwareMap.get(DcMotor.class, "rightRear");

        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);

        FLMotor.setDirection(DcMotor.Direction.FORWARD);
        BLMotor.setDirection(DcMotor.Direction.FORWARD);
        FRMotor.setDirection(DcMotor.Direction.REVERSE);
        BRMotor.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

//            robot.kicker1.setPosition(kicker1);
//            robot.kicker2.setPosition(kicker2);
//            robot.kicker3.setPosition(kicker3);
//            robot.turret1.setPosition(turret);
//            robot.turret2.setPosition(turret);
//            robot.hood.setPosition(hood);

            telemetry.addData("Transfer Vel: ", transfer.getVelocity(AngleUnit.DEGREES));
            telemetry.update();
            robot.clearCache();

            if(gamepad1.cross) {
                power = .75;
            }
            if(gamepad1.square) {
                power = .65;
            }
            shooter1.setPower(gamepad1.left_trigger*power);
            shooter2.setPower(gamepad1.left_trigger*power);
            //   transfer.setPower(gamepad1.left_stick_y);
            intake.setPower(gamepad1.right_trigger);

            double lateral = -gamepad1.left_stick_y;
            double axial = -gamepad1.left_stick_x;
            double yaw = -gamepad1.right_stick_x;
            double leftFrontPower = (axial + lateral + yaw);
            double rightFrontPower = (axial - lateral - yaw);
            double leftBackPower = (axial - lateral + yaw);
            double rightBackPower = (axial + lateral - yaw);

            if(gamepad1.circle) {
                transfer.setPower(1);
            }
            if(gamepad1.triangle) {
                transfer.setPower(0);
            }


            FLMotor.setPower(leftFrontPower);
            FRMotor.setPower(rightFrontPower);
            BLMotor.setPower(leftBackPower);
            BRMotor.setPower(rightBackPower);


        }}
}
