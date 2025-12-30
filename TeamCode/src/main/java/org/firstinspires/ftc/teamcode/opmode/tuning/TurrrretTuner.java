package org.firstinspires.ftc.teamcode.opmode.tuning;

import android.util.Log;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@Autonomous
@Config
public class TurrrretTuner extends OpMode {
    Robot robot;
    public static double turretPosition = Globals.TURRET_RESET;
    Trigger intakeTrigger;
    public static double shooterPower = 0;
    public static double hoodAngle = Globals.HOOD_MAX;

    public double getTurretAngleToGoal(double robotX, double robotY, double robotHeadingRadians) {
        double goalX = Globals.BLUE_CASTLE.getX();
        double goalY = Globals.BLUE_CASTLE.getY();

        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);
        double turretAngle = angleToGoal - robotHeadingRadians;
        turretAngle = Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));

        return turretAngle;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addLine("Reset all encoders.");
        robot.dt.startDrive();

        intakeTrigger = new Trigger(
                () -> gamepad1.right_trigger > 0.1 && !robot.rotator.threeBallsDetected()
        );

        telemetry.update();
    }


    static double change(double val, int dp) {
        val = val*Math.pow(10,dp);
        val=Math.floor(val);
        val=val/Math.pow(10,dp);

        return val;
    }

    @Override
    public void loop() {
        robot.dt.drive(gamepad1);
        robot.r.setPosition(hoodAngle);
        robot.s1.set(shooterPower);
        robot.s2.set(shooterPower);

        robot.t1.setPosition(turretPosition);
        robot.t2.setPosition(turretPosition);

        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.INTAKING),
                                new InstantCommand(() -> robot.rotator.spinIn()),
                                new InstantCommand(() -> robot.rotator.openGate()),
                                KickCommands.resetAll(robot.kicker)
                        )
                )
                .whenInactive(
                        new InstantCommand(() -> {
                            if (robot.rotator.threeBallsDetected()) {
                                CommandScheduler.getInstance().schedule(
                                        robot.rotator.transfer()
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        robot.rotator.stop()
                                );
                            }
                        })
                );

        if (gamepad1.dpadLeftWasPressed()) {
            CommandScheduler.getInstance().schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(robot.kicker, 1))
            );
        }
        if (gamepad1.dpadRightWasPressed()) {
            CommandScheduler.getInstance().schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(robot.kicker, 2))
            );
        }
        if (gamepad1.dpadDownWasPressed()) {
            CommandScheduler.getInstance().schedule(
                    new UninterruptibleCommand(KickCommands.kickAndReset(robot.kicker, 3))
            );
        }

        if (gamepad1.xWasPressed()) {
            Log.i(String.valueOf(robot.t1.getPosition()), String.valueOf(change(getTurretAngleToGoal(robot.dt.getPose().getX(), robot.dt.getPose().getY(), robot.dt.getPose().getHeading()),4)));
        }

        telemetry.addData("Turret Reading: ", (change(getTurretAngleToGoal(robot.dt.getPose().getX(), robot.dt.getPose().getY(), robot.dt.getPose().getHeading()),4)));
        telemetry.addData("robot pos:", robot.dt.getPose());

        telemetry.update();

        robot.noOuttakeLoop(robot);
    }
}