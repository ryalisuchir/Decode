package org.firstinspires.ftc.teamcode.common.utility.camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.common.utility.peacock.geometry.Pose;

@Config
public class CameraConfig {
    public static String WEBCAM_NAME = "Webcam 1";

    public static int FRAME_WIDTH = 320;
    public static int FRAME_HEIGHT = 240;

    public static double ROI_LEFT = -1;
    public static double ROI_TOP = 0.75;
    public static double ROI_RIGHT = 1.00;
    public static double ROI_BOTTOM = -0.4;

    public static int ZONE_COUNT = 3;

    public static double MIN_CONTOUR_AREA = 140;
    public static double MAX_CONTOUR_AREA = 30000;
    public static double MIN_DENSITY = 0.25;
    public static int MIN_TOTAL_BLOBS_FOR_CONFIDENCE = 3;
    public static double MIN_DOMINANCE_BLOB_RATIO_FOR_CONFIDENCE = 0.34;
    public static int POPULATION_BIN_COUNT = 15;
    public static int POPULATION_BIN_RADIUS = 1;

    public static int STABILITY_FRAMES = 7;
    public static int TARGET_COLOR_MODE = 0; // 0=both purple+green, 1=purple only, 2=green only

    public static double Y_LEFT2 = 45;
    public static Pose Y_LEFT2_POSE = new Pose(135, 45, Math.toRadians(0));
    public static Pose Y_LEFT2_CONTROL_POSE = new Pose(116.41539193083571, 35.3342939481268, Math.toRadians(0));

    public static double Y_CENTER = 28;
    public static Pose Y_CENTER_POSE = new Pose(135, 28, Math.toRadians(0));
    public static Pose Y_CENTER_CONTROL_POSE = new Pose(116.41539193083571, 24.752161383285305, Math.toRadians(0));

    public static double Y_RIGHT2 = 11;
    public static Pose Y_RIGHT2_POSE = new Pose(135, 11, Math.toRadians(0));
    public static Pose Y_RIGHT2_CONTROL_POSE = new Pose(116.41539193083571, 9.812680115273784, Math.toRadians(0));
}
