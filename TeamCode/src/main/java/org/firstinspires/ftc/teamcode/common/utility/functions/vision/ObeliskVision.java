package org.firstinspires.ftc.teamcode.common.utility.functions.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.common.utility.G;

import java.util.List;

public final class ObeliskVision {

    private ObeliskVision() {}

    public static G.ObeliskOptions getObeliskFiducial(LLResult result) {

        if (result == null) {
            return G.ObeliskOptions.NOT_FOUND;
        }

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) {
            return G.ObeliskOptions.NOT_FOUND;
        }

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                    return G.ObeliskOptions.GPP;
                case 22:
                    return G.ObeliskOptions.PGP;
                case 23:
                    return G.ObeliskOptions.PPG;
            }
        }

        return G.ObeliskOptions.NOT_FOUND;
    }
}
