package org.firstinspires.ftc.teamcode.opmode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.camera.BallZoneCamera;
import org.firstinspires.ftc.teamcode.common.utility.camera.CameraConfig;

@TeleOp(name = "Camera Zone Tuner", group = "Tuning")
@Config
public class CameraZoneTuner extends OpMode {
    private BallZoneCamera camera;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        WebcamName webcam = hardwareMap.get(WebcamName.class, CameraConfig.WEBCAM_NAME);
        camera = new BallZoneCamera();
        camera.start(hardwareMap, webcam);
    }

    @Override
    public void loop() {
        camera.update();

        telemetry.addData("camera", CameraConfig.WEBCAM_NAME);
        telemetry.addData("target_color_mode", CameraConfig.TARGET_COLOR_MODE);
        telemetry.addData("filtered_blobs", camera.getFilteredBlobCount());
        telemetry.addData("live_blend", "%.3f", camera.getStableTargetNormalized());
        telemetry.addData("drive_blend_side", "%.3f", camera.getDriveTargetNormalizedForSide(G.side));
        telemetry.addData("confidence", camera.hasConfidence());
        telemetry.addData("recommended_y", camera.getRecommendedY());

        telemetry.update();
    }

    @Override
    public void stop() {
        if (camera != null) camera.stop();
    }
}
