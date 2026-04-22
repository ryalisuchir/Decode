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
        addMirroredPoint(87.86, 87.65, 1300, 0.32); 
        addMirroredPoint(87.13, 88.16, 1300, 0.32); 
        addMirroredPoint(85.93, 88.46, 1300, 0.32); 
        addMirroredPoint(84.46, 90.18, 1300, 0.32); 
        addMirroredPoint(85.70, 93.07, 1300, 0.32); 
        addMirroredPoint(81.69, 90.03, 1300, 0.32); 
        addMirroredPoint(83.15, 91.25, 1300, 0.32); 
        addMirroredPoint(86.95, 90.26, 1300, 0.32); 
        addMirroredPoint(85.92, 90.19, 1300, 0.32); 
        addMirroredPoint(90.23, 91.25, 1300, 0.32); 
        addMirroredPoint(85.95, 86.96, 1300, 0.32); 
        addMirroredPoint(86.55, 87.26, 1300, 0.32); 
        addMirroredPoint(87.79, 87.46, 1300, 0.32); 
        addMirroredPoint(86.67, 87.85, 1300, 0.32); 
        addMirroredPoint(85.63, 89.13, 1300, 0.32); 
        addMirroredPoint(85.66, 88.70, 1300, 0.32); 
        addMirroredPoint(86.46, 88.51, 1300, 0.32); 
        addMirroredPoint(88.22, 85.92, 1300, 0.32); 
        addMirroredPoint(86.01, 83.81, 1300, 0.32); 
        addMirroredPoint(85.073, 85.94, 1300, 0.32); 
        addMirroredPoint(88.45, 85.18, 1300, 0.32);
        addMirroredPoint(71.02, 69.82, 1500, 0.45); 
        addMirroredPoint(70.24, 75.47, 1500, 0.45); 
        addMirroredPoint(66.80, 70.52, 1500, 0.45); 
        addMirroredPoint(67.83, 73.55, 1500, 0.45); 
        addMirroredPoint(67.85, 71.46, 1500, 0.45); 
        addMirroredPoint(67.26, 71.22, 1500, 0.45); 
        addMirroredPoint(67.21, 72.24, 1500, 0.45); 
        addMirroredPoint(66.64, 73.75, 1500, 0.45); 
        addMirroredPoint(68.20, 74.09, 1500, 0.45); 
        addMirroredPoint(68.51, 72.66, 1500, 0.45); 
        addMirroredPoint(68.11, 72.23, 1500, 0.45); 
        addMirroredPoint(67.94, 72.16, 1500, 0.45); 
        addMirroredPoint(67.98, 72.14, 1500, 0.45); 
        addMirroredPoint(69.94, 72.47, 1500, 0.45); 
        addMirroredPoint(68.65, 71.08, 1500, 0.45); 
        addMirroredPoint(70.40, 70.32, 1500, 0.45); 
        addMirroredPoint(71.24, 69.42, 1500, 0.45); 
        addMirroredPoint(71.32, 68.48, 1500, 0.45); 
        addMirroredPoint(71.69, 67.95, 1500, 0.45); 
        addMirroredPoint(71.40, 66.47, 1500, 0.45); 
        addMirroredPoint(71.27, 66.58, 1500, 0.45);
        addMirroredPoint(48.63, 88.04, 1550, 0.45); 
        addMirroredPoint(48.13, 88.49, 1550, 0.45); 
        addMirroredPoint(43.71, 87.69, 1550, 0.45); 
        addMirroredPoint(49.76, 89.29, 1550, 0.45); 
        addMirroredPoint(38.05, 89.41, 1550, 0.45); 
        addMirroredPoint(39.28, 88.76, 1550, 0.45); 
        addMirroredPoint(40.18, 89.12, 1550, 0.45); 
        addMirroredPoint(42.33, 87.81, 1550, 0.45); 
        addMirroredPoint(42.60, 87.02, 1550, 0.45); 
        addMirroredPoint(43.40, 85.16, 1550, 0.45); 
        addMirroredPoint(45.23, 86.24, 1550, 0.45); 
        addMirroredPoint(45.94, 84.33, 1550, 0.45); 
        addMirroredPoint(45.47, 83.92, 1550, 0.45); 
        addMirroredPoint(46.23, 84.24, 1550, 0.45); 
        addMirroredPoint(46.94, 84.46, 1550, 0.45); 
        addMirroredPoint(47.74, 83.54, 1550, 0.45); 
        addMirroredPoint(47.89, 82.89, 1550, 0.45); 
        addMirroredPoint(47.82, 81.65, 1550, 0.45); 
        addMirroredPoint(43.83, 90.02, 1550, 0.45); 
        addMirroredPoint(47.13, 84.32, 1550, 0.45); 
        addMirroredPoint(42.45, 89.01, 1550, 0.45);
        addMirroredPoint(28.34, 106.03, 1600, 0.55); 
        addMirroredPoint(24.98, 105.15, 1600, 0.55); 
        addMirroredPoint(25.15, 105.58, 1600, 0.55); 
        addMirroredPoint(25.92, 106.06, 1600, 0.55); 
        addMirroredPoint(24.81, 106.105, 1600, 0.55); 
        addMirroredPoint(25.29, 104.86, 1600, 0.55); 
        addMirroredPoint(25.57, 105.12, 1600, 0.55); 
        addMirroredPoint(26.39, 103.37, 1600, 0.55); 
        addMirroredPoint(27.65, 103.41, 1600, 0.55); 
        addMirroredPoint(28.78, 102.18, 1600, 0.55); 
        addMirroredPoint(30.03, 101.15, 1600, 0.55); 
        addMirroredPoint(31.78, 101.42, 1600, 0.55); 
        addMirroredPoint(33.45, 100.65, 1600, 0.55); 
        addMirroredPoint(33.34, 99.48, 1600, 0.55); 
        addMirroredPoint(34.36, 99.48, 1600, 0.55); 
        addMirroredPoint(34.40, 99.58, 1600, 0.55); 
        addMirroredPoint(34.03, 99.46, 1600, 0.55); 
        addMirroredPoint(34.20, 98.95, 1600, 0.55); 
        addMirroredPoint(32.66, 99.78, 1600, 0.55); 
        addMirroredPoint(32.11, 98.90, 1600, 0.55); 
        addMirroredPoint(31.87, 100.27, 1600, 0.55);
        addMirroredPoint(70.62, 115.97, 1300, 0.35); 
        addMirroredPoint(69.95, 115.03, 1300, 0.35); 
        addMirroredPoint(70.06, 114.28, 1300, 0.35); 
        addMirroredPoint(70.93, 115.50, 1300, 0.35); 
        addMirroredPoint(71.19, 115.97, 1300, 0.35); 
        addMirroredPoint(72.31, 116.95, 1300, 0.35); 
        addMirroredPoint(72.74, 116.78, 1300, 0.35); 
        addMirroredPoint(71.55, 118.35, 1300, 0.35); 
        addMirroredPoint(70.73, 116.74, 1300, 0.35); 
        addMirroredPoint(71.54, 116.31, 1300, 0.35); 
        addMirroredPoint(72.40, 115.33, 1300, 0.35); 
        addMirroredPoint(72.87, 117.25, 1300, 0.35); 
        addMirroredPoint(72.85, 116.89, 1300, 0.35); 
        addMirroredPoint(74.02, 116.40, 1300, 0.35); 
        addMirroredPoint(74.10, 116.78, 1300, 0.35); 
        addMirroredPoint(73.89, 116.97, 1300, 0.35); 
        addMirroredPoint(74.40, 116.47, 1300, 0.35); 
        addMirroredPoint(74.56, 116.60, 1300, 0.35); 
        addMirroredPoint(73.57, 116.22, 1300, 0.35); 
        addMirroredPoint(74.10, 116.10, 1300, 0.35); 
        addMirroredPoint(74.17, 115.78, 1300, 0.35); 

        //Far:
        addMirroredPoint(97.83, 7.32, 1700, 0.7);
        addMirroredPoint(97.98, 8.08, 1700, 0.7);
        addMirroredPoint(95.90, 8.27, 1700, 0.7);
        addMirroredPoint(94.15, 8.32, 1700, 0.7);
        addMirroredPoint(92.19, 7.72, 1700, 0.7);
        addMirroredPoint(89.66, 8.33, 1700, 0.7);
        addMirroredPoint(87.26, 9.06, 1700, 0.7);
        addMirroredPoint(85.89, 10.10, 1700, 0.7);
        addMirroredPoint(84.95, 11.23, 1700, 0.6);
        addMirroredPoint(85.00, 12.08, 1700, 0.6);
        addMirroredPoint(86.35, 12.09, 1700, 0.6);
        addMirroredPoint(86.52, 12.82, 1700, 0.6);
        addMirroredPoint(87.84, 14.11, 1700, 0.6);
        addMirroredPoint(87.35, 14.08, 1700, 0.6);
        addMirroredPoint(87.71, 13.01, 1675, 0.6);
        addMirroredPoint(88.07, 12.25, 1725, 0.6);
        addMirroredPoint(88.45, 12.29, 1750, 0.6);
        addMirroredPoint(89.65, 10.89, 1700, 0.6);
        addMirroredPoint(90.54, 9.61, 1700, 0.6);
        addMirroredPoint(91.00, 9.84, 1700, 0.6);
        addMirroredPoint(91.60, 9.00, 1700, 0.6);
        addMirroredPoint(74.42, 23.69, 1675, 0.6);
        addMirroredPoint(74.51, 21.82, 1675, 0.6);
        addMirroredPoint(74.23, 21.78, 1675, 0.6);
        addMirroredPoint(73.57, 20.76, 1675, 0.6);
        addMirroredPoint(72.15, 19.15, 1675, 0.6);
        addMirroredPoint(71.05, 18.01, 1675, 0.6);
        addMirroredPoint(70.99, 17.88, 1700, 0.6);
        addMirroredPoint(71.10, 18.89, 1700, 0.6);
        addMirroredPoint(71.74, 18.84, 1700, 0.6);
        addMirroredPoint(71.90, 18.97, 1725, 0.6);
        addMirroredPoint(73.11, 20.47, 1725, 0.6);
        addMirroredPoint(73.38, 20.86, 1725, 0.6);
        addMirroredPoint(73.92, 21.82, 1725, 0.6);
        addMirroredPoint(74.41, 22.37, 1750, 0.6);
        addMirroredPoint(74.91, 22.29, 1750, 0.6);
        addMirroredPoint(72.41, 19.65, 1725, 0.6);
        addMirroredPoint(72.63, 19.02, 1725, 0.6);
        addMirroredPoint(73.88, 18.68, 1700, 0.6);
        addMirroredPoint(73.72, 18.21, 1700, 0.6);
        addMirroredPoint(73.95, 17.43, 1700, 0.6);
        addMirroredPoint(72.71, 16.90, 1675, 0.6);
        addMirroredPoint(49.60, 7.05, 1825, 0.7);
        addMirroredPoint(50.12, 7.77, 1825, 0.7);
        addMirroredPoint(50.22, 7.24, 1825, 0.7);
        addMirroredPoint(49.29, 7.22, 1825, 0.65);
        addMirroredPoint(48.78, 3.79, 1850, 0.7);
        addMirroredPoint(46.33, 4.37, 1875, 0.7);
        addMirroredPoint(46.22, 6.27, 1875, 0.7);
        addMirroredPoint(46.42, 7.46, 1875, 0.67);
        addMirroredPoint(46.83, 7.67, 1875, 0.67);
        addMirroredPoint(45.42, 9.91, 1875, 0.67);
        addMirroredPoint(44.10, 11.64, 1900, 0.67);
        addMirroredPoint(45.29, 11.34, 1900, 0.7);
        addMirroredPoint(46.00, 9.53, 1900, 0.7);
        addMirroredPoint(46.71, 9.38, 1950, 0.72);
        addMirroredPoint(45.96, 9.55, 1950, 0.72);
        addMirroredPoint(46.72, 9.89, 1950, 0.77);
        addMirroredPoint(47.11, 10.80, 1975, 0.77);
        addMirroredPoint(48.73, 10.91, 1975, 0.77);
        addMirroredPoint(49.56, 11.58, 1975, 0.77);
        addMirroredPoint(48.91, 12.33, 1950, 0.72);
        addMirroredPoint(48.51, 13.03, 1925, 0.7);
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
