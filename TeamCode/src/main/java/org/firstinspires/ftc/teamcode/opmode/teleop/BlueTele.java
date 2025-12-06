package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandBase.commands.ShootingMaster;
import org.firstinspires.ftc.teamcode.common.commandBase.commands.additional.ShootGreen;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.common.robot.Robot;

@TeleOp
public class BlueTele extends CommandOpMode {

    Robot robot;

    private boolean rbLast = false;
    private boolean lbLast = false;
    private boolean triLast = false;

    private boolean threeBallRumbleLatched = false;

    @Override
    public void initialize() {
        robot = new Robot(hardwareMap, Globals.BLUE_FAR_START, Globals.Side.BLUE, false);

        robot.initLoop(robot);
        robot.follower.startTeleopDrive();
    }

    @Override
    public void run() {
        telemetry.addData("Starting pose:", robot.follower.getPose());

        robot.follower.setTeleOpDrive(
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_y),
                -0.5 * Math.tan(1.12 * gamepad1.left_stick_x),
                -0.5 * Math.tan(1.12 * gamepad1.right_stick_x),
                true
        );

        if (gamepad1.ps) {
            gamepad1.rumble(1000);
            robot.follower.setPose(Globals.DEFAULT_START_POSE);
        }

        telemetry.addData("Dist: ", robot.getDistanceToGoalPinpoint());
        telemetry.addData("Curr vel: ", robot.shooterSpinner2.getCorrectedVelocity());

        boolean rbNow = gamepad1.right_bumper;
        if (rbNow && !rbLast) {
            schedule(new ShootingMaster());
            telemetry.addLine("RB Pressed");
        }
        rbLast = rbNow;

        if (gamepad1.circle) schedule(new ShootGreen());

        boolean threeDetected =
                Globals.ballColor1 != Globals.BallColor1.NONE &&
                        Globals.ballColor2 != Globals.BallColor2.NONE &&
                        Globals.ballColor3 != Globals.BallColor3.NONE;

        if (threeDetected && !threeBallRumbleLatched) {
            gamepad1.rumble(1000); // 1 second rumble
            threeBallRumbleLatched = true;
        }

        if (!threeDetected) {
            threeBallRumbleLatched = false;
        }

        boolean lbNow = gamepad1.left_bumper;

        if (lbNow && !lbLast) {
            if (Globals.intakeState == Globals.IntakeState.STOPPED) {
                Globals.intakeState = Globals.IntakeState.INTAKING;
            } else {
                Globals.intakeState = Globals.IntakeState.STOPPED;
            }
        }

        lbLast = lbNow;

        boolean triNow = gamepad1.triangle;
        if (triNow && !triLast) {
            schedule(
                    new SequentialCommandGroup(
                            new InstantCommand(() -> Globals.kicker1State = Globals.Kicker1State.KICK),
                            new WaitCommand(1000),
                            new InstantCommand(() -> Globals.kicker2State = Globals.Kicker2State.KICK),
                            new WaitCommand(1000),
                            new InstantCommand(() -> Globals.kicker3State = Globals.Kicker3State.KICK),
                            new WaitCommand(1000)
                    )
            );
        }
        triLast = triNow;

        telemetry.addData("Intake State:", Globals.intakeState);
        telemetry.addData("Kicker 1:", Globals.kicker1State);
        telemetry.addData("Kicker 2:", Globals.kicker2State);
        telemetry.addData("Kicker 3:", Globals.kicker3State);

        telemetry.addData("Turret pos:", robot.turret1.getPosition());
        telemetry.addData("Ball 1:", Globals.ballColor1);
        telemetry.addData("Ball 2:", Globals.ballColor2);
        telemetry.addData("Ball 3:", Globals.ballColor3);
        telemetry.addData("3 Balls Detected:", threeDetected);
        telemetry.addData("Obelisk: ", Globals.obeliskOptions);

        telemetry.update();
        robot.loop(robot);
    }
}
