package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretCalibrator;

@TeleOp
public class TurretTuner extends OpMode {
    Robot r;
    TurretCalibrator t;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.initLoop(r);
        r.dt.startDrive();
        t = new TurretCalibrator();
    }

    @Override
    public void loop() {
        t.update(
                r.turret,
                gamepad1,
                telemetry,
                r.dt.getPose().getX(),
                r.dt.getPose().getY(),
                r.dt.getPose().getHeading()
        );

        r.dt.drive(gamepad1);
        r.dt.periodic();
        r.clearCache();
        CommandScheduler.getInstance().run();

        telemetry.update();
    }
}