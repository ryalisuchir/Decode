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
            {87.04158272330217, 16.882499634750246, 39.374828206545835, 0.60},
            {75.729, 17.5947, 32.5, 0.60},
            {72.03863669568159, 11.376966341273992, 59.53315140969257, 0.51},
            {65.43902059239666, 21.24958428810901, 54.4393021946, 0.51},
            {71.56, 28.29, 53.0, 0.51},
            {56.795322688545774, 16.238613579216903, 53.099000513419256, 0.51},
            {52.20891516978347, 11.979338878721704, 49.9900896, 0.51}
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

    public InstantCommand customRedFar() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.54);
        });
    }

    public void tiltPosition() {
        G.turretState = G.TurretState.SET_POSITION;
        setPositionOnce(1);
    }

    public InstantCommand customblueFar() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.175);
        });
    }

    public InstantCommand red12Pos() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.66);
        });
    }

    public InstantCommand red18Pos1() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.985);
        });
    }

    public InstantCommand red18Pos2() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.74); //0.58
        });
    }


    public InstantCommand customBlueOriginal() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.213);
        });
    }

    public InstantCommand customBlueClose() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.213);
        });
    }

    public InstantCommand customBlueCloser() {
        return new InstantCommand(() -> {
            G.turretState = G.TurretState.SET_POSITION;
            setPositionOnce(0.12);
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

        double servoPosition;
        if (side == G.Side.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

//        //cursed asf:
//        if (side == G.Side.BLUE && G.match == G.Match.AUTO && follower.getPose().getY() > 50) {
//            servoPosition-=0.008; //0.018
//        }
//
//        if (side == G.Side.RED && G.match == G.Match.AUTO && follower.getPose().getY() < 50) {
//            servoPosition+=0.01; //0.018
//        }

        if (side == G.Side.RED && follower.getPose().getY() < 50) {
            double speed = follower.getVelocity().getMagnitude();
            if (G.TURRET_RED_FAR_MODEL_ENABLED && speed <= G.TURRET_RED_FAR_MODEL_MAX_SPEED) {
                servoPosition = applyRedFarRegressionCorrection(servoPosition);
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

    private double applyRedFarRegressionCorrection(double baseServo) {
        // Fit a compact equation to far-sample residuals (target - base model).
        // Basis: [1, xN, yN, sin(h), cos(h), xN*yN]
        final int m = 6;
        double[][] ata = new double[m][m];
        double[] atb = new double[m];

        for (double[] p : RED_FAR_MODEL_POINTS) {
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

        deltaNow *= G.TURRET_RED_FAR_MODEL_BLEND;
        deltaNow = clamp(deltaNow, -G.TURRET_RED_FAR_MODEL_MAX_CORRECTION, G.TURRET_RED_FAR_MODEL_MAX_CORRECTION);
        return clamp(baseServo + deltaNow, 0.0, 1.0);
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
