package org.firstinspires.ftc.teamcode.common.utility;

import com.pedropathing.math.Vector;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.Globals;

import java.util.List;

public final class Vision {

    private static Limelight3A limelight;
    private static final int distance = 2, regular = 2;
    private static final int pipeline = regular;

    private Vision() {}

    /** Call ONCE during robot init */
    public static void init(Limelight3A ll) {
        limelight = ll;
    }

    /** Latest Limelight result (null-safe) */
    public static LLResult getLatestResult() {
        if (limelight == null) return null;
        return limelight.getLatestResult();
    }

    /** True if Limelight sees the correct fiducial for our alliance */
    public static boolean hasCorrectFiducial() {
        LLResult result = getLatestResult();
        if (result == null) return false;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return false;

        int wantedId = (Globals.alliance == Globals.Alliance.BLUE) ? 20 : 24;

        for (LLResultTypes.FiducialResult f : fiducials) {
            if (f.getFiducialId() == wantedId) {
                return true;
            }
        }
        return false;
    }

    public static double distanceFromTag() {
        List<LLResultTypes.FiducialResult> r = limelight.getLatestResult().getFiducialResults();
        limelight.getLatestResult().getBotpose_MT2();

        if (r.isEmpty()) return 0;

        int tagID = (Globals.alliance == Globals.Alliance.BLUE) ? 20 : 24;

        LLResultTypes.FiducialResult target = null;
        for (LLResultTypes.FiducialResult i: r) {
            if (i != null && i.getFiducialId() ==  tagID) {
                target = i;
                break;
            }
        }

        if (target != null) {
            double x = (target.getCameraPoseTargetSpace().getPosition().x / DistanceUnit.mPerInch) + 8; // right/left from tag
            double z = (target.getCameraPoseTargetSpace().getPosition().z / DistanceUnit.mPerInch) + 8; // forward/back from tag

            Vector e = new Vector();
            e.setOrthogonalComponents(x, z);
            return 0.04321*(Math.pow(e.getMagnitude(), 1.48));
        }

        return 0;
    }

    public static double getTx() {
        LLResult result = getLatestResult();
        if (result == null) return 0.0;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return 0.0;

        int wantedId = (Globals.alliance == Globals.Alliance.BLUE) ? 20 : 24;

        for (LLResultTypes.FiducialResult f : fiducials) {
            if (f.getFiducialId() == wantedId) {
                return f.getTargetXDegrees();
            }
        }

        return 0.0;
    }

    public static double txToServoPos(double txDeg) {
        return -0.0033 * txDeg + 0.0053;
    }

    public static void switchToRegular() {
        if (pipeline != regular)
            limelight.pipelineSwitch(regular);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public static void switchToDistance() {
        if (pipeline != distance)
            limelight.pipelineSwitch(distance);
        limelight.setPollRateHz(100);
        limelight.start();
    }
}
