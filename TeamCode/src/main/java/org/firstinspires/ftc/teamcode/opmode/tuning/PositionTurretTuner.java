package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

//@Autonomous
@Config
public class PositionTurretTuner extends OpMode {
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
        telemetry.update();

        intakeTrigger = new Trigger(
                () -> gamepad1.right_trigger > 0.1 && !robot.spinner.threeBallsDetected()
        );
        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> robot.spinner.intakeIn()),
                                new InstantCommand(() -> robot.spinner.openGate()),
                                KickCommands.resetAll(robot.kicker)
                        )
                )
                .whenInactive(
                        new InstantCommand(() -> {
                            if (robot.spinner.oneBallDetected()) {
                                CommandScheduler.getInstance().schedule(
                                        robot.spinner.transfer()
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        new InstantCommand(() -> robot.spinner.intakeStop()),
                                        new InstantCommand(() -> robot.spinner.transferStop())
                                );
                            }
                        })
                );

    }


    static double change(double val, int dp) {
        val = val*Math.pow(10,dp);
        val=Math.floor(val);
        val=val/Math.pow(10,dp);

        return val;
    }

    @Override
    public void loop() {
        robot.r.setPosition(hoodAngle);
        robot.s1.set(shooterPower);
        robot.s2.set(shooterPower);

        robot.dt.drive(gamepad1);
        robot.t1.setPosition(turretPosition);
        robot.t2.setPosition(turretPosition);

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

        telemetry.addData("Turret Reading: ", (change(getTurretAngleToGoal(robot.dt.getPose().getX(), robot.dt.getPose().getY(), robot.dt.getPose().getHeading()),4)));
        telemetry.addData("Servo Pos: ", robot.t1.getPosition());
        telemetry.addData("Robot Pose: ", robot.dt.getPose());
        telemetry.update();

        robot.noOuttakeLoop(robot);
    }
}