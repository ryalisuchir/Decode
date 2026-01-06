package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@Autonomous
public class PullPose extends OpMode {
    Robot r;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        r = new Robot(hardwareMap, Globals.OTHER_DEFAULT_START_POSE, Globals.Side.RED, true);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void init_loop() {
        telemetry.addLine("Created all subsystems.");
        r.initLoop(r);
        telemetry.update();
    }

    public void start() {
    }

    @Override
    public void loop() {
        r.dt.loop();
        telemetry.addData("Current Pose: ", r.dt.getPose());
    }

    @Override
    public void stop() {
        r.stop();
    }
}