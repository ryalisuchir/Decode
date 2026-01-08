package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;

@TeleOp
public class TurretTester extends OpMode {
    Robot r;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.dt.startDrive();
        Globals.turretState = Globals.TurretState.FOLLOWING;
    }

    @Override
    public void loop() {
        r.dt.drive(gamepad1);
        r.turret.loop();

        if (gamepad1.crossWasPressed()) {
            CommandScheduler.getInstance().schedule(new InstantCommand(() -> {
                r.turret.applyVisionCorrectionOnce();
            }));
        }

        if (gamepad1.circleWasPressed()) {
            CommandScheduler.getInstance().schedule(new InstantCommand(() -> {
                r.turret.clearVisionCorrection();
            }));
        }

        r.clearCache();
        r.noOuttakeLoop(r);
        telemetry.addData("tx: ", Vision.getTx());
        telemetry.addData("Correct fid: ", Vision.hasCorrectFiducial());
        telemetry.addData("Robot Turret Pos:", r.turret.getTurretAngleToGoal(r.dt.getPose().getX(), r.dt.getPose().getY(), r.dt.getPose().getHeading()));
        telemetry.addData("Servo Pos: ", r.t1.getPosition());
        telemetry.addData("Pose: ", r.dt.getPose().getHeading());
        telemetry.update();
    }
}