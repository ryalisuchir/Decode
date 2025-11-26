//package org.firstinspires.ftc.teamcode.opmode.tuning;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.seattlesolvers.solverslib.controller.PIDFController;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.teamcode.common.robot.Globals;
//import org.firstinspires.ftc.teamcode.common.robot.Robot;
//
//@TeleOp
//@Config
//public class ShooterTuner extends OpMode {
//    Robot robot;
//    static double setVelocity = 0;
//    static double hoodPosition = Globals.HOOD_LOWERED;
//
//    public static double P = 0.0001;
//    public static double I = 0.0;
//    public static double D = 0.0;
//    public static double F = 0.0008;
//
//    public PIDFController controller;
//
//    public double power;
//
//    @Override
//    public void init() {
//        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
//        controller = new PIDFController(P, I, D, F);
//        controller.setTolerance(Globals.SHOOTER_VELOCITY_TOLERANCE);
//        controller.setSetPoint(0);
//    }
//
//    @Override
//    public void loop() {
//        controller.setP(P);
//        controller.setI(I);
//        controller.setI(D);
//        controller.setI(F);
//
//        controller.setSetPoint(setVelocity);
//        power = controller.calculate(robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES), setVelocity);
//
//        robot.hood.setPosition(hoodPosition);
//
//        telemetry.addData("Limelight Distance from Blue Goal: ", robot.getGoalDistance(robot.follower));
//        telemetry.addData("Current Velocity: ", robot.shooterSpinner2.getVelocity(AngleUnit.DEGREES));
//        telemetry.addData("Current Power: ", robot.shooterSpinner2.getPower());
//        telemetry.addData("Hood Value: ", robot.hood.getPosition());
//    }
//}