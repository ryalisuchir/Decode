package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class IntakeSubsystem extends SubsystemBase {

    public final DcMotorEx intakeMotor;

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

    public void syncer() {
        if (Globals.intakeState == Globals.IntakeState.INTAKING) {
            intakeMotor.setPower(Globals.MAX_INTAKING_POWER);
            Globals.kicker1State = Globals.Kicker1State.RESET;
            Globals.kicker2State = Globals.Kicker2State.RESET;
            Globals.kicker3State = Globals.Kicker3State.RESET;

            if (isBallDetected()) {
                Globals.shooterState = Globals.ShooterState.SHOOTING;
                Globals.turretState = Globals.TurretState.FOLLOWING;
            }
            if (threeBallsDetected()) {
                Globals.intakeState = Globals.IntakeState.STOPPED;
            }
        } else {
            intakeMotor.setPower(0);
        }
    }
}