package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.UninterruptibleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.utility.KickCommands;
import org.firstinspires.ftc.teamcode.common.commandbase.commands.KickOrderACmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
public class SubsystemTest extends CommandOpMode {

    Robot r;
    private boolean threeBallRumbleLatched = false;

    @Override
    public void initialize() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.initLoop(r);
        r.dt.startDrive();
    }

    @Override
    public void run() {
        telemetry.addData("Color 1: ", Globals.ballColors[0]);
        telemetry.addData("Color 2: ", Globals.ballColors[1]);
        telemetry.addData("Color 3: ", Globals.ballColors[2]);
        telemetry.addData("Obelisk: ", Globals.obeliskOptions);

        telemetry.update();

        if (gamepad1.leftBumperWasPressed()) {
            schedule(r.rotator.toggleIn());
        }

        if (gamepad1.rightBumperWasPressed()) {
            schedule(
                    new UninterruptibleCommand(new KickOrderACmd(r))
            );
        }

        if (gamepad1.triangleWasPressed()) {
            schedule(KickCommands.kickAndResetMany(r.kicker, 1, 2, 3));
        }

        if (gamepad1.ps) {
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
