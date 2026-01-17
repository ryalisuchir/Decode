package org.firstinspires.ftc.teamcode.common.utility.functions;

import com.pedropathing.follower.Follower;

public final class TurretMath {

    private TurretMath() {}

    /**
     * @param robotX field X position
     * @param robotY field Y position
     * @param robotHeadingRadians robot heading (radians)
     * @param goalX target X position
     * @param goalY target Y position
     * @return turret angle relative to robot (radians, normalized to [-pi, pi])
     */
    public static double getTurretAngleToGoal(
            double robotX,
            double robotY,
            double robotHeadingRadians,
            double goalX,
            double goalY
    ) {
        double dx = goalX - robotX;
        double dy = goalY - robotY;

        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }

    public static double getDistanceToGoalPinpoint(Follower f, double gX, double gY) {
        double dxOdo = f.getPose().getX() - gX;
        double dyOdo = f.getPose().getY() - gY;
        return Math.hypot(dxOdo, dyOdo);
    }

    public static double getDistanceToGoalPinpoint(double rX, double rY, double gX, double gY) {
        double dxOdo = rX - gX;
        double dyOdo = rY - gY;
        return Math.hypot(dxOdo, dyOdo);
    }

}
