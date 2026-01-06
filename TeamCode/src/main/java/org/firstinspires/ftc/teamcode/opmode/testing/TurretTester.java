package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;
import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;

@TeleOp
public class TurretTester extends OpMode {
    Robot r;

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.OTHER_DEFAULT_START_POSE, Globals.Side.RED, true);
        r.dt.startDrive();
    }

    @Override
    public void loop() {
        r.dt.drive(gamepad1);
        r.turret.followGoal();

        r.clearCache();
        r.noOuttakeLoop(r);
        telemetry.addData("tx: ", Vision.getTx());
        telemetry.addData("Correct fid: ", Vision.hasCorrectFiducial());
        telemetry.addData("Pose: ", r.dt.getPose().getHeading());
        telemetry.update();
    }
}