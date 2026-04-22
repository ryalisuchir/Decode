package org.firstinspires.ftc.teamcode.opmode.tuning;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;

@Config
@TeleOp
public class ShooterPIDTuning extends CommandOpMode {
    public static double P = 0.0023;
    public static double I = 0;
    public static double D = 0;
    public static double F = 0.00036;

    public static double TARGET_VEL = 0.0;
    public static double POS_TOLERANCE = 0;

    private static final PIDFController launcherPIDF = new PIDFController(P, I, D, F);

    public ElapsedTime timer;
    Halo r;

    @Override
    public void initialize() {
        r = new Halo(hardwareMap, Globals.Positions.RED_CUBE_START, Globals.Alliance.RED, Globals.Match.TESTING);
        r.hood.setPosition(Globals.HOOD.getMax());
        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        super.reset();
    }

    @Override
    public void run() {
        if (timer == null) {
            timer = new ElapsedTime();
        }

        double currentVel = r.shooter2.getCorrectedVelocity();

        launcherPIDF.setPIDF(P, I, D, F);

        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        launcherPIDF.setSetPoint(TARGET_VEL);

        double power = launcherPIDF.calculate(currentVel, TARGET_VEL);

        r.shooter1.set(power);
        r.shooter2.set(power);

        telemetry.addData("Loop Time", timer.milliseconds());
        timer.reset();

        telemetry.addData("Power: ", power);
        telemetry.addData("Target Velocity: ", TARGET_VEL);
        telemetry.addData("Actual Velocity: ", currentVel);
        telemetry.update();

        r.nothingLoop(r);
    }

    @Override
    public void end() {
        Log.v("P", String.valueOf(P));
        Log.v("I", String.valueOf(I));
        Log.v("D", String.valueOf(D));
        Log.v("F", String.valueOf(F));
        Log.v("posTolerance", String.valueOf(POS_TOLERANCE));
    }
}