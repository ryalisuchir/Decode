package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.utility.Globals;

public class Spinner {
    private final DcMotorEx i, t;
    private final ServoImplEx g;

    private double intakeTargetPower = 0.0;
    private double currentIntakePower = 0.0;

    private double transferTargetPower = 0.0;
    private long lastUpdateTimeNs = 0;

    private boolean autoTransferTriggered = false;

    public Spinner(DcMotorEx i, DcMotorEx t, ServoImplEx g) {
        this.i = i;
        this.t = t;
        this.g = g;
    }

    public void transferStart() {
        Globals.transferState = Globals.TransferState.TRANSFERRING;
        setTransferTarget(Globals.MAX_TRANSFER_POWER);
    }

    public void transferStop() {
        Globals.transferState = Globals.TransferState.STOPPED;
        setTransferTarget(0);
    }

    public void openGate() {
        if (Math.abs(Globals.GATE_OPEN - g.getPosition()) < 0.05) return;

        g.setPosition(Globals.GATE_OPEN);
    }

    public void closeGate() {
        g.setPosition(Globals.GATE_CLOSED);
    }

    private void setIntakeTarget(double power) {
        intakeTargetPower = power;
    }

    private void setTransferTarget(double power) {
        if (Math.abs(power - t.getPower()) < 0.05) return;

        transferTargetPower = power;
    }

    public void intakeIn() {
        Globals.intakeState = Globals.IntakeState.INTAKING;
        Globals.transferState = Globals.TransferState.INTAKING;

        setIntakeTarget(Globals.MAX_INTAKING_POWER);
        setTransferTarget(Globals.TRANSFER_INTAKING);
    }

    public SequentialCommandGroup intakeOut() {
        Globals.intakeState = Globals.IntakeState.STOPPED;

        return new SequentialCommandGroup(
                new InstantCommand(() -> setIntakeTarget(-0.4)),
                new WaitCommand(600),
                new InstantCommand(this::intakeStop)
        );
    }

    public void intakeStop() {
        setIntakeTarget(0);
        Globals.intakeState = Globals.IntakeState.STOPPED;
        closeGate();
    }


    public ParallelCommandGroup intake() {
        return new ParallelCommandGroup(new InstantCommand(this::intakeIn), new InstantCommand(this::openGate));
    }

    public ParallelCommandGroup transfer() {
        return new ParallelCommandGroup(
                intakeOut(),
                new InstantCommand(this::transferStart)
        );
    }

    public ParallelCommandGroup stop() {
        long wait;
        if (Globals.match == Globals.Match.AUTO) {
            wait = Globals.GATE_WAIT_AUTO;
        } else {
            wait = Globals.GATE_WAIT_TELE;
        }

        if (!threeBallsDetected()) {
            return new ParallelCommandGroup(new SequentialCommandGroup(new InstantCommand(() -> setIntakeTarget(-0.6)), new WaitCommand(wait), new InstantCommand(() -> {
                setIntakeTarget(0);
                Globals.intakeState = Globals.IntakeState.STOPPED;
                g.setPosition(Globals.GATE_OPEN);
            })), new InstantCommand(this::transferStop));
        } else {
            return transfer();
        }
    }

    public boolean threeBallsDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE && Globals.ballColors[1] != Globals.BallColor.NONE && Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public boolean oneBallDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE || Globals.ballColors[1] != Globals.BallColor.NONE || Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public ParallelCommandGroup toggleIn() {
        if (Globals.intakeState != Globals.IntakeState.INTAKING) {
            return intake();
        } else if (threeBallsDetected()) {
            return transfer();
        } else {
            return stop();
        }
    }

    public void periodic() {
        long now = System.nanoTime();

        if (lastUpdateTimeNs == 0) {
            lastUpdateTimeNs = now;
        }

        double dt = (now - lastUpdateTimeNs) * 1e-9;
        lastUpdateTimeNs = now;

        double delta = intakeTargetPower - currentIntakePower;

        if (Math.abs(delta) <= 0.05) {
            currentIntakePower = intakeTargetPower;
        } else {
            double maxDelta = Globals.POWER_RAMP_PER_SEC * dt;

            if (Math.abs(delta) > maxDelta) {
                currentIntakePower += Math.signum(delta) * maxDelta;
            } else {
                currentIntakePower = intakeTargetPower;
            }
        }

        i.setPower(currentIntakePower);
        t.setPower(transferTargetPower);

        if (oneBallDetected()) {
            Globals.shooterState = Globals.ShooterState.SHOOTING;

            if (Globals.match == Globals.Match.AUTO && (Globals.turretState != Globals.TurretState.BLUE_CLOSE_OBELISK && Globals.turretState != Globals.TurretState.RED_CLOSE_OBELISK)) {
                Globals.turretState = Globals.TurretState.FOLLOWING;
            }
        }

        if (Globals.intakeState == Globals.IntakeState.INTAKING
                && threeBallsDetected()
                && !autoTransferTriggered) {
            autoTransferTriggered = true;
            CommandScheduler.getInstance().schedule(transfer());
        }

        if (Globals.intakeState != Globals.IntakeState.INTAKING) {
            autoTransferTriggered = false;
        }
    }
}