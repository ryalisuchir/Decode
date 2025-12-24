package org.firstinspires.ftc.teamcode.common.utility.functions;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;

import java.util.ArrayList;
import java.util.List;

public class TurretCalibrator {

    private static final double START = 0.0;
    private static final double END = 1.0;
    private static final double STEP = 0.05;

    private double currentServo = START;
    private boolean lastX = false;
    private boolean finished = false;

    private final List<Point> samples = new ArrayList<>();

    public void update(
            Turret turret,
            Gamepad gamepad,
            Telemetry telemetry,
            double robotX,
            double robotY,
            double robotHeading
    ) {
        if (finished) {
            telemetryLUT(telemetry);
            return;
        }

        turret.setFixedPosition(currentServo);

        boolean x = gamepad.x;
        if (x && !lastX) {

            double angle = turret.getTurretAngleToGoal(
                    robotX,
                    robotY,
                    robotHeading
            );

            samples.add(new Point(angle, currentServo));

            currentServo += STEP;

            if (currentServo > END) {
                finished = true;
            }
        }

        lastX = x;

        telemetry.addLine("Turret Calibrating...");
        telemetry.addData("Servo", "%.2f", currentServo);
        telemetry.addData("Samples", samples.size());
        telemetry.addLine("Align turret, press X to record.");
    }

    private void telemetryLUT(Telemetry telemetry) {
        telemetry.addLine("Turret LUT Data:");
        telemetry.addLine("angle , servo");

        for (Point p : samples) {
            telemetry.addLine(
                    String.format("%.5f , %.3f", p.angle, p.servo)
            );
        }
    }

    private static class Point {
        double angle;
        double servo;

        Point(double angle, double servo) {
            this.angle = angle;
            this.servo = servo;
        }
    }
}