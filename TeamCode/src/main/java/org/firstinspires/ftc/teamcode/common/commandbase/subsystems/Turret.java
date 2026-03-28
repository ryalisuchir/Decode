package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;


import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.TimeOfFlightLUT;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.math.Vector;
import org.firstinspires.ftc.teamcode.common.utility.turret.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.RedTurretLUT;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();
    private final TimeOfFlightLUT timeOfFlightLUT = new TimeOfFlightLUT();
    private double lastPos = -1;

    private final Follower follower;
    private final G.Side side;
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
            {113.20010688361221, 116.22548020730808, -92.26302176142181, 0.90},
            {109.89180917814961, 103.30115611158958, 155.913930297175, 0.18},
            {95.79892616572343, 130.65181702140748, -142.64770851377259, 0.97},
            {99.22594926488681, 111.25804510642226, 167.17057382, 0.10},
            {98.31302, 94.83, 178.98, 0.10},
            {97.01580378014273, 82.47829647514764, -179.7512495193844, 0.11},
            {84.51690914124016, 129.946029542938, -157.446119, 1.0},
            {86.34747362512303, 112.7660748339075, 167.10374725213683, 0.05},
            {86.34747362512303, 98.933, 178.70, 0.05},
            {83.6079755167323, 83.96158533772146, -170.93130330074, 0.05},
            {81.44640824926182, 71.24404546782726, 176.8817231245220, 0.11},
            {75.05744994156004, 65.32487523837352, 148.57800660025782, 0.19},
            {61.112344096026085, 75.14949287955217, 136.5366, 0.19},
            {60.07, 79.255, 131.43, 0.20},
            {63.27960157018947, 94.55749031126969, 124.672180580673, 0.20},
            {66.1120, 107.0845, 164.1125, 0.05},
            {67.08948330616388, 119.9785079355315, 156.75718545210475, 0.05},
            {66.13578736312746, 131.61878690944883, -153.6146563961169, 0.98},
            {50.058060293122544, 95.41877806656005, 138.3, 0.15},
            {55.406989150159944, 116.68250376783956, -155.83412636691256, 1.0},
            {56.110147791584644, 132.2154416061762, -167.3209609281216, 1.0},
            {31.819085549181843, 115.37065160556102, -13.09422737481773, 0.56},
            {34.047594445896905, 128.9848844272884, -31.299495550839758, 0.59},
            {27.0, 123.0, 1.0, 0.52},
            {78.27325775867372, 51.106029120017226, 152.13760066734497, 0.20}
    };

    // Seed blue points from mirrored red points so blue uses the same algorithm path.
    // Replace with measured blue points for best accuracy.
    private static final double[][] BLUE_FAR_MODEL_POINTS = {
            {52.1045, 11.0075, 107.44, 0.5},
            {66.256, 9.48, 110.27, 0.5},
            {62.857, 19.13, 115.89, 0.5},
            {88.57, 11.43, 121, 0.5},
            {75.61, 21.949, 119, 0.5},
            {68.109, 26.334, -79.24, 0},
            {64.37, 17.99, -83, 0},
            {82.08, 23.128, -74.51, 0},
            {65.06, 13.17, -114.59, 0.1},
            {75.49, 24.36, -108.45, 0.1},
            {74.923, 13.64, -110, 0.1},
            {90.94, 11.58, -105.09, 0.1},
            {91.237, 12.76, -138.44, 0.2},
            {81.46, 12.55, -141.151, 0.2},
            {76.09, 23.39, -140.45, 0.2},
            {67.97, 15.34, -146.57, 0.2},
            {56.32, 10.99, -150.84, 0.2},
            {46.06, 10.579, -156.86, 0.2},
            {95.821, 13.596, -135.59, 0.2},
            {98.39, 11.773, -168, 0.3},
            {87.93, 10.73, -173.46, 0.3},
            {79.326, 20.1956, -172.56, 0.3},
            {75.913, 31.08, -171.6, 0.3},
            {77.7, 10.48, -174.786, 0.3},
            {65.885, 18.52, -179.01, 0.3},
            {64.403, 9.37, 179.5, 0.3},
            {51.823, 10.415, 173.38, 0.3},
            {99.17, 11.087, 156.66, 0.4},
            {89.63, 11.395, 153.56, 0.4},
            {85.132, 19.31, 153.68, 0.4},
            {77.54, 11.30, 150.38, 0.4},
            {73.89, 30.92, 153.165, 0.4},
            {63.94, 15.86, 142.80, 0.4},
            {56.18, 12.01, 142.176, 0.4},
            {100.921, 9.34, 93.7, 0.6},
            {90.68, 11.605, 92.5, 0.6},
            {84.67, 21.365, 91.504, 0.6},
            {76.99, 29.705, 92.02, 0.6},
            {77.89, 15.25, 88.17, 0.6},
            {67.51, 24.633, 86.59, 0.6},
            {66.656, 11.307, 83.32, 0.6},
            {56.53, 10.06, 77.41, 0.6},
            {98.93, 11.957, 61.7, 0.7},
            {89.54, 11.567, 56.63, 0.7},
            {79.023, 18.68, 53.92, 0.7},
            {76.07, 13.68, 54.12, 0.7},
            {67.161, 19.00, 47.51, 0.7},
            {61.01, 14.64, 45.23, 0.7},
            {50.257, 13.49, 41.40, 0.7},
            {97.36, 10.59, 24.42, 0.8},
            {87.117, 21.852, 22.289, 0.8},
            {82.98, 12.230, 18.124, 0.8},
            {71.776, 24.616, 13.920, 0.8},
            {69.5827, 13.73, 11.98, 0.8},
            {64.846, 22.243, 10.431, 0.8},
            {60.725, 13.735, 8.62, 0.8},
            {49.757, 14.87, 4.14, 0.8},
            {99.41, 8.002, -16.04, 0.9},
            {84.914, 11.397, -20.20, 0.9},
            {85.15, 18.60, -19.52, 0.9},
            {73.35, 14.141, -24.82, 0.9},
            {70.725, 25.63, -27.15, 0.9},
            {63.12, 18.22, -29.817, 0.9},
            {55.116, 18.41, -34.35, 0.9},
            {91.94, 11.824, -53.25, 1},
            {84.32, 19.296, -56.213, 1},
            {78.24, 16.24, -59.5, 1},
            {75.117, 29.82 -56.557, 1},
            {66.6519, 17.908 -63.998, 1},
            {60.421, 22.167 -66.63, 1},
            {54.89, 21.717 -69.27, 1}
    };

    private static final double[][] BLUE_CLOSE_MODEL_POINTS = {
            {31, 112.64, 10.25, 0.9},
            {39.77, 122.99, 36.01, 0.9},
            {39.77, 122.99, 36.01, 0.9},
            {42.778, 106.18, 16.64, 0.9},
            {47.5922, 119.08, 33.52, 0.9},
            {47.008, 92.1136, 5.38, 0.9},
            {48.91, 92.084, 99.954, 0.6},
            {48.91, 92.084, 99.954, 0.6},
            {71.878, 85.09, 107.62, 0.6},
            {70.01, 85.12, 45.87, 0.8},
            {63.307, 119.673, 70.47, 0.8},
            {79.52, 85.28, 50.548, 0.8},
            {92.782, 95.94, 58.72, 0.8},
            {99.04, 106.33, 67.08, 0.8},
            {103.781, 126.79, 83.38, 0.79},
            {54.339, 94.78, 127.122, 0.5},
            {75.174, 99.442, 150.89, 0.5},
            {73.368, 113.00, 160.86, 0.5},
            {70.79, 129.05, 172.16, 0.5},
            {90.901, 95.48, 153.27, 0.5},
            {104.559, 106.48, 163.51, 0.5},
            {40.39, 105.822, 163.03, 0.4},
            {55.08, 91.93, 163.325, 0.4},
            {57.30, 99.537, 169.20, 0.4},
            {67.3, 72.319, 160.44, 0.4},
            {92.88, 85.99, 176.367, 0.4},
            {96.66, 114.98, -167.01, 0.4},
            {110.053, 125.203, -158.99, 0.4},
            {52.5937, 88.93, -167.85, 0.3},
            {41.332, 102.935, -170, 0.3},
            {62.595, 76.63, -168.45, 0.3},
            {61.13, 116.91, -141.18, 0.3},
            {79.46, 120.57, -132.57, 0.3},
            {94.7477, 117.71, -135.00, 0.3},
            {93.631, 92.30, -148.67, 0.3},
            {41.52, 116.66, -120.38, 0.2},
            {49.34, 95.399, -131.97, 0.2},
            {58.806, 109.37, -116.98, 0.2},
            {68.565, 78.193, -129.902, 0.2},
            {88.805, 86.64, -117.53, 0.2},
            {88.805, 110.56, -105, 0.2},
            {47.517, 97.69, -98.32, 0.1},
            {63.65, 106.32, -84.732, 0.1},
            {69.977, 101.255, -79.211, 0.1},
            {75.42, 121.065, -59.94, 0.1},
            {85.033, 102.328, -74.26, 0.1},
            {69.2810, 76.55, -91.82, 0.1},
            {41.057, 111.95, -55, 0},
            {52.533, 92.12, -62.17, 0},
            {64.12, 79.40, -61.86, 0},
            {62.686, 105.45, -45.257, 0},
            {74.654, 119.85, -30.67, 0}
    };

    public Turret(
            G.Side side,
            ServoImplEx turret1,
            ServoImplEx turret2,
            Follower follower
    ) {
        this.side = side;
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.cornerGoal = (side == G.Side.BLUE)
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
        return new InstantCommand(() -> G.turretState = G.TurretState.FOLLOWING);
    }

    public InstantCommand blue12Pos() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.335);
        });
    }

    public InstantCommand blue18Pos1() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.04);
        });
    }

    public InstantCommand blue18Pos2() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.25);
        });
    }

    public InstantCommand red12Pos() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.66);
        });
    }

    public InstantCommand redCloseClose() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.97);
        });
    }

    public InstantCommand red18Pos1() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(1);
        });
    }

    public InstantCommand red18Pos2() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.725); //0.58
        });
    }

    public InstantCommand redFarNot() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.62); //0.58
        });
    }

    public InstantCommand redFar() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.71); //0.58
        });
    }

    public InstantCommand reset() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RESET));
    }

    public InstantCommand initClose() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RESET));
    }

    public InstantCommand initFarBlue() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_BLUE_FAR_READ));
    }

    public InstantCommand initFarRed() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RED_FAR_READ));
    }

    public InstantCommand blueObeliskRead() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.BLUE_CLOSE_OBELISK;
            setPositionOnce(G.TURRET_BLUE_CLOSE_READ);
        });
    }

    public InstantCommand redObeliskRead() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.RED_CLOSE_OBELISK;
            setPositionOnce(G.TURRET_RED_CLOSE_READ);
        });
    }

    public double getAngleToGoal() {
        return TurretMath.getTurretAngleToCornerGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                G.pivotX,
                G.pivotY,
                cornerGoal
        );
    }

    public double getBestTurretPosition() {
        double turretAngle = TurretMath.getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                G.pivotX,
                G.pivotY,
                virtualGoalX,
                virtualGoalY
        );

        double servoPosition;
        if (side == G.Side.BLUE) {
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
                G.pivotX,
                G.pivotY,
                virtualGoalX,
                virtualGoalY
        );

        double servoPosition = side == G.Side.BLUE
                ? blueTurretLUT.getServoValue(turretAngle)
                : redTurretLUT.getServoValue(turretAngle);

        double y = follower.getPose().getY();
        boolean closeZone = y >= TurretMath.CLOSE_ZONE_MIN_Y;
        double speed = follower.getVelocity().getMagnitude();

        if (side == G.Side.RED) {
            if (closeZone && G.TURRET_RED_CLOSE_MODEL_ENABLED && speed <= G.TURRET_RED_CLOSE_MODEL_MAX_SPEED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        RED_CLOSE_MODEL_POINTS,
                        G.TURRET_RED_CLOSE_MODEL_BLEND,
                        G.TURRET_RED_CLOSE_MODEL_MAX_CORRECTION
                );
            } else if (!closeZone && G.TURRET_RED_FAR_MODEL_ENABLED && speed <= G.TURRET_RED_FAR_MODEL_MAX_SPEED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        RED_FAR_MODEL_POINTS,
                        G.TURRET_RED_FAR_MODEL_BLEND,
                        G.TURRET_RED_FAR_MODEL_MAX_CORRECTION
                );
            }
        } else {
            if (closeZone && G.TURRET_BLUE_CLOSE_MODEL_ENABLED && speed <= G.TURRET_BLUE_CLOSE_MODEL_MAX_SPEED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        BLUE_CLOSE_MODEL_POINTS,
                        G.TURRET_BLUE_CLOSE_MODEL_BLEND,
                        G.TURRET_BLUE_CLOSE_MODEL_MAX_CORRECTION
                );
            } else if (!closeZone && G.TURRET_BLUE_FAR_MODEL_ENABLED && speed <= G.TURRET_BLUE_FAR_MODEL_MAX_SPEED) {
                servoPosition = applySampleRegressionCorrection(
                        servoPosition,
                        BLUE_FAR_MODEL_POINTS,
                        G.TURRET_BLUE_FAR_MODEL_BLEND,
                        G.TURRET_BLUE_FAR_MODEL_MAX_CORRECTION
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
                G.pivotX,
                G.pivotY
        );

        double servoPosition;
        if (side == G.Side.BLUE) {
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

        if (G.TURRET_TOF_COMP_ENABLED) {
            Vector velocity = follower.getVelocity();
            double speed = velocity.getMagnitude();
            if (speed >= G.TURRET_SOTM_MIN_SPEED) {
                double distance = TurretMath.getDistanceToGoalPinpoint(follower, baseGoalX, baseGoalY);
                double tof = timeOfFlightLUT.get(distance) * G.TURRET_TOF_COMP_GAIN;

                virtualGoalX = baseGoalX - velocity.getXComponent() * tof;
                virtualGoalY = baseGoalY - velocity.getYComponent() * tof;
            }

            // Add rotational lead for in-place turns: v_rot = omega x r (field frame).
            double omega = follower.getAngularVelocity(); // rad/s
            if (Math.abs(omega) > 1e-6) {
                double heading = follower.getPose().getHeading();
                double cosH = Math.cos(heading);
                double sinH = Math.sin(heading);

                double pivotFieldXFromCenter = G.pivotX * cosH - G.pivotY * sinH;
                double pivotFieldYFromCenter = G.pivotX * sinH + G.pivotY * cosH;

                double vRotX = -omega * pivotFieldYFromCenter;
                double vRotY = omega * pivotFieldXFromCenter;

                double distance = TurretMath.getDistanceToGoalPinpoint(follower, baseGoalX, baseGoalY);
                double tof = timeOfFlightLUT.get(distance) * G.TURRET_TOF_COMP_GAIN;

                virtualGoalX -= vRotX * tof;
                virtualGoalY -= vRotY * tof;
            }
        }

        if (G.turretState == G.TurretState.FOLLOWING) {
            followGoal();
        }

        if (G.turretState == G.TurretState.SET_POSITION) {
            return;
        }

        if (G.turretState == G.TurretState.BLUE_CLOSE_OBELISK || G.turretState == G.TurretState.RED_CLOSE_OBELISK) {
            followObelisk();
        }
    }

    private double applySampleRegressionCorrection(
            double baseServo,
            double[][] modelPoints,
            double blend,
            double maxCorrection
    ) {
        // Fit a compact equation to sample residuals (target - base model).
        // Basis: [1, xN, yN, sin(h), cos(h), xN*yN]
        final int m = 6;
        double[][] ata = new double[m][m];
        double[] atb = new double[m];

        for (double[] p : modelPoints) {
            double[] phi = featuresFor(p[0], p[1], p[2]);
            double sampleBase = getBaseServoForPose(p[0], p[1], Math.toRadians(p[2]));
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
        double[][] out = new double[src.length][4];
        for (int i = 0; i < src.length; i++) {
            double x = src[i][0];
            double y = src[i][1];
            double h = src[i][2];
            double s = src[i][3];
            out[i][0] = 144.0 - x;
            out[i][1] = y;
            out[i][2] = wrapDegStatic(180.0 - h);
            out[i][3] = clampStatic(1.0 - s, 0.0, 1.0);
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

    private double getBaseServoForPose(double robotX, double robotY, double headingRad) {
        double[] center = TurretMath.getCornerGoalCenter(cornerGoal, robotY);
        double angle = TurretMath.getTurretAngleToGoal(
                robotX,
                robotY,
                headingRad,
                G.pivotX,
                G.pivotY,
                center[0],
                center[1]
        );
        return side == G.Side.BLUE ? blueTurretLUT.getServoValue(angle) : redTurretLUT.getServoValue(angle);
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
