package org.firstinspires.ftc.teamcode.opmode.tuning;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.Halo;

@Config
@TeleOp
public class ShooterPIDTuner extends CommandOpMode {
    public static double P = 0.001;
    public static double I = 0.001;
    public static double D = 0.000;
    public static double F = 0.0004;

    public static double TARGET_VEL = 0.0;
    public static double POS_TOLERANCE = 0;

    private static final PIDFController launcherPIDF = new PIDFController(P, I, D, F);

    public ElapsedTime timer;
    Halo r;

    @Override
    public void initialize() {
        r = new Halo(hardwareMap, G.BLUE_CUBE_START, G.Side.BLUE, true);
        r.r.setPosition(G.HOOD_MAX);
        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        super.reset();
    }

    @Override
    public void run() {
        if (timer == null) {
            timer = new ElapsedTime();
        }

        double currentVel = r.s1.getCorrectedVelocity();

        launcherPIDF.setPIDF(P, I, D, F);

        launcherPIDF.setTolerance(POS_TOLERANCE, 0);
        launcherPIDF.setSetPoint(TARGET_VEL);

        double power = launcherPIDF.calculate(currentVel, TARGET_VEL);

        r.s1.set(power);
        r.s2.set(power);

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