package org.firstinspires.ftc.teamcode.opmode.testing;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.ShootingMaster;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class SubsystemTest extends CommandOpMode {

    Robot robot;

    @Override
    public void initialize() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
    }

    @Override
    public void run() {

        if (gamepad1.left_bumper) {
            Globals.intakeState = Globals.IntakeState.INTAKING;
        }

        if (gamepad1.right_bumper) {
            schedule(new ShootingMaster());
        }

        robot.loop(robot);
        telemetry.update();

    }
}