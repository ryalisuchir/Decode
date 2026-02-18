package org.firstinspires.ftc.teamcode.common.utility.camera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BallZoneCamera {
    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor purpleLocator;
    private ColorBlobLocatorProcessor greenLocator;
    private ZoneOverlayProcessor zoneOverlay;

    private double[] zoneScores = new double[0];
    private int bestZone = 0;
    private int stableZone = 0;
    private boolean confident = false;
    private int filteredBlobCount = 0;
    private double targetNormalized = 0.5;
    private double stableTargetNormalized = 0.5;

    private final ArrayDeque<Double> targetHistory = new ArrayDeque<>();

    public void start(HardwareMap hardwareMap, WebcamName webcam) {
        stop();

        int mode = CameraConfig.TARGET_COLOR_MODE;
        if (mode == 0 || mode == 1) {
            purpleLocator = buildLocator(ColorRange.ARTIFACT_PURPLE);
        }
        if (mode == 0 || mode == 2) {
            greenLocator = buildLocator(ColorRange.ARTIFACT_GREEN);
        }
        zoneOverlay = new ZoneOverlayProcessor();

        VisionPortal.Builder portalBuilder = new VisionPortal.Builder()
                .setCamera(webcam)
                .setCameraResolution(new Size(CameraConfig.FRAME_WIDTH, CameraConfig.FRAME_HEIGHT));
        if (purpleLocator != null) portalBuilder.addProcessor(purpleLocator);
        if (greenLocator != null) portalBuilder.addProcessor(greenLocator);
        portalBuilder.addProcessor(zoneOverlay);
        visionPortal = portalBuilder.build();
        FtcDashboard.getInstance().startCameraStream(visionPortal, 20);

        int zoneCount = Math.max(1, CameraConfig.ZONE_COUNT);
        zoneScores = new double[zoneCount];
        bestZone = centerZone(zoneCount);
        stableZone = bestZone;
        targetNormalized = 0.5;
        stableTargetNormalized = 0.5;
        targetHistory.clear();
    }

    public void stop() {
        if (visionPortal != null) {
            FtcDashboard.getInstance().stopCameraStream();
            visionPortal.close();
            visionPortal = null;
        }
        purpleLocator = null;
        greenLocator = null;
        zoneOverlay = null;
        targetHistory.clear();
    }

    public void update() {
        if (purpleLocator == null && greenLocator == null) return;

        int zoneCount = Math.max(1, CameraConfig.ZONE_COUNT);
        if (zoneScores.length != zoneCount) {
            zoneScores = new double[zoneCount];
            bestZone = centerZone(zoneCount);
            stableZone = bestZone;
            targetNormalized = 0.5;
            stableTargetNormalized = 0.5;
            targetHistory.clear();
        }
        Arrays.fill(zoneScores, 0.0);
        filteredBlobCount = 0;
        Accumulation accumulation = new Accumulation();

        accumulateFromLocator(purpleLocator, zoneCount, accumulation);
        accumulateFromLocator(greenLocator, zoneCount, accumulation);

        PopulationTarget populationTarget = computePopulationTarget(accumulation.samples);
        targetNormalized = populationTarget.targetNormalized;

        bestZone = normalizedToZone(targetNormalized, zoneCount);
        double dominanceRatio = accumulation.sampleCount > 0
                ? populationTarget.peakCount / accumulation.sampleCount
                : 0;
        confident = accumulation.sampleCount >= CameraConfig.MIN_TOTAL_BLOBS_FOR_CONFIDENCE
                && dominanceRatio >= CameraConfig.MIN_DOMINANCE_BLOB_RATIO_FOR_CONFIDENCE;

        double chosen = confident ? targetNormalized : 0.5;
        pushTargetHistory(chosen);
        stableTargetNormalized = average(targetHistory);
        stableZone = normalizedToZone(stableTargetNormalized, zoneCount);

        if (zoneOverlay != null) {
            zoneOverlay.setObservedBlend(stableTargetNormalized);
        }
    }

    public int getBestZone() {
        return bestZone;
    }

    public int getStableZone() {
        return stableZone;
    }

    public double getStableTargetNormalized() {
        return stableTargetNormalized;
    }

    public double getDriveTargetNormalized() {
        return stableTargetNormalized;
    }

    public double getDriveTargetNormalizedForSide(G.Side side) {
        return sideAdjustedBlend(stableTargetNormalized, side);
    }

    public boolean hasConfidence() {
        return confident;
    }

    public int getFilteredBlobCount() {
        return filteredBlobCount;
    }

    public double[] getZoneScores() {
        return Arrays.copyOf(zoneScores, zoneScores.length);
    }

    public double getRecommendedY() {
        return getRecommendedY(G.side);
    }

    public double getRecommendedY(G.Side side) {
        return interpolateThree(
                CameraConfig.Y_LEFT2,
                CameraConfig.Y_CENTER,
                CameraConfig.Y_RIGHT2,
                sideAdjustedBlend(stableTargetNormalized, side)
        );
    }

    public String getZoneLabel(int zone) {
        switch (zone) {
            case 0: return "left";
            case 1: return "center";
            case 2: return "right";
            default: return "zone_" + zone;
        }
    }

    public double zoneToTargetY(int zone) {
        switch (zone) {
            case 0: return CameraConfig.Y_LEFT2;
            case 1: return CameraConfig.Y_CENTER;
            case 2: return CameraConfig.Y_RIGHT2;
            default: return CameraConfig.Y_CENTER;
        }
    }

    private static double interpolateThree(double left, double center, double right, double blend) {
        double b = clamp01(blend);
        if (b <= 0.5) {
            return lerp(left, center, b * 2.0);
        }
        return lerp(center, right, (b - 0.5) * 2.0);
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static double sideAdjustedBlend(double blend, G.Side side) {
        if (side == G.Side.BLUE) return clamp01(1.0 - blend);
        return clamp01(blend);
    }

    private void pushTargetHistory(double value) {
        int maxFrames = Math.max(1, CameraConfig.STABILITY_FRAMES);
        targetHistory.addLast(value);
        while (targetHistory.size() > maxFrames) {
            targetHistory.removeFirst();
        }
    }

    private static double average(ArrayDeque<Double> values) {
        if (values.isEmpty()) return 0.5;
        double sum = 0.0;
        for (double v : values) sum += v;
        return sum / values.size();
    }

    private static int centerZone(int zoneCount) {
        return zoneCount / 2;
    }

    private static int xToZone(double x, int frameWidth, int zoneCount) {
        if (zoneCount <= 1) return 0;
        double clamped = Math.max(0, Math.min(frameWidth - 1, x));
        int zone = (int) Math.floor(clamped / (frameWidth / (double) zoneCount));
        return Math.max(0, Math.min(zoneCount - 1, zone));
    }

    private static int normalizedToZone(double normalizedX, int zoneCount) {
        double x = clamp01(normalizedX) * Math.max(1.0, CameraConfig.FRAME_WIDTH - 1.0);
        return xToZone(x, CameraConfig.FRAME_WIDTH, zoneCount);
    }

    private static double clamp01(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    private void accumulateFromLocator(ColorBlobLocatorProcessor locator, int zoneCount, Accumulation accumulation) {
        if (locator == null) return;
        List<ColorBlobLocatorProcessor.Blob> blobs = new ArrayList<>(locator.getBlobs());
        for (ColorBlobLocatorProcessor.Blob blob : blobs) {
            double contourArea = blob.getContourArea();
            if (contourArea < CameraConfig.MIN_CONTOUR_AREA || contourArea > CameraConfig.MAX_CONTOUR_AREA) continue;
            if (blob.getDensity() < CameraConfig.MIN_DENSITY) continue;

            RotatedRect boxFit = blob.getBoxFit();
            int zone = xToZone(boxFit.center.x, CameraConfig.FRAME_WIDTH, zoneCount);
            zoneScores[zone] += contourArea;
            double normalizedX = clamp01(boxFit.center.x / Math.max(1.0, CameraConfig.FRAME_WIDTH - 1.0));
            accumulation.samples.add(new BlobSample(normalizedX));
            accumulation.sampleCount++;
            filteredBlobCount++;
        }
    }

    private PopulationTarget computePopulationTarget(List<BlobSample> samples) {
        if (samples.isEmpty()) return new PopulationTarget(0.5, 0.0);

        int bins = Math.max(3, CameraConfig.POPULATION_BIN_COUNT);
        int radius = Math.max(0, CameraConfig.POPULATION_BIN_RADIUS);
        double[] binCount = new double[bins];
        double[] binSumX = new double[bins];

        for (BlobSample sample : samples) {
            int bin = Math.max(0, Math.min(bins - 1, (int) Math.floor(sample.normalizedX * bins)));
            binCount[bin] += 1.0;
            binSumX[bin] += sample.normalizedX;
        }

        int bestBin = 0;
        double bestClusterCount = -1.0;
        double bestClusterSumX = 0.0;
        for (int i = 0; i < bins; i++) {
            double clusterCount = 0.0;
            double clusterSumX = 0.0;
            for (int j = Math.max(0, i - radius); j <= Math.min(bins - 1, i + radius); j++) {
                clusterCount += binCount[j];
                clusterSumX += binSumX[j];
            }
            if (clusterCount > bestClusterCount) {
                bestClusterCount = clusterCount;
                bestClusterSumX = clusterSumX;
                bestBin = i;
            }
        }

        double target = (bestClusterCount > 0)
                ? bestClusterSumX / bestClusterCount
                : (bestBin + 0.5) / bins;
        return new PopulationTarget(clamp01(target), Math.max(0.0, bestClusterCount));
    }

    private static ColorBlobLocatorProcessor buildLocator(ColorRange colorRange) {
        return new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(colorRange)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.asUnityCenterCoordinates(
                        CameraConfig.ROI_LEFT,
                        CameraConfig.ROI_TOP,
                        CameraConfig.ROI_RIGHT,
                        CameraConfig.ROI_BOTTOM
                ))
                .setDrawContours(true)
                .setBlurSize(5)
                .setErodeSize(3)
                .setDilateSize(3)
                .build();
    }

    private static class Accumulation {
        double sampleCount = 0.0;
        List<BlobSample> samples = new ArrayList<>();
    }

    private static class BlobSample {
        final double normalizedX;

        BlobSample(double normalizedX) {
            this.normalizedX = normalizedX;
        }
    }

    private static class PopulationTarget {
        final double targetNormalized;
        final double peakCount;

        PopulationTarget(double targetNormalized, double peakCount) {
            this.targetNormalized = targetNormalized;
            this.peakCount = peakCount;
        }
    }

    private static class ZoneOverlayProcessor implements VisionProcessor {
        private volatile double observedBlend = 0.5;

        private final Paint outlinePaint = new Paint();
        private final Paint observedPaint = new Paint();

        ZoneOverlayProcessor() {
            outlinePaint.setColor(Color.WHITE);
            outlinePaint.setStrokeWidth(2f);
            outlinePaint.setStyle(Paint.Style.STROKE);

            observedPaint.setColor(Color.argb(110, 0, 255, 0));
            observedPaint.setStyle(Paint.Style.FILL);
        }

        void setObservedBlend(double observedBlend) {
            this.observedBlend = clamp01(observedBlend);
        }

        @Override
        public void init(int width, int height, CameraCalibration calibration) {}

        @Override
        public Object processFrame(Mat frame, long captureTimeNanos) {
            return null;
        }

        @Override
        public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight,
                                float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
            float left = normalizedXToCanvas(CameraConfig.ROI_LEFT, onscreenWidth);
            float right = normalizedXToCanvas(CameraConfig.ROI_RIGHT, onscreenWidth);
            float top = normalizedYToCanvas(CameraConfig.ROI_TOP, onscreenHeight);
            float bottom = normalizedYToCanvas(CameraConfig.ROI_BOTTOM, onscreenHeight);

            if (right < left) {
                float tmp = left;
                left = right;
                right = tmp;
            }
            if (bottom < top) {
                float tmp = top;
                top = bottom;
                bottom = tmp;
            }

            float roiWidth = right - left;
            float targetWidth = Math.max(roiWidth * 0.18f, 16f);
            float observedCenterX = (float) (left + roiWidth * observedBlend);

            float observedLeft = Math.max(left, observedCenterX - targetWidth / 2f);
            float observedRight = Math.min(right, observedCenterX + targetWidth / 2f);

            canvas.drawRect(observedLeft, top, observedRight, bottom, observedPaint);
            canvas.drawRect(left, top, right, bottom, outlinePaint);
        }

        private float normalizedXToCanvas(double normalizedX, int width) {
            return (float) ((normalizedX + 1.0) * 0.5 * width);
        }

        private float normalizedYToCanvas(double normalizedY, int height) {
            return (float) ((1.0 - ((normalizedY + 1.0) * 0.5)) * height);
        }
    }
}
