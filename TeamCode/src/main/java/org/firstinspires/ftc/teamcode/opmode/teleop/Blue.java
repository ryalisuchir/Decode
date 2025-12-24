package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOneGreenTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOnePurpleTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.teleopspecific.KickOrderTCmd;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
public class Blue extends CommandOpMode {

    Robot r;
    private boolean threeBallRumbleLatched = false;
    Gamepad ahnaf, swetha;
    Trigger intakeTrigger;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.initLoop(r);
        r.dt.startDrive();
        ahnaf = gamepad1;
        swetha = gamepad2;

        intakeTrigger = new Trigger(
                () -> ahnaf.right_trigger > 0.1 && !r.rotator.threeBallsDetected()
        );
    }


    @Override
    public void run() {
        telemetry.addData("Color 1: ", Globals.ballColors[0]);
        telemetry.addData("Color 2: ", Globals.ballColors[1]);
        telemetry.addData("Color 3: ", Globals.ballColors[2]);
        telemetry.addData("Obelisk: ", Globals.obeliskOptions);

        telemetry.update();

        intakeTrigger
                .whileActiveContinuous(
                        new ParallelCommandGroup(
                                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.INTAKING),
                                new InstantCommand(() -> r.rotator.spinIn()),
                                new InstantCommand(() -> r.rotator.openGate()),
                                KickCommands.resetAll(r.kicker)
                        )
                )
                .whenInactive(
                        new InstantCommand(() -> {
                            if (r.rotator.threeBallsDetected()) {
                                CommandScheduler.getInstance().schedule(
                                        r.rotator.transfer()
                                );
                            } else {
                                CommandScheduler.getInstance().schedule(
                                        r.rotator.stop()
                                );
                            }
                        })
                );

        if (gamepad1.leftBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOrderTCmd(r))
            );
        }

        if (gamepad1.ps || gamepad2.ps) {
            schedule(
                    new ParallelCommandGroup(
                            r.dt.corner(),
                            new InstantCommand(() -> {
                                gamepad1.rumble(1000);
                                gamepad2.rumble(1000);
                            })
                    )
            );
        }

        //Failsafes:
        if (gamepad2.leftBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOneGreenTCmd(r))
            );
        }

        if (gamepad2.rightBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOnePurpleTCmd(r))
            );
        }

        if (gamepad2.triangleWasPressed()) {
            schedule(KickCommands.kickAndResetMany(r.kicker, 1, 2, 3));
        }

        if (r.rotator.threeBallsDetected() && !threeBallRumbleLatched) {
            schedule(
                    new InstantCommand(() -> {
                        gamepad1.rumble(1000);
                        gamepad2.rumble(1000);
                        threeBallRumbleLatched = true;
                    })
            );
        }

        if (!r.rotator.threeBallsDetected()) {
            threeBallRumbleLatched = false;
        }

        r.dt.drive(gamepad1);
        r.loop(r);
    }
}
