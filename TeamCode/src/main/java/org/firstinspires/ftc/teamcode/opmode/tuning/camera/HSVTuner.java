//package org.firstinspires.ftc.teamcode.opmode.tuning.camera;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.teamcode.common.utility.camera.BallZoneCamera;
//import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;
//
//@TeleOp
//@Config
//public class HSVTuner extends OpMode {
//
//    public static double PURPLE_H_MIN = CameraConfig.PURPLE_H_MIN;
//    public static double PURPLE_H_MAX = CameraConfig.PURPLE_H_MAX;
//    public static double PURPLE_S_MIN = CameraConfig.PURPLE_S_MIN;
//    public static double PURPLE_S_MAX = CameraConfig.PURPLE_S_MAX;
//    public static double PURPLE_V_MIN = CameraConfig.PURPLE_V_MIN;
//    public static double PURPLE_V_MAX = CameraConfig.PURPLE_V_MAX;
//
//    public static double GREEN_H_MIN = 82;
//    public static double GREEN_H_MAX = 105;
//    public static double GREEN_S_MIN = 100;
//    public static double GREEN_S_MAX = 255;
//    public static double GREEN_V_MIN = 40;
//    public static double GREEN_V_MAX = 220;
//
//    public static double MIN_CONTOUR_AREA       = CameraConfig.MIN_CONTOUR_AREA;
//    public static double MAX_CONTOUR_AREA       = CameraConfig.MAX_CONTOUR_AREA;
//    public static double MIN_DENSITY            = CameraConfig.MIN_DENSITY;
//    public static double MAX_SINGLE_BALL_BBOX_PX = CameraConfig.MAX_SINGLE_BALL_BBOX_PX;
//    public static int    LOCATOR_BLUR           = CameraConfig.LOCATOR_BLUR;
//    public static int    LOCATOR_ERODE          = CameraConfig.LOCATOR_ERODE;
//    public static int    LOCATOR_DILATE         = CameraConfig.LOCATOR_DILATE;
//
//    /** 0 = both, 1 = purple only, 2 = green only */
//    public static int TARGET_COLOR_MODE = 0;
//
//    private BallZoneCamera camera;
//    private double lastHash = 0;
//
//    @Override
//    public void init() {
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
//        pushToConfig();
//        startCamera();
//    }
//
//    @Override
//    public void loop() {
//        // Detect any slider change and restart camera with new values
//        double hash = computeHash();
//        if (hash != lastHash) {
//            lastHash = hash;
//            pushToConfig();
//            restartCamera();
//        }
//
//        camera.update();
//
//        int purple = 0, green = 0;
//        for (BallZoneCamera.DetectedBlob b : camera.getDetectedBlobs()) {
//            if (b.color == BallZoneCamera.BallColor.PURPLE) purple++;
//            else green++;
//            telemetry.addData(b.color == BallZoneCamera.BallColor.PURPLE ? "P" : "G",
//                    String.format("dist=%.0fcm  bbox=%.0fx%.0f  density=%.2f",
//                            b.distanceCm, b.width, b.height,
//                            b.area / Math.max(1, b.width * b.height)));
//        }
//
//        telemetry.addData("Detected:", String.format("purple=%d  green=%d", purple, green));
//        telemetry.addData("PURPLE_H", String.format("%.0f  –  %.0f", PURPLE_H_MIN, PURPLE_H_MAX));
//        telemetry.addData("PURPLE_S", String.format("%.0f  –  %.0f", PURPLE_S_MIN, PURPLE_S_MAX));
//        telemetry.addData("PURPLE_V", String.format("%.0f  –  %.0f", PURPLE_V_MIN, PURPLE_V_MAX));
//        telemetry.addData("GREEN_H",  String.format("%.0f  –  %.0f", GREEN_H_MIN,  GREEN_H_MAX));
//        telemetry.addData("GREEN_S",  String.format("%.0f  –  %.0f", GREEN_S_MIN,  GREEN_S_MAX));
//        telemetry.addData("GREEN_V",  String.format("%.0f  –  %.0f", GREEN_V_MIN,  GREEN_V_MAX));
//        telemetry.addData("CONTOUR_AREA", String.format("%.0f – %.0f", MIN_CONTOUR_AREA, MAX_CONTOUR_AREA));
//        telemetry.addData("DENSITY",  String.format("%.2f", MIN_DENSITY));
//        telemetry.addData("BLUR/ERODE/DILATE", String.format("%d / %d / %d", LOCATOR_BLUR, LOCATOR_ERODE, LOCATOR_DILATE));
//        telemetry.update();
//    }
//
//    @Override
//    public void stop() {
//        if (camera != null) camera.stop();
//    }
//
//    private void pushToConfig() {
//        CameraConfig.PURPLE_H_MIN          = PURPLE_H_MIN;
//        CameraConfig.PURPLE_H_MAX          = PURPLE_H_MAX;
//        CameraConfig.PURPLE_S_MIN          = PURPLE_S_MIN;
//        CameraConfig.PURPLE_S_MAX          = PURPLE_S_MAX;
//        CameraConfig.PURPLE_V_MIN          = PURPLE_V_MIN;
//        CameraConfig.PURPLE_V_MAX          = PURPLE_V_MAX;
//        CameraConfig.GREEN_H_MIN           = GREEN_H_MIN;
//        CameraConfig.GREEN_H_MAX           = GREEN_H_MAX;
//        CameraConfig.GREEN_S_MIN           = GREEN_S_MIN;
//        CameraConfig.GREEN_S_MAX           = GREEN_S_MAX;
//        CameraConfig.GREEN_V_MIN           = GREEN_V_MIN;
//        CameraConfig.GREEN_V_MAX           = GREEN_V_MAX;
//        CameraConfig.MIN_CONTOUR_AREA      = MIN_CONTOUR_AREA;
//        CameraConfig.MAX_CONTOUR_AREA      = MAX_CONTOUR_AREA;
//        CameraConfig.MIN_DENSITY           = MIN_DENSITY;
//        CameraConfig.MAX_SINGLE_BALL_BBOX_PX = MAX_SINGLE_BALL_BBOX_PX;
//        CameraConfig.LOCATOR_BLUR          = LOCATOR_BLUR;
//        CameraConfig.LOCATOR_ERODE         = LOCATOR_ERODE;
//        CameraConfig.LOCATOR_DILATE        = LOCATOR_DILATE;
//        CameraConfig.TARGET_COLOR_MODE     = TARGET_COLOR_MODE;
//    }
//
//    private void startCamera() {
//        WebcamName webcam = hardwareMap.get(WebcamName.class, CameraConfig.WEBCAM_NAME);
//        camera = new BallZoneCamera();
//        camera.start(hardwareMap, webcam);
//    }
//
//    private void restartCamera() {
//        if (camera != null) camera.stop();
//        // Small delay so the stop completes before restarting
//        try { Thread.sleep(80); } catch (InterruptedException ignored) {}
//        startCamera();
//    }
//
//    private double computeHash() {
//        return PURPLE_H_MIN * 1.1 + PURPLE_H_MAX * 1.2
//                + PURPLE_S_MIN * 1.3 + PURPLE_S_MAX * 1.4
//                + PURPLE_V_MIN * 1.5 + PURPLE_V_MAX * 1.6
//                + GREEN_H_MIN  * 2.1 + GREEN_H_MAX  * 2.2
//                + GREEN_S_MIN  * 2.3 + GREEN_S_MAX  * 2.4
//                + GREEN_V_MIN  * 2.5 + GREEN_V_MAX  * 2.6
//                + MIN_CONTOUR_AREA * 3.1 + MAX_CONTOUR_AREA * 3.2
//                + MIN_DENSITY * 3.3 + MAX_SINGLE_BALL_BBOX_PX * 3.4
//                + LOCATOR_BLUR * 4.1 + LOCATOR_ERODE * 4.2 + LOCATOR_DILATE * 4.3
//                + TARGET_COLOR_MODE * 5.1;
//    }
//}