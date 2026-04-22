package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.utility.turret.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.tables.TimeOfFlightLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.RedTurretLUT;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();
    private final TimeOfFlightLUT timeOfFlightLUT = new TimeOfFlightLUT();
    private double lastPos = -1;

    private final Follower follower;
    private final Globals.Alliance alliance;
    private final TurretMath.CornerGoal cornerGoal;
    public static double virtualGoalX, virtualGoalY;
    public static double baseGoalX, baseGoalY;
    public double originalGoalX, originalGoalY;

    // Red far regression training points: {x, y, headingDeg, targetServo}
    private static final double[][] RED_FAR_MODEL_POINTS = {
            {98.72, 11.519, 109.79, 0.38},
            {89.589, 10.0, 99.58, 0.42},
            {84.85, 10.26, 33.91, 0.6},
            {83.957, 14.7026, -98.22, 1},
            {76.21, 14.79, -132.74, 0},
            {75, 17.10, -19.31, 0.75},
            {85, 12.94, -15.56, 0.75},
            {87.04158272330217, 16.882499634750246, 39.374828206545835, 0.60},
            {75.729, 17.5947, 32.5, 0.60},
            {72.03863669568159, 11.376966341273992, 59.53315140969257, 0.51},
            {65.43902059239666, 21.24958428810901, 54.4393021946, 0.51},
            {71.56, 28.29, 53.0, 0.51},
            {56.795322688545774, 16.238613579216903, 53.099000513419256, 0.51},
            {52.20891516978347, 11.979338878721704, 49.9900896, 0.51}
    };

    // Red close regression training points: {x, y, headingDeg, targetServo}
    private static final double[][] RED_CLOSE_MODEL_POINTS = {
            //Close:
            {87.86, 87.65, 46.53, 0.03},
            {87.13, 88.16, 30.65, 0.08},
            {85.93, 88.46, 26.05, 0.12},
            {84.46, 90.18, 2.55, 0.17},
            {85.7, 93.07, -8.78, 0.22},
            {81.69, 90.03, -38.12, 0.27},
            {83.15, 91.25, -51.93, 0.32},
            {86.95, 90.26, -75.47, 0.37},
            {85.92, 90.19, -88.05, 0.42},
            {90.23, 91.25, -113.79, 0.47},
            {85.95, 86.96, -126.19, 0.52},
            {86.55, 87.26, -148.01, 0.57},
            {87.79, 87.46, -166.93, 0.62},
            {86.67, 87.85, 174.12, 0.67},
            {85.63, 89.13, 152.69, 0.72},
            {85.66, 88.7, 132.32, 0.77},
            {86.46, 88.51, 113.91, 0.82},
            {88.22, 85.92, 93.41, 0.85},
            {86.01, 83.81, 80.89, 0.9},
            {85.073, 85.94, 61.43, 0.95},
            {88.45, 85.18, 43.94, 1.0},
            //Close Tip:
            {71.02, 69.82, 55.03, 0.03},
            {70.24, 75.47, 33.16, 0.08},
            {66.8, 70.52, 21.62, 0.13},
            {67.83, 73.55, -7.81, 0.18},
            {67.85, 71.46, -19.83, 0.23},
            {67.26, 71.22, -37.34, 0.28},
            {67.21, 72.24, -56.15, 0.33},
            {66.64, 73.75, -72.1, 0.38},
            {68.2, 74.09, -89.69, 0.43},
            {68.51, 72.66, -107.41, 0.48},
            {68.11, 72.23, -125.36, 0.53},
            {67.94, 72.16, -148.95, 0.58},
            {67.98, 72.14, -165.68, 0.63},
            {69.94, 72.47, 173.4, 0.68},
            {68.65, 71.08, 153.92, 0.73},
            {70.4, 70.32, 140.71, 0.78},
            {71.24, 69.42, 128.25, 0.83},
            {71.32, 68.48, 99.3, 0.88},
            {71.69, 67.95, 86.07, 0.92},
            {71.4, 66.47, 64.46, 0.97},
            {71.27, 66.58, 56.68, 1.0},
            //Close Far:
            {48.63, 88.04, 34.29, 0.03},
            {48.13, 88.49, 19.81, 0.08},
            {43.71, 87.69, 13.11, 0.1},
            {49.76, 89.29, -8.21, 0.15},
            {38.05, 89.41, -26.96, 0.2},
            {39.28, 88.76, -41.23, 0.25},
            {40.18, 89.12, -59.12, 0.3},
            {42.33, 87.81, -76.2, 0.35},
            {42.6, 87.02, -90.56, 0.4},
            {43.4, 85.16, -109.16, 0.45},
            {45.23, 86.24, -134.58, 0.5},
            {45.94, 84.33, -157.98, 0.55},
            {45.47, 83.92, -173.99, 0.6},
            {46.23, 84.24, 167.37, 0.65},
            {46.94, 84.46, 149.05, 0.7},
            {47.74, 83.54, 129.36, 0.75},
            {47.89, 82.89, 110.98, 0.8},
            {47.82, 81.65, 91.2, 0.85},
            {43.83, 90.02, 50.27, 0.9},
            {47.13, 84.32, 61.75, 0.95},
            {42.45, 89.01, 20.01, 1.0},
            //Close Opposite:
            {28.34, 106.03, 22.71, 0.03},
            {24.98, 105.15, 5.06, 0.07},
            {25.15, 105.58, -1.55, 0.1},
            {25.92, 106.06, -17.29, 0.15},
            {24.81, 106.105, -38.09, 0.2},
            {25.29, 104.86, -53.74, 0.25},
            {25.57, 105.12, -71.84, 0.3},
            {26.39, 103.37, -88.63, 0.35},
            {27.65, 103.41, -103.46, 0.4},
            {28.78, 102.18, -120.09, 0.45},
            {30.03, 101.15, -145.12, 0.5},
            {31.78, 101.42, -164.15, 0.55},
            {33.45, 100.65, 177.89, 0.6},
            {33.34, 99.48, 162.79, 0.65},
            {34.36, 99.48, 141.91, 0.7},
            {34.4, 99.58, 119.9, 0.75},
            {34.03, 99.46, 103.77, 0.8},
            {34.2, 98.95, 83.55, 0.85},
            {32.66, 99.78, 44.86, 0.9},
            {32.11, 98.9, 28.6, 0.95},
            {31.87, 100.27, 11.77, 1.0},
            //Close Mid:
            {70.62, 115.97, 21.15, 0.03},
            {69.95, 115.03, 11.35, 0.07},
            {70.06, 114.28, 2.03, 0.1},
            {70.93, 115.5, -17.24, 0.15},
            {71.19, 115.97, -35.35, 0.2},
            {72.31, 116.95, -53.49, 0.25},
            {72.74, 116.78, -69.42, 0.3},
            {71.55, 118.35, -92.98, 0.35},
            {70.73, 116.74, -105.25, 0.4},
            {71.54, 116.31, -120.13, 0.45},
            {72.4, 115.33, -140.96, 0.5},
            {72.87, 117.25, -165.22, 0.55},
            {72.85, 116.89, 175.53, 0.6},
            {74.02, 116.4, 153.05, 0.65},
            {74.1, 116.78, 132.08, 0.7},
            {73.89, 116.97, 111.57, 0.75},
            {74.4, 116.47, 95.52, 0.8},
            {74.56, 116.6, 76.61, 0.85},
            {73.57, 116.22, 60.97, 0.9},
            {74.1, 116.1, 46.09, 0.95},
            {74.17, 115.78, 21.36, 1.0}
    };

    private static final double[][] BLUE_FAR_MODEL_POINTS = mirrorModelPoints(RED_FAR_MODEL_POINTS);
    private static final double[][] BLUE_CLOSE_MODEL_POINTS = mirrorModelPoints(RED_CLOSE_MODEL_POINTS);

    public Turret(
            Globals.Alliance alliance,
            ServoImplEx turret1,
            ServoImplEx turret2,
            Follower follower
    ) {
        this.alliance = alliance;
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.cornerGoal = (alliance == Globals.Alliance.BLUE)
                ? TurretMath.CornerGoal.LEFT_BLUE
                : TurretMath.CornerGoal.RIGHT_RED;

        double[] g = TurretMath.getCornerGoalCenter(cornerGoal);
        this.baseGoalX = g[0];
        this.baseGoalY = g[1];
        this.virtualGoalX = g[0];
        this.virtualGoalY = g[1];
        this.originalGoalX = g[0];
        this.originalGoalY = g[1];
    }

    public void setPositionOnce(double pos) {
        if (Math.abs(pos - lastPos) > 0.001) {
            turret1.setPosition(pos);
            turret2.setPosition(pos);
            lastPos = pos;
        }
    }

    public InstantCommand clearCustom() {
        return new InstantCommand(() -> {
            Globals.turretState = Globals.TurretState.RESET;
            setPositionOnce(Globals.Turret.TURRET_FORWARD);
        });
    }

    public InstantCommand setCustom(double servoPosition) {
        return new InstantCommand(() -> {
            Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
            setPositionOnce(servoPosition);
        });
    }

    public InstantCommand reset() {
        return new InstantCommand(() -> {
            Globals.turretState = Globals.TurretState.RESET;
            setPositionOnce(Globals.Turret.TURRET_FORWARD);
        });
    }

    public double getAngleToGoal() {
        return TurretMath.getTurretAngleToCornerGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                Globals.Turret.pivotX,
                Globals.Turret.pivotY,
                cornerGoal
        );
    }

    public double getBestTurretPosition() {
        double turretAngle = TurretMath.getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                Globals.Turret.pivotX,
                Globals.Turret.pivotY,
                virtualGoalX,
                virtualGoalY
        );

        double servoPosition;
        if (alliance == Globals.Alliance.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        return servoPosition;
    }

    public void followGoal() {
        double turretAngle = TurretMath.getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                Globals.Turret.pivotX,
                Globals.Turret.pivotY,
                virtualGoalX,
                virtualGoalY
        );

        double servoPosition = alliance == Globals.Alliance.BLUE
                ? blueTurretLUT.getServoValue(turretAngle)
                : redTurretLUT.getServoValue(turretAngle);

        double y = follower.getPose().getY();
        boolean closeZone = y >= TurretMath.CLOSE_ZONE_MIN_Y;

        if (alliance == Globals.Alliance.RED) {
            if (closeZone && Globals.Turret.TURRET_RED_CLOSE_MODEL_ENABLED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        RED_CLOSE_MODEL_POINTS,
                        Globals.Turret.TURRET_RED_CLOSE_MODEL_BLEND,
                        Globals.Turret.TURRET_RED_CLOSE_MODEL_MAX_CORRECTION,
                        virtualGoalX,
                        virtualGoalY
                );
            } else if (!closeZone && Globals.Turret.TURRET_RED_FAR_MODEL_ENABLED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        RED_FAR_MODEL_POINTS,
                        Globals.Turret.TURRET_RED_FAR_MODEL_BLEND,
                        Globals.Turret.TURRET_RED_FAR_MODEL_MAX_CORRECTION,
                        virtualGoalX,
                        virtualGoalY
                );
            }
        } else {
            if (closeZone && Globals.Turret.TURRET_BLUE_CLOSE_MODEL_ENABLED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        BLUE_CLOSE_MODEL_POINTS,
                        Globals.Turret.TURRET_BLUE_CLOSE_MODEL_BLEND,
                        Globals.Turret.TURRET_BLUE_CLOSE_MODEL_MAX_CORRECTION,
                        virtualGoalX,
                        virtualGoalY
                );
            } else if (!closeZone && Globals.Turret.TURRET_BLUE_FAR_MODEL_ENABLED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        BLUE_FAR_MODEL_POINTS,
                        Globals.Turret.TURRET_BLUE_FAR_MODEL_BLEND,
                        Globals.Turret.TURRET_BLUE_FAR_MODEL_MAX_CORRECTION,
                        virtualGoalX,
                        virtualGoalY
                );
            }
        }

        setPositionOnce(servoPosition);
    }

    public void followObelisk() {
        double turretAngle = TurretMath.getTurretAngleToObelisk(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                Globals.Turret.pivotX,
                Globals.Turret.pivotY
        );

        double servoPosition;
        if (alliance == Globals.Alliance.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        setPositionOnce(servoPosition);
    }

    public void loop() {
        double robotY = follower.getPose().getY();

        double[] goalCenter = TurretMath.getCornerGoalCenter(cornerGoal, robotY);
        baseGoalX = goalCenter[0];
        baseGoalY = goalCenter[1];
        originalGoalX = baseGoalX;
        originalGoalY = baseGoalY;

        virtualGoalX = baseGoalX;
        virtualGoalY = baseGoalY;

        if (Globals.SOTM.TURRET_TOF_COMP_ENABLED) {
            Vector velocity = follower.getVelocity();
            double speed = velocity.getMagnitude();
            if (speed >= Globals.SOTM.TURRET_MIN_SPEED) {
                double distance = TurretMath.getDistanceToGoalPinpoint(follower, baseGoalX, baseGoalY);
                double tof = timeOfFlightLUT.get(distance) * Globals.SOTM.TURRET_LINEAR_GAIN;

                virtualGoalX = baseGoalX - velocity.getXComponent() * tof;
                virtualGoalY = baseGoalY - velocity.getYComponent() * tof;
            }

            // Add rotational lead for in-place turns: v_rot = omega x r (field frame).
            double omega = follower.getAngularVelocity(); // rad/s
            if (Math.abs(omega) > 1e-6) {
                double heading = follower.getPose().getHeading();
                double cosH = Math.cos(heading);
                double sinH = Math.sin(heading);

                double pivotFieldXFromCenter = Globals.Turret.pivotX * cosH - Globals.Turret.pivotY * sinH;
                double pivotFieldYFromCenter = Globals.Turret.pivotX * sinH + Globals.Turret.pivotY * cosH;

                double vRotX = -omega * pivotFieldYFromCenter;
                double vRotY = omega * pivotFieldXFromCenter;

                double distance = TurretMath.getDistanceToGoalPinpoint(follower, baseGoalX, baseGoalY);
                double tof = timeOfFlightLUT.get(distance) * Globals.SOTM.TURRET_LINEAR_GAIN;

                virtualGoalX -= vRotX * tof * Globals.SOTM.TURRET_ROTATIONAL_GAIN;
                virtualGoalY -= vRotY * tof * Globals.SOTM.TURRET_ROTATIONAL_GAIN;
            }
        }

        if (Globals.turretState == Globals.TurretState.FOLLOWING_GOAL) {
            followGoal();
        }

        if (Globals.turretState == Globals.TurretState.SET_POSITION) {
            return;
        }

        if (Globals.turretState == Globals.TurretState.FOLLOWING_OBELISK) {
            followObelisk();
        }
    }

    private double applySampleRegressionCorrection(
            double baseServo,
            double[][] modelPoints,
            double blend,
            double maxCorrection,
            double vGoalX,
            double vGoalY
    ) {
        final int m = 6;
        double[][] ata = new double[m][m];
        double[] atb = new double[m];

        for (double[] p : modelPoints) {
            double[] phi = featuresFor(p[0], p[1], p[2]);
            double[] trainingGoal = TurretMath.getCornerGoalCenter(cornerGoal, p[1]);
            double sampleBase = getBaseServoForPose(p[0], p[1], Math.toRadians(p[2]),
                    trainingGoal[0], trainingGoal[1]);
            double delta = p[3] - sampleBase;

            for (int i = 0; i < m; i++) {
                atb[i] += phi[i] * delta;
                for (int j = 0; j < m; j++) {
                    ata[i][j] += phi[i] * phi[j];
                }
            }
        }

        for (int i = 0; i < m; i++) {
            ata[i][i] += 1e-3; // ridge stabilization
        }

        double[] coeff = solveLinearSystem(ata, atb);
        if (coeff == null) {
            return clamp(baseServo, 0.0, 1.0);
        }

        double headingDeg = Math.toDegrees(follower.getPose().getHeading());
        double[] phiNow = featuresFor(follower.getPose().getX(), follower.getPose().getY(), headingDeg);
        double deltaNow = 0.0;
        for (int i = 0; i < m; i++) {
            deltaNow += coeff[i] * phiNow[i];
        }

        deltaNow *= blend;
        deltaNow = clamp(deltaNow, -maxCorrection, maxCorrection);
        return clamp(baseServo + deltaNow, 0.0, 1.0);
    }

    private static double[][] mirrorModelPoints(double[][] src) {
        // Turret pivot offset in robot-local frame (forward = +y, left = +x)
        // Offset is (-2.5, 0): 2.5 inches toward the back of the robot
        final double TURRET_LOCAL_X = -2.5;
        final double TURRET_LOCAL_Y = 0.0;

        double[][] out = new double[src.length][4];
        for (int i = 0; i < src.length; i++) {
            double robotX     = src[i][0];
            double robotY     = src[i][1];
            double headingDeg = src[i][2];
            double servo      = src[i][3];

            double headingRad = Math.toRadians(headingDeg);
            double cosH = Math.cos(headingRad);
            double sinH = Math.sin(headingRad);

            // Rotate turret offset into field frame and find turret pivot position
            double turretFieldX = robotX + (TURRET_LOCAL_X * cosH - TURRET_LOCAL_Y * sinH);
            double turretFieldY = robotY + (TURRET_LOCAL_X * sinH + TURRET_LOCAL_Y * cosH);

            // Mirror turret pivot across x = 72
            double mirroredTurretX = 144.0 - turretFieldX;
            double mirroredTurretY = turretFieldY;

            // Mirror heading
            double mirroredHeadingDeg = wrapDegStatic(180.0 - headingDeg);
            double mirroredHeadingRad = Math.toRadians(mirroredHeadingDeg);
            double cosMH = Math.cos(mirroredHeadingRad);
            double sinMH = Math.sin(mirroredHeadingRad);

            // Back-calculate robot center from mirrored turret position and mirrored heading
            double rotatedOffsetX = TURRET_LOCAL_X * cosMH - TURRET_LOCAL_Y * sinMH;
            double rotatedOffsetY = TURRET_LOCAL_X * sinMH + TURRET_LOCAL_Y * cosMH;

            double mirroredRobotX = mirroredTurretX - rotatedOffsetX;
            double mirroredRobotY = mirroredTurretY - rotatedOffsetY;

            // Servo inverts because turret now aims at the opposite corner
            double mirroredServo = clampStatic(1.0 - servo, 0.0, 1.0);

            out[i][0] = mirroredRobotX;
            out[i][1] = mirroredRobotY;
            out[i][2] = mirroredHeadingDeg;
            out[i][3] = mirroredServo;
        }
        return out;
    }

    private static double wrapDegStatic(double a) {
        while (a > 180.0) a -= 360.0;
        while (a < -180.0) a += 360.0;
        return a;
    }

    private static double clampStatic(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private double[] featuresFor(double x, double y, double headingDeg) {
        double xN = (x - 75.0) / 25.0;
        double yN = (y - 18.0) / 12.0;
        double h = Math.toRadians(headingDeg);
        return new double[]{1.0, xN, yN, Math.sin(h), Math.cos(h), xN * yN};
    }

    private double getBaseServoForPose(double robotX, double robotY, double headingRad,
                                       double goalX, double goalY) {
        double angle = TurretMath.getTurretAngleToGoal(
                robotX, robotY, headingRad,
                Globals.Turret.pivotX, Globals.Turret.pivotY,
                goalX, goalY
        );
        return alliance == Globals.Alliance.BLUE
                ? blueTurretLUT.getServoValue(angle)
                : redTurretLUT.getServoValue(angle);
    }

    private double[] solveLinearSystem(double[][] a, double[] b) {
        int n = b.length;
        double[][] m = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, m[i], 0, n);
            m[i][n] = b[i];
        }

        for (int col = 0; col < n; col++) {
            int pivot = col;
            for (int row = col + 1; row < n; row++) {
                if (Math.abs(m[row][col]) > Math.abs(m[pivot][col])) pivot = row;
            }
            if (Math.abs(m[pivot][col]) < 1e-10) return null;

            if (pivot != col) {
                double[] tmp = m[pivot];
                m[pivot] = m[col];
                m[col] = tmp;
            }

            double inv = 1.0 / m[col][col];
            for (int j = col; j <= n; j++) m[col][j] *= inv;

            for (int row = 0; row < n; row++) {
                if (row == col) continue;
                double factor = m[row][col];
                for (int j = col; j <= n; j++) {
                    m[row][j] -= factor * m[col][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++) x[i] = m[i][n];
        return x;
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

}