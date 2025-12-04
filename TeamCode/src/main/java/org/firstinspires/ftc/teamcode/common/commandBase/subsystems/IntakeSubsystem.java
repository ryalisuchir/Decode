package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class IntakeSubsystem extends SubsystemBase {

    public final DcMotorEx intakeMotor;
    private double lastIntakePower = -999;

    private boolean wasIntaking = false;
    private boolean reversing = false;
    private long reverseEndTime = 0;

    public IntakeSubsystem(DcMotorEx intakeMotor) {
        this.intakeMotor = intakeMotor;
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

    private void setIntakePowerOnce(double power) {
        if (power != lastIntakePower) {
            intakeMotor.setPower(power);
            lastIntakePower = power;
        }
    }

    public void syncer() {

        long now = System.currentTimeMillis();

        if (Globals.intakeState == Globals.IntakeState.INTAKING && threeBallsDetected()) {
            Globals.intakeState = Globals.IntakeState.STOPPED;
        }

        if (wasIntaking && Globals.intakeState == Globals.IntakeState.STOPPED && !reversing) {
            reversing = true;
            reverseEndTime = now + 1000;

            Globals.gateState = Globals.GateState.CLOSED;
        }

        if (Globals.intakeState == Globals.IntakeState.INTAKING && reversing) {
            reversing = false;
        }


        if (reversing) {
            if (now < reverseEndTime) {
                intakeMotor.setPower(-0.7);
            } else {
                reversing = false;
                intakeMotor.setPower(0);
            }

            lastIntakePower = -999;
            wasIntaking = Globals.intakeState == Globals.IntakeState.INTAKING;
            return;
        }

        if (Globals.intakeState == Globals.IntakeState.INTAKING) {
            setIntakePowerOnce(Globals.MAX_INTAKING_POWER);

            Globals.kicker1State = Globals.Kicker1State.RESET;
            Globals.kicker2State = Globals.Kicker2State.RESET;
            Globals.kicker3State = Globals.Kicker3State.RESET;
            Globals.gateState = Globals.GateState.OPEN;
            Globals.failsafeState = Globals.FailsafeState.RESET;
        }

        else {
            setIntakePowerOnce(0);
            Globals.gateState = Globals.GateState.CLOSED;
        }

        if (isBallDetected()) {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            Globals.transferState = Globals.TransferState.TRANSFERRING;
            Globals.turretState = Globals.TurretState.FOLLOWING;
        }

        wasIntaking = Globals.intakeState == Globals.IntakeState.INTAKING;
    }
}
