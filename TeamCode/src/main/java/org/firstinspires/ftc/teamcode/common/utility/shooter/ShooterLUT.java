package org.firstinspires.ftc.teamcode.common.utility.shooter;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParamLUT2D;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.PeacockCoordinates;
import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.peacock.math.MathFunctions;

public class ShooterLUT {
    private final ShooterParamLUT2D redShooterLUT = new ShooterParamLUT2D();
    private final ShooterParamLUT2D blueShooterLUT = new ShooterParamLUT2D();

    public ShooterLUT() {
        // Close
        addMirroredPoint(113.20010688361221, 116.22548020730808, 1240, 0.66);
        addMirroredPoint(109.89180917814961, 103.30115611158958, 1430, 0.74);
        addMirroredPoint(95.79892616572343, 130.65181702140748, 1400, 0.73);
        addMirroredPoint(99.22594926488681, 111.25804510642226, 1430, 0.745);
        addMirroredPoint(98.31302, 94.83, 1500, 0.76);
        addMirroredPoint(97.01580378014273, 82.47829647514764, 1550, 0.805);
        addMirroredPoint(84.51690914124016, 129.946029542938, 1450, 0.78);
        addMirroredPoint(86.34747362512303, 112.7660748339075, 1450, 0.78);
        addMirroredPoint(86.34747362512303, 98.933, 1500, 0.80);
        addMirroredPoint(83.6079755167323, 83.96158533772146, 1600, 0.81);
        addMirroredPoint(81.44640824926182, 71.24404546782726, 1650, 0.83);
        addMirroredPoint(75.05744994156004, 65.32487523837352, 1730, 0.83);
        addMirroredPoint(61.112344096026085, 75.14949287955217, 1750, 0.85);
        addMirroredPoint(60.07, 79.255, 1750, 0.85);
        addMirroredPoint(63.27960157018947, 94.55749031126969, 1710, 0.845);
        addMirroredPoint(66.1120, 107.0845, 1700, 0.86);
        addMirroredPoint(67.08948330616388, 119.9785079355315, 1650, 0.85);
        addMirroredPoint(66.13578736312746, 131.61878690944883, 1600, 0.845);
        addMirroredPoint(50.058060293122544, 95.41877806656005, 1800, 0.87);
        addMirroredPoint(51.866085022453255, 105.88112658403051, 1830, 0.86);
        addMirroredPoint(55.406989150159944, 116.68250376783956, 1750, 0.86);
        addMirroredPoint(56.110147791584644, 132.2154416061762, 1750, 0.86);
        addMirroredPoint(31.819085549181843, 115.37065160556102, 1850, 0.86);
        addMirroredPoint(34.047594445896905, 128.9848844272884, 1850, 0.86);
        addMirroredPoint(27, 123, 1850, 0.86);
        addMirroredPoint(78.27325775867372, 51.106029120017226, 1800, 0.865);

        // Far
        double x = 0.05;
        addMirroredPoint(98.72, 11.519, 2000, 0.87-x);
        addMirroredPoint(89.589, 10, 2050, 0.86-x);
        addMirroredPoint(87.04158272330217, 16.882499634750246, 2050, 0.875-x);
        addMirroredPoint(75.729, 17.5947, 2050, 0.885-x);
        addMirroredPoint(72.03863669568159, 11.376966341273992, 2100, 0.895-x);
        addMirroredPoint(65.43902059239666, 21.24958428810901, 2130, 0.88-x);
        addMirroredPoint(71.56, 28.29, 2000, 0.87-x);
        addMirroredPoint(56.795322688545774, 16.238613579216903, 2150, 0.87-x);
        addMirroredPoint(52.20891516978347, 11.979338878721704, 2200, 0.88-x);
    }

    private void addMirroredPoint(double x, double y, double shooterVel, double hoodPos) {
        Pose redPose = new Pose(x, y);
        Pose bluePose = redPose.differentMirror();

        redShooterLUT.addPoint(redPose, new ShooterParams(hoodPos, shooterVel));
        blueShooterLUT.addPoint(bluePose, new ShooterParams(hoodPos, shooterVel));
    }

    public ShooterParams getShooterValue(Pose samplePose, G.Side side) {
        if (side == G.Side.RED) {
            return redShooterLUT.get(samplePose);
        }
        return blueShooterLUT.get(samplePose);
    }
}
