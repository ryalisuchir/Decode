package org.firstinspires.ftc.teamcode.common.utility.functions;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

import java.util.List;

public final class ObeliskVision {

    private ObeliskVision() {}

    public static Globals.ObeliskOptions getObeliskFiducial(LLResult result) {

        if (result == null) {
            return Globals.ObeliskOptions.NOT_FOUND;
        }

        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        if (fiducials == null || fiducials.isEmpty()) {
            return Globals.ObeliskOptions.NOT_FOUND;
        }

        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            switch (fiducial.getFiducialId()) {
                case 21:
                    return Globals.ObeliskOptions.GPP;
                case 22:
                    return Globals.ObeliskOptions.PGP;
                case 23:
                    return Globals.ObeliskOptions.PPG;
            }
        }

        return Globals.ObeliskOptions.NOT_FOUND;
    }
}
