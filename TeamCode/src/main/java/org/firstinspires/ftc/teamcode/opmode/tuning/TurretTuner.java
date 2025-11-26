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
//public class TurretTuner extends OpMode {
//    Robot robot;
//    public static double turretPosition = 0;
//
//    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
//        double goalX = -72;
//        double goalY = 72;
//        double dx = goalX - robotX;
//        double dy = goalY - robotY;
//        double absoluteAngle = Math.atan2(dy, dx);
//
//        double turretAngle = absoluteAngle - robotHeadingRadians;
//        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle)); //normalizes
//
//        return turretAngle; //in radians
//    }
//
//    @Override
//    public void init() {
//        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        telemetry.addLine("Reset all encoders.");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//        robot.turret1.setPosition(turretPosition);
//        robot.turret2.setPosition(turretPosition);
//
//        telemetry.addData("Follower Heading: ", robot.follower.getHeading());
//        telemetry.addData("Turret Reading: ", getTurretAngleToGoal(robot.follower.getPose().getX(), robot.follower.getPose().getY(), robot.follower.getHeading()));
//        telemetry.update();
//        robot.loop(robot);
//    }
//}