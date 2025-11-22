package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class IntakeSubsystem extends SubsystemBase {

    public final DcMotorEx intakeMotor;
    private static final double INTAKE_POWER = Globals.MAX_INTAKING_POWER;

    public IntakeSubsystem(DcMotorEx intakeMotorInput) {
        intakeMotor = intakeMotorInput;
    }

    public void runIntake() {
        intakeMotor.setPower(INTAKE_POWER);
    }

    public void stopIntake() {
        intakeMotor.setPower(0);
    }

    public boolean isBallDetected() {
        return Globals.ballColor1 != Globals.BallColor1.NONE ||
                Globals.ballColor2 != Globals.BallColor2.NONE ||
                Globals.ballColor3 != Globals.BallColor3.NONE;
    }

    public boolean threeBallsDetected() {
        return Globals.ballColor1 != Globals.BallColor1.NONE &&
                Globals.ballColor2 != Globals.BallColor2.NONE &&
                Globals.ballColor3 != Globals.BallColor3.NONE;
    }
}