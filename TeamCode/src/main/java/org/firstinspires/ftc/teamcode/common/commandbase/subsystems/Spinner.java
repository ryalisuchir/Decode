package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Globals;

public class Spinner {
    private final DcMotorEx i, t;
    private final ServoImplEx g, pivot;

    private double intakeTargetPower = 0.0;
    private double currentIntakePower = 0.0;

    private double transferTargetPower = 0.0;
    private long lastUpdateTimeNs = 0;

    private boolean transferLocked = false;

    private boolean autoTransferTriggered = false;
    private int intakeActionVersion = 0;

    public Spinner(DcMotorEx i, DcMotorEx t, ServoImplEx g, ServoImplEx pivot) {
        this.i = i;
        this.t = t;
        this.g = g;
        this.pivot = pivot;
    }

    public void transferStart() {
        Globals.transferState = Globals.TransferState.TRANSFERRING;
        setTransferTarget(Globals.Transfer.OUT_POWER);
    }

    public void transferStop() {
        Globals.transferState = Globals.TransferState.STOPPED;
        setTransferTarget(0);
    }

    public void pivotIntake() {
        if (Math.abs(Globals.Pivot.PIVOT_INTAKE - pivot.getPosition()) < 0.05) return;
        Globals.pivotState = Globals.PivotState.LOWERED;
        pivot.setPosition(Globals.Pivot.PIVOT_INTAKE);
    }

    public void pivotUp() {
        if (Math.abs(Globals.Pivot.PIVOT_RAISED - pivot.getPosition()) < 0.05) return;
        Globals.pivotState = Globals.PivotState.RAISED;
        pivot.setPosition(Globals.Pivot.PIVOT_RAISED);
    }

    public void pivotReady() {
        if (Math.abs(Globals.Pivot.PIVOT_RESTING - pivot.getPosition()) < 0.05) return;
        Globals.pivotState = Globals.PivotState.RESTING;
        pivot.setPosition(Globals.Pivot.PIVOT_RESTING);
    }

    public void openGate() {
        if (Math.abs(Globals.Gate.GATE_OPEN - g.getPosition()) < 0.05) return;
        g.setPosition(Globals.Gate.GATE_OPEN);
    }

    public void closeGate() {
        g.setPosition(Globals.Gate.GATE_CLOSED);
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
        Globals.intakeState = Globals.IntakeState.STOPPED;
        closeGate();
    }

    public void intakeIn() {
        nextIntakeActionVersion();
        Globals.intakeState = Globals.IntakeState.INTAKING;
        Globals.transferState = Globals.TransferState.INTAKING;

        setIntakeTarget(Globals.Intake.IN_POWER);
        setTransferTarget(Globals.Transfer.IN_POWER);
    }

    public SequentialCommandGroup intakeOut() {
        int actionVersion = nextIntakeActionVersion();
        Globals.intakeState = Globals.IntakeState.STOPPED;
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    if (!isCurrentIntakeAction(actionVersion)) return;
                    closeGate();
                    setIntakeTarget(Globals.Intake.OUT_POWER);
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
        return new ParallelCommandGroup(
                new InstantCommand(this::intakeIn),
                new InstantCommand(this::openGate),
                new InstantCommand(this::pivotIntake)
        );
    }

    public ParallelCommandGroup transfer() {
        nextIntakeActionVersion(); // invalidate any running intakeOut sequence
        Globals.intakeState = Globals.IntakeState.STOPPED; // kill immediately
        autoTransferTriggered = true; // prevent periodic() re-entry
        transferLocked = true;

        return new ParallelCommandGroup(
                new SequentialCommandGroup(
                        new InstantCommand(() -> {
                            closeGate();
                            setIntakeTarget(Globals.Intake.OUT_POWER);
                        }),
                        new WaitCommand(300),
                        new InstantCommand(() -> setIntakeTarget(0))
                ),
                new InstantCommand(this::transferStart),
                new InstantCommand(this::pivotUp)
        );
    }

    public ParallelCommandGroup stop() {
        long wait = Globals.Timings.GATE_WAIT;

        if (!threeBallsDetected()) {
            int actionVersion = nextIntakeActionVersion();
            return new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new InstantCommand(() -> {
                                if (isCurrentIntakeAction(actionVersion))
                                    setIntakeTarget(Globals.Intake.OUT_POWER);
                            }),
                            new WaitCommand(wait),
                            new InstantCommand(() -> {
                                if (!isCurrentIntakeAction(actionVersion)) return;
                                setIntakeTarget(0);
                                Globals.intakeState = Globals.IntakeState.STOPPED;
                                g.setPosition(Globals.Gate.GATE_OPEN);
                            })
                    ),
                    new InstantCommand(oneBallDetected() ? this::transferStart : this::transferStop),
                    new InstantCommand(this::pivotReady)
            );
        } else {
            return transfer();
        }
    }

    public boolean threeBallsDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE
                && Globals.ballColors[1] != Globals.BallColor.NONE
                && Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public boolean oneBallDetected() {
        return Globals.ballColors[0] != Globals.BallColor.NONE
                || Globals.ballColors[1] != Globals.BallColor.NONE
                || Globals.ballColors[2] != Globals.BallColor.NONE;
    }

    public ParallelCommandGroup toggleIn() {
        // Block intake entirely while shoot order is locked
        if (Globals.shootOrderLocked) return new ParallelCommandGroup();

        if (Globals.intakeState != Globals.IntakeState.INTAKING) {
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
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if ((Globals.match == Globals.Match.AUTO || Globals.match == Globals.Match.TESTING)
                    && (Globals.turretState != Globals.TurretState.SET_POSITION)) {
                Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
            }
        }

        // Auto-transfer on three balls while intaking
        if (Globals.intakeState == Globals.IntakeState.INTAKING
                && threeBallsDetected()
                && !autoTransferTriggered
                && !transferLocked) {
            autoTransferTriggered = true;
            transferLocked = true;
            CommandScheduler.getInstance().schedule(transfer());
        }

        if (Globals.intakeState != Globals.IntakeState.INTAKING) {
            autoTransferTriggered = false;
        }

        // Unlock everything only once all balls are fully gone
        if (!oneBallDetected() && !Globals.shootOrderLocked) {
            transferLocked = false;
        }

        // Clear shoot order lock once all balls have been shot/cleared
        if (!oneBallDetected() && Globals.shootOrderLocked) {
            Globals.shootOrderLocked = false;
            transferLocked = false;
        }
    }
}