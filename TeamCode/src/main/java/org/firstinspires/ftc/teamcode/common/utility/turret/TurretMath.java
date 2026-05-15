package org.firstinspires.ftc.teamcode.common.utility.turret;

import static org.firstinspires.ftc.teamcode.common.TurretConfig.closeOffsetX;
import static org.firstinspires.ftc.teamcode.common.TurretConfig.closeOffsetY;
import static org.firstinspires.ftc.teamcode.common.TurretConfig.farOffsetX;
import static org.firstinspires.ftc.teamcode.common.TurretConfig.farOffsetY;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.common.TurretConfig;

public final class TurretMath {

    private TurretMath() {}

    public static final double FIELD_SIZE = 144.0;

    public static final double OBELISK_X  = 72.0;
    public static final double OBELISK_Y  = 144.0;

    public enum CornerGoal {
        LEFT_BLUE,
        RIGHT_RED
    }

    public static double[] getCornerGoalCenter(CornerGoal goal, double robotX, double robotY) {

        if (goal == CornerGoal.RIGHT_RED) {
//            return new double[]{ 128.82997118155623, 133.3256484149856 };
            return new double[]{ 144, 144 };
        } else {
            return new double[]{ 13.463976945244935, 136.02305475504323 };
        }
    }

    public static double getTurretAngleToGoal(
            double robotX, double robotY, double robotHeading,
            double pivotX,  double pivotY,
            double goalX,   double goalY
    ) {
        double cosH = Math.cos(robotHeading);
        double sinH = Math.sin(robotHeading);

        double pivotFieldX = robotX + pivotX * cosH - pivotY * sinH;
        double pivotFieldY = robotY + pivotX * sinH + pivotY * cosH;

        double dx = goalX - pivotFieldX;
        double dy = goalY - pivotFieldY;

        double turretAngle = Math.atan2(dy, dx) - robotHeading;
        return normalizeRadians(turretAngle);
    }

    public static double getTurretAngleToCornerGoal(
            double robotX, double robotY, double robotHeading,
            double pivotX,  double pivotY,
            CornerGoal goal
    ) {
        double[] g = getCornerGoalCenter(goal, robotX, robotY);
        return getTurretAngleToGoal(robotX, robotY, robotHeading, pivotX, pivotY, g[0], g[1]);
    }

    public static double getTurretAngleToObelisk(
            double robotX, double robotY, double robotHeading,
            double pivotX,  double pivotY
    ) {
        return getTurretAngleToGoal(
                robotX, robotY, robotHeading,
                pivotX, pivotY,
                OBELISK_X, OBELISK_Y
        );
    }

    public static double applyLeadAngle(double staticTurretAngleRad, double leadAngleRad) {
        return normalizeRadians(staticTurretAngleRad + leadAngleRad);
    }

    public static double getTurretAngleSOTM(
            double robotX, double robotY, double robotHeading,
            double pivotX,  double pivotY,
            CornerGoal goal,
            double leadAngleRad
    ) {
        double staticAngle = getTurretAngleToCornerGoal(
                robotX, robotY, robotHeading, pivotX, pivotY, goal
        );
        return applyLeadAngle(staticAngle, leadAngleRad);
    }

    public static double getDistanceToGoal(Follower f, double gX, double gY) {
        return Math.hypot(f.getPose().getX() - gX, f.getPose().getY() - gY);
    }

    public static double getDistanceToGoal(double currX, double currY, double gX, double gY) {
        return Math.hypot(currX - gX, currY - gY);
    }

    public static double[] getVectorToGoal(Follower f, double gX, double gY) {
        return new double[]{ gX - f.getPose().getX(), gY - f.getPose().getY() };
    }

    public static double normalizeRadians(double angleRad) {
        return Math.atan2(Math.sin(angleRad), Math.cos(angleRad));
    }

}
