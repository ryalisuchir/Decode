//package org.firstinspires.ftc.teamcode.opmode.tuning;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.teamcode.common.robot.Globals;
//import org.firstinspires.ftc.teamcode.common.robot.Robot;
//
//@Autonomous
//@Config
//public class ServoTuner extends OpMode {
//    Robot robot;
//    public static double kicker1 = Globals.KICKER1_RESET;
//    public static double kicker2 = Globals.KICKER2_RESET;
//    public static double kicker3 = Globals.KICKER3_RESET;
//    public static double turret = Globals.TURRET_RESET;
//    public static double hood = Globals.HOOD_LOWERED;
//
//    public static double transferSpeed = 0;
//    public static double shooterSpeed = 0;
//    public static double intakeSpeed = 0;
//
//    public static double dtSpeed1 = 0;
//    public static double dtSpeed2 = 0;
//    public static double dtSpeed3 = 0;
//    public static double dtSpeed4 = 0;
//
//    @Override
//    public void init() {
//        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        telemetry.addLine("Reset all encoders.");
//        telemetry.update();
//
//    }
//
//    @Override
//    public void loop() {
//        robot.kicker1.setPosition(kicker1);
//        robot.kicker2.setPosition(kicker2);
//        robot.kicker3.setPosition(kicker3);
//        robot.turret1.setPosition(turret);
//        robot.turret2.setPosition(turret);
//        robot.hood.setPosition(hood);
//
//        robot.leftFront.setPower(dtSpeed1);
//        robot.rightFront.setPower(dtSpeed2);
//        robot.leftRear.setPower(dtSpeed3);
//        robot.rightRear.setPower(dtSpeed4);
//
//        robot.transfer.setPower(transferSpeed);
//        robot.shooterSpinner1.setPower(shooterSpeed);
//        robot.shooterSpinner2.setPower(shooterSpeed);
//        robot.intake.setPower(intakeSpeed);
//
//        robot.getObeliskFiducial();
//
//        telemetry.addData("Transfer Motor Velocity:", robot.transfer.getVelocity(AngleUnit.DEGREES));
//        telemetry.addData("Shooter 1 Motor Velocity:", robot.shooterSpinner1.getVelocity(AngleUnit.DEGREES));
//        telemetry.addData("Shooter 2 Motor Velocity:", robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES));
//        telemetry.addData("Obelisk:", Globals.obeliskOptions);
////        telemetry.addData("Distance to tag:", robot.getGoalDistance(robot.follower));
////        telemetry.addData("Pinpoint x:", robot.follower.getPose().getX());
////        telemetry.addData("Pinpoint y:", robot.follower.getPose().getY());
////        telemetry.addData("Limelight x:", robot.getLLPosition(robot.follower).getX());
////        telemetry.addData("Limelight y:", robot.getLLPosition(robot.follower).getY());
//
//        telemetry.update();
////        robot.loop(robot);
//    }
//}