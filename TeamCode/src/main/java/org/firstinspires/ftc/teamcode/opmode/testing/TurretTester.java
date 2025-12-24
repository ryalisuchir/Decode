package org.firstinspires.ftc.teamcode.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.utility.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.RedTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.Robot;

@TeleOp
public class TurretTester extends OpMode {
    Robot r;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();

    @Override
    public void init() {
        r = new Robot(hardwareMap, Globals.DEFAULT_START_POSE, Globals.Side.BLUE, true);
        r.dt.startDrive();
    }

    @Override
    public void loop() {

        r.dt.drive(gamepad1);

        double servoPosition = blueTurretLUT.getServoValue(r.dt.getTurretAngle());

        r.t1.setPosition(servoPosition);
        r.t2.setPosition(servoPosition);

        r.clearCache();
        r.dt.periodic();
    }
}