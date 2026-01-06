package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.commands.CloseGateCmd;
import org.firstinspires.ftc.teamcode.common.utility.Globals;

public class Rotator {
    private final DcMotorEx i;
    private final DcMotorEx t;
    private final ServoImplEx g;

    private double targetPower = 0.0;
    private double currentPower = 0.0;
    private long lastUpdateTimeNs = 0;


    private boolean autoTransferTriggered = false;

    public Rotator(DcMotorEx i, DcMotorEx t, ServoImplEx g) {
        this.i = i;
        this.t = t;
        this.g = g;
    }

    public void openGate() {
        g.setPosition(Globals.GATE_OPEN);
    }

    private void setTarget(double power) {
        targetPower = power;
    }

    public void spinIn() {
        setTarget(Globals.MAX_INTAKING_POWER);
    }

    public void spinOut() {
        setTarget(Globals.MAX_TRANSFER_POWER);
    }

    public void spinStop() {
        setTarget(0);
    }

    public void spinEject() {
        setTarget(Globals.MAX_TRANSFER_POWER / 2.0);
    }

    public ParallelCommandGroup intake() {
        return new ParallelCommandGroup(
                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.INTAKING),
                new InstantCommand(this::spinIn),
                new InstantCommand(this::openGate)
        );
    }

    public ParallelCommandGroup transfer() {
        long wait;
        if (Globals.match == Globals.Match.AUTO) { wait = 0; } else { wait = 1000; }

        return new ParallelCommandGroup(
                new InstantCommand(() -> Globals.rotateState = Globals.RotateState.TRANSFERRING),
                new InstantCommand(this::spinOut),
                new SequentialCommandGroup(
                 new WaitCommand(wait),
                        new InstantCommand(() -> g.setPosition(Globals.GATE_CLOSED))
                )
        );
    }

    public ParallelCommandGroup stop() {
        if (!threeBallsDetected()) {
            return new ParallelCommandGroup(
                    new InstantCommand(() -> Globals.rotateState = Globals.RotateState.STOPPED),
                    new InstantCommand(this::spinStop),
                    new InstantCommand(this::openGate)
            );
        } else {
            return transfer();
        }
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

        long now = System.nanoTime();

        if (lastUpdateTimeNs == 0) {
            lastUpdateTimeNs = now;
        }

        double dt = (now - lastUpdateTimeNs) * 1e-9;
        lastUpdateTimeNs = now;

        double delta = targetPower - currentPower;

        if (Math.abs(delta) <= 0.05) {
            currentPower = targetPower;
        } else {
            double maxDelta = Globals.POWER_RAMP_PER_SEC * dt;

            if (Math.abs(delta) > maxDelta) {
                currentPower += Math.signum(delta) * maxDelta;
            } else {
                currentPower = targetPower;
            }
        }

        i.setPower(currentPower);
        t.setPower(currentPower);

        if (oneBallDetected() && (Globals.turretState != Globals.TurretState.BLUE_CLOSE_OBELISK && Globals.turretState != Globals.TurretState.RED_CLOSE_OBELISK)) {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if (Globals.match == Globals.Match.AUTO) {
                Globals.turretState = Globals.TurretState.FOLLOWING;
            }
        }

        if ((Globals.rotateState == Globals.RotateState.INTAKING
                && threeBallsDetected()
                && !autoTransferTriggered) || (Globals.rotateState == Globals.RotateState.STOPPED && oneBallDetected())) {
            autoTransferTriggered = true;
            CommandScheduler.getInstance().schedule(
                    new ParallelCommandGroup(
                            transfer(),
                            new InstantCommand(() -> {
                                Globals.shooterState = Globals.ShooterState.SHOOTING;
                                if (Globals.match == Globals.Match.AUTO) {Globals.turretState = Globals.TurretState.FOLLOWING;}
                            })
                    )
            );
        }

        if (Globals.rotateState != Globals.RotateState.INTAKING) {
            autoTransferTriggered = false;
        }
    }

}