package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.Halo;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.RapidAllCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystems.Turret;
import org.firstinspires.ftc.teamcode.common.utility.Vision;
//import org.firstinspires.ftc.teamcode.common.utility.turret.TurretCalibrator;

@TeleOp
@Config
@Configurable
@Disabled
public class TurretTester extends CommandOpMode {
    Halo r;
    Trigger intakeTrigger;
    public static double shooterPower = 0;
    public static double hoodAngle = Globals.HOOD.getMax();
//    TurretCalibrator t;

    @Override
    public void initialize() {
        r = new Halo(hardwareMap, Globals.Positions.RED_CUBE_START, Globals.Alliance.RED, Globals.Match.AUTO);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    }

    @Override
    public void run() {
        telemetry.update();

        r.noShooterLoop(r);
        Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
    }
}