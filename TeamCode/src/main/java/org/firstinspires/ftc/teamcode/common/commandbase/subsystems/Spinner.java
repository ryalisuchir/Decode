package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.controller.PIDFController;

import org.firstinspires.ftc.teamcode.common.utility.G;

public class Spinner {
    private final DcMotorEx i, t;
    private final ServoImplEx g;

    private double intakeTargetPower = 0.0;
    private double currentIntakePower = 0.0;

    private double transferTargetPower = 0.0;
    private long lastUpdateTimeNs = 0;

    private boolean autoTransferTriggered = false;
    private int intakeActionVersion = 0;

    public Spinner(DcMotorEx i, DcMotorEx t, ServoImplEx g) {
        this.i = i;
        this.t = t;
        this.g = g;
    }

    public void transferStart() {
        G.transferState = G.TransferState.TRANSFERRING;
        setTransferTarget(-1);
    }

    public void transferStop() {
        G.transferState = G.TransferState.STOPPED;
        setTransferTarget(0);
    }

    public void openGate() {
        if (Math.abs(G.GATE_OPEN - g.getPosition()) < 0.05) return;

        g.setPosition(G.GATE_OPEN);
    }

    public void closeGate() {
        g.setPosition(G.GATE_CLOSED);
    }

    private void setIntakeTarget(double power) {
        intakeTargetPower = power;
    }

    private void setTransferTarget(double power) {
        transferTargetPower = power;
    }

    private int nextIntakeActionVersion() {
        return ++intakeActionVersion;
    }

    private boolean isCurrentIntakeAction(int actionVersion) {
        return actionVersion == intakeActionVersion;
    }

    private void applyIntakeStop() {
        setIntakeTarget(0);
        G.intakeState = G.IntakeState.STOPPED;
        closeGate();
    }

    public void intakeIn() {
        nextIntakeActionVersion();
        G.intakeState = G.IntakeState.INTAKING;
        G.transferState = G.TransferState.INTAKING;

        setIntakeTarget(G.MAX_INTAKING_POWER);
        setTransferTarget(0.4);
    }

    public SequentialCommandGroup intakeOut() {
        int actionVersion = nextIntakeActionVersion();
        G.intakeState = G.IntakeState.STOPPED;
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    if (!isCurrentIntakeAction(actionVersion)) return;
                    closeGate();
                    setIntakeTarget(-0.86);
                }),
                new WaitCommand(300),
                new InstantCommand(() -> {
                    if (isCurrentIntakeAction(actionVersion)) applyIntakeStop();
                })
        );
    }

    public void intakeStop() {
        nextIntakeActionVersion();
        applyIntakeStop();
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
        if (G.match == G.Match.AUTO) {
            wait = G.GATE_WAIT_AUTO;
        } else {
            wait = G.GATE_WAIT_TELE;
        }

        if (!threeBallsDetected()) {
            int actionVersion = nextIntakeActionVersion();
            return new ParallelCommandGroup(new SequentialCommandGroup(new InstantCommand(() -> {
                if (isCurrentIntakeAction(actionVersion)) setIntakeTarget(-0.6);
            }), new WaitCommand(wait), new InstantCommand(() -> {
                if (!isCurrentIntakeAction(actionVersion)) return;
                setIntakeTarget(0);
                G.intakeState = G.IntakeState.STOPPED;
                g.setPosition(G.GATE_OPEN);
            })), new InstantCommand(this::transferStop));
        } else {
            return transfer();
        }
    }

    public boolean threeBallsDetected() {
        return G.ballColors[0] != G.BallColor.NONE && G.ballColors[1] != G.BallColor.NONE && G.ballColors[2] != G.BallColor.NONE;
    }

    public boolean oneBallDetected() {
        return G.ballColors[0] != G.BallColor.NONE || G.ballColors[1] != G.BallColor.NONE || G.ballColors[2] != G.BallColor.NONE;
    }

    public ParallelCommandGroup toggleIn() {
        if (G.intakeState != G.IntakeState.INTAKING) {
            return intake();
        } else if (threeBallsDetected()) {
            return transfer();
        } else {
            return stop();
        }
    }

    public void periodic() {
        i.setPower(intakeTargetPower);
        t.setPower(transferTargetPower);

        if (oneBallDetected()) {
            G.shooterState = G.ShooterState.SHOOTING;

            if (G.match == G.Match.AUTO && (G.turretState != G.TurretState.BLUE_CLOSE_OBELISK && G.turretState != G.TurretState.RED_CLOSE_OBELISK && G.turretState != G.TurretState.SET_POSITION)) {
                G.turretState = G.TurretState.FOLLOWING;
            }
        }

        if (G.intakeState == G.IntakeState.INTAKING
                && threeBallsDetected()
                && !autoTransferTriggered) {
            autoTransferTriggered = true;
            CommandScheduler.getInstance().schedule(transfer());
        }

        if (G.intakeState == G.IntakeState.STOPPED //new code for bug prevention in tele
                && threeBallsDetected() && Math.abs(t.getPower()) < 0.1) {
            CommandScheduler.getInstance().schedule(transfer());
        }

        if (G.intakeState != G.IntakeState.INTAKING) {
            autoTransferTriggered = false;
        }
    }
}
