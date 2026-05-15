package org.firstinspires.ftc.teamcode.common.utility;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.tables.ShooterParamLUT2D;

public class ShooterLUT {
    private final ShooterParamLUT2D redShooterLUT = new ShooterParamLUT2D();
    private final ShooterParamLUT2D blueShooterLUT = new ShooterParamLUT2D();

    public ShooterLUT() {
        //Close:
        double x = 0;
        addMirroredPoint(108, 114, 1100, 0);
        addMirroredPoint(101, 134, 1100, 0);
        addMirroredPoint(101, 134, 1100, 0);
        addMirroredPoint(104, 110, 1100, 0.1);
        addMirroredPoint(96, 118, 1100, 0.05);
        addMirroredPoint(96, 118, 1100, 0.05);
        addMirroredPoint(90, 134, 1100, 0.05);
        addMirroredPoint(91, 103, 1200, 0.2+x);
        addMirroredPoint(88, 113, 1200, 0.26+x);
        addMirroredPoint(86, 131, 1100, 0.18+x);
        addMirroredPoint(76, 86, 1250, 0.29+x);
        addMirroredPoint(71, 98, 1200, 0.29+x);
        addMirroredPoint(77, 120, 1200, 0.25+x);
        addMirroredPoint(78, 138, 1200, 0.25+x);
        addMirroredPoint(64, 74, 1350, 0.33+x);
        addMirroredPoint(69, 96, 1300, 0.38+x);
        addMirroredPoint(75, 114, 1250, 0.3+x);
        addMirroredPoint(67, 121, 1250, 0.3+x);
        addMirroredPoint(58, 84, 1360, 0.44+x);
        addMirroredPoint(57, 100, 1380, 0.44+x);
        addMirroredPoint(58, 120, 1320, 0.4+x);
        addMirroredPoint(58, 120, 1320, 0.4+x);
        addMirroredPoint(54, 130, 1280, 0.44+x);
        addMirroredPoint(46.78, 96, 1400, 0.42+x);
        addMirroredPoint(37, 113, 1450, 0.45+x);
        addMirroredPoint(36, 124, 1460, 0.52+x);
        addMirroredPoint(18, 118, 1570, 0.61+x);


        //Far:
        addMirroredPoint(90, 10, 1760, 0.68);

        addMirroredPoint(87, 7.14, 1760, 0.68);
        addMirroredPoint(98, 7.52, 1730, 0.66);
        addMirroredPoint(85, 17, 1690, 0.70);
        addMirroredPoint(76.59, 6.93, 1720, 0.70);
        addMirroredPoint(74.37, 13.56, 1760, 0.70);
        addMirroredPoint(66.38, 6.16, 1800, 0.75);
        addMirroredPoint(63.47, 15.77, 1800, 0.76);
        addMirroredPoint(57, 7, 1800, 0.75);
        addMirroredPoint(57, 18, 1800, 0.77);
        addMirroredPoint(45, 8, 1890, 0.73);
    }

    private void addMirroredPoint(double x, double y, double shooterVel, double hoodPos) {
        Pose redPose = new Pose(x, y);
        Pose bluePose = redPose.mirror(141);

        redShooterLUT.addPoint(redPose, new ShooterParams(hoodPos, shooterVel));
        blueShooterLUT.addPoint(bluePose, new ShooterParams(hoodPos, shooterVel));
    }

    public ShooterParams getShooterValue(Pose samplePose, Globals.Alliance side) {
        if (side == Globals.Alliance.RED) {
            return redShooterLUT.get(samplePose);
        }
        return blueShooterLUT.get(samplePose);
    }
}
