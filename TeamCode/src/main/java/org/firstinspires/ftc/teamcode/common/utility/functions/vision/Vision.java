package org.firstinspires.ftc.teamcode.common.utility.functions.vision;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

import java.util.List;

public final class Vision {

    private static Limelight3A limelight;

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

        int wantedId = (Globals.side == Globals.Side.BLUE) ? 20 : 24;

        for (LLResultTypes.FiducialResult f : fiducials) {
            if (f.getFiducialId() == wantedId) {
                return true;
            }
        }
        return false;
    }

    /** TX in degrees of the correct fiducial (0 if not found) */
    public static double getTx() {
        LLResult result = getLatestResult();
        if (result == null) return 0.0;

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) return 0.0;

        int wantedId = (Globals.side == Globals.Side.BLUE) ? 20 : 24;

        for (LLResultTypes.FiducialResult f : fiducials) {
            if (f.getFiducialId() == wantedId) {
                return f.getTargetXDegrees(); // <-- correct method
            }
        }

        return 0.0;
    }

    public static double txToServoPos(double txDeg) {
        return -0.0035 * txDeg + 0.0052;
    }
}
