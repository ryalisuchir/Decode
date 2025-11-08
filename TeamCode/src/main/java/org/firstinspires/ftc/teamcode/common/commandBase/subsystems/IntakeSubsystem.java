package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

public class IntakeSubsystem extends SubsystemBase {
    private final ElapsedTime intakeTimer = new ElapsedTime();
    Robot robot;

    private boolean isRunning;
    public boolean manualStopRequested;

    private static final double INTAKE_POWER = Globals.MAX_INTAKING_POWER;
    private static final double HSV_GREEN_MIN_H = 60;
    private static final double HSV_GREEN_MAX_H = 180;
    private static final double HSV_PURPLE_MIN_H = 250;
    private static final double HSV_PURPLE_MAX_H = 320;
    private static final double HSV_MIN_SAT = 0.3;
    private static final double HSV_MIN_VAL = 0.1;

    public IntakeSubsystem(Robot robot) {
        this.robot = robot;
        intakeTimer.reset();
        isRunning = true;
        manualStopRequested = false;
    }

    public void startIntake() {
        robot.intake.setPower(INTAKE_POWER);
        intakeTimer.reset();
        isRunning = true;
    }

    public void stopIntake() {
        robot.intake.setPower(0);
        isRunning = false;
    }

    private boolean allSensorsSeeColor() {
        return isColorDetected(robot.brushlands1)
                && isColorDetected(robot.brushlands2)
                && isColorDetected(robot.brushlands3);
    }

    private boolean isColorDetected(RevColorSensorV3 sensor) {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(
                (int) (colors.red * 255),
                (int) (colors.green * 255),
                (int) (colors.blue * 255),
                hsv
        );
        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        boolean isGreen = (hue >= HSV_GREEN_MIN_H && hue <= HSV_GREEN_MAX_H);
        boolean isPurple = (hue >= HSV_PURPLE_MIN_H && hue <= HSV_PURPLE_MAX_H);
        boolean strongColor = sat >= HSV_MIN_SAT && val >= HSV_MIN_VAL;

        return strongColor && (isGreen || isPurple);
    }

    @Override
    public void periodic() {
        if (!isRunning) return;

        if (allSensorsSeeColor()) {
            stopIntake();
            return;
        }

        //Manual-Automatic Stops:
        if (
                Globals.opMode.equals(Globals.OpMode.AUTO) && intakeTimer.seconds() > Globals.MAX_TIME_SPENT_INTAKING ||
                manualStopRequested //have teleop button set this true
        ) {
            stopIntake();
        }
    }
}
