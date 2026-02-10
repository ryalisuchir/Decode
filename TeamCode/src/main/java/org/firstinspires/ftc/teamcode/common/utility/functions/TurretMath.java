package org.firstinspires.ftc.teamcode.common.utility.functions;

import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.math.Vector;

public final class TurretMath {

    private TurretMath() {}

    public static final double FIELD_SIZE = 144;
    public static final double GOAL_W = 23.0; // along X
    public static final double GOAL_H = 22.20; // along Y
    public static final double GOAL_CENTER_X_BIAS_CLOSE = 0.0;
    public static final double GOAL_CENTER_Y_BIAS_CLOSE = 5.0;
    public static final double GOAL_CENTER_X_BIAS_FAR = 3;
    public static final double GOAL_CENTER_Y_BIAS_FAR = 3;
    public static final double CLOSE_ZONE_MIN_Y = 50.0;

    public enum CornerGoal {
        LEFT_BLUE,
        RIGHT_RED
    }

    /**
     * Computes turret angle that aims the TURRET PIVOT at the goal,
     * accounting only for turret pivot offset.
     *
     * @param robotX field X position (center of robot)
     * @param robotY field Y position (center of robot)
     * @param robotHeading robot heading (radians)
     *
     * @param pivotX turret pivot offset from robot center (robot frame, inches)
     * @param pivotY turret pivot offset from robot center (robot frame, inches)
     *
     * @param goalX goal field X
     * @param goalY goal field Y
     *
     * @return turret angle relative to robot (radians, [-pi, pi])
     */
    public static double getTurretAngleToGoal(
            double robotX,
            double robotY,
            double robotHeading,
            double pivotX,
            double pivotY,
            double goalX,
            double goalY
    ) {
        double cosH = Math.cos(robotHeading);
        double sinH = Math.sin(robotHeading);

        // Pivot position in field coordinates
        double pivotFieldX = robotX + pivotX * cosH - pivotY * sinH;
        double pivotFieldY = robotY + pivotX * sinH + pivotY * cosH;

        // Vector from pivot to goal
        double dx = goalX - pivotFieldX;
        double dy = goalY - pivotFieldY;

        // Absolute angle to goal
        double angleToGoal = Math.atan2(dy, dx);

        // Turret angle relative to robot
        double turretAngle = angleToGoal - robotHeading;

        // Normalize to [-pi, pi]
        return Math.atan2(
                Math.sin(turretAngle),
                Math.cos(turretAngle)
        );
    }

    /**
     * Computes turret angle to the center of a corner goal (top-left or top-right).
     * Uses the midpoint of the triangle's legs as "goal center".
     */
    public static double getTurretAngleToCornerGoal(
            double robotX,
            double robotY,
            double robotHeading,
            double pivotX,
            double pivotY,
            CornerGoal goal
    ) {
        double[] g = getCornerGoalCenter(goal, robotY);
        return getTurretAngleToGoal(
                robotX,
                robotY,
                robotHeading,
                pivotX,
                pivotY,
                g[0],
                g[1]
        );
    }

    public static double getTurretAngleToObelisk(
            double robotX,
            double robotY,
            double robotHeading,
            double pivotX,
            double pivotY
    ) {
        double cosH = Math.cos(robotHeading);
        double sinH = Math.sin(robotHeading);

        // Pivot position in field coordinates
        double pivotFieldX = robotX + pivotX * cosH - pivotY * sinH;
        double pivotFieldY = robotY + pivotX * sinH + pivotY * cosH;

        // Obelisk field position (72, 144)
        double dx = 72 - pivotFieldX;
        double dy = 144 - pivotFieldY;

        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeading;

        return Math.atan2(
                Math.sin(turretAngle),
                Math.cos(turretAngle)
        );
    }

    public static double[] getCornerGoalCenter(CornerGoal goal) {
        return getCornerGoalCenter(goal, CLOSE_ZONE_MIN_Y);
    }

    public static double[] getCornerGoalCenter(CornerGoal goal, double robotY) {
        double xBias = (robotY >= CLOSE_ZONE_MIN_Y) ? GOAL_CENTER_X_BIAS_CLOSE : GOAL_CENTER_X_BIAS_FAR;
        double yBias = (robotY >= CLOSE_ZONE_MIN_Y) ? GOAL_CENTER_Y_BIAS_CLOSE : GOAL_CENTER_Y_BIAS_FAR;
        if (goal == CornerGoal.LEFT_BLUE) {
            double gx = (GOAL_W / 2.0) + xBias;
            double gy = FIELD_SIZE - GOAL_H / 2.0 + yBias;
            return new double[]{gx, gy};
        } else {
            double gx = FIELD_SIZE - (GOAL_W / 2.0) - xBias;
            double gy = FIELD_SIZE - GOAL_H / 2.0 + yBias;
            return new double[]{gx, gy};
        }
    }


    public static double getDistanceToGoalPinpoint(Follower f, double gX, double gY) {
        double dxOdo = f.getPose().getX() - gX;
        double dyOdo = f.getPose().getY() - gY;
        return Math.hypot(dxOdo, dyOdo);
    }

    public static Vector getVectorToGoalPinpoint(Follower f, double gX, double gY) {
        double dxOdo = f.getPose().getX() - gX;
        double dyOdo = f.getPose().getY() - gY;
        return new Vector(dxOdo, dyOdo);
    }
}
