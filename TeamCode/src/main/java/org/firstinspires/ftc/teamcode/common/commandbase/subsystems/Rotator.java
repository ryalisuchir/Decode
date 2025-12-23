package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

public class Rotator {
    private final DcMotorEx i;
    private final DcMotorEx t;
    private final ServoImplEx g;

    private boolean autoTransferTriggered = false;

    public Rotator(DcMotorEx i, DcMotorEx t, ServoImplEx g) {
        this.i = i;
        this.t = t;
        this.g = g;
    }

    public void closeGate() {
        g.setPosition(Globals.GATE_CLOSED);
    }

    public void openGate() {
        g.setPosition(Globals.GATE_OPEN);
    }

    public void set(double power) {
        i.setPower(power);
        t.setPower(power);
    }

    public void spinIn() {
        set(Globals.MAX_INTAKING_POWER);
    }

    public void spinOut() {
        set(Globals.MAX_TRANSFER_POWER);
    }

    public void spinStop() {
        set(0);
    }

    public void spinEject() {
        set(Globals.MAX_TRANSFER_POWER / 2);
    }

    public ParallelCommandGroup intake() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.INTAKING),
                new InstantCommand(this::spinIn),
                new InstantCommand(this::openGate)
        );
    }

    public ParallelCommandGroup transfer() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.TRANSFERRING),
                new InstantCommand(this::spinOut),
                new InstantCommand(this::closeGate)
        );
    }

    public ParallelCommandGroup stop() {
        if (!oneBallDetected()) {
            return new ParallelCommandGroup(
                    new InstantCommand(() -> Globals.rotateState = Globals.RotateState.STOPPED),
                    new InstantCommand(this::spinStop),
                    new InstantCommand(this::closeGate)
            );
        } else {
            return new ParallelCommandGroup(
                    new InstantCommand(() -> Globals.rotateState = Globals.RotateState.STOPPED),
                    new InstantCommand(this::spinOut),
                    new InstantCommand(this::closeGate)
            );
        }
    }

    public ParallelCommandGroup eject() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.EJECTING),
                new InstantCommand(this::spinEject),
                new InstantCommand(this::closeGate)
        );
    }

    public boolean threeBallsDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE &&
                Globals.ballColors[1] != Globals.BallColor.NONE &&
                Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public boolean oneBallDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE ||
                Globals.ballColors[1] != Globals.BallColor.NONE ||
                Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public ParallelCommandGroup toggleIn() {
        if (Globals.rotateState != Globals.RotateState.INTAKING) {
            return intake();
        } else if (threeBallsDetected()) {
            return transfer();
        } else {
            return stop();
        }
    }

    public void periodic() {
        if ((Globals.rotateState == Globals.RotateState.INTAKING
                && threeBallsDetected()
                && !autoTransferTriggered) || (Globals.rotateState == Globals.RotateState.STOPPED && oneBallDetected())) {
            autoTransferTriggered = true;
            CommandScheduler.getInstance().schedule(
                    new ParallelCommandGroup(
                            transfer(),
                            new InstantCommand(() -> Globals.shooterState = Globals.ShooterState.SHOOTING)
                    )
            );
        }

        if (Globals.rotateState != Globals.RotateState.INTAKING) {
            autoTransferTriggered = false;
        }
    }

}