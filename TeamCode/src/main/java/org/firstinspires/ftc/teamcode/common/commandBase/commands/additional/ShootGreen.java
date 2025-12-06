package org.firstinspires.ftc.teamcode.common.commandBase.commands.additional;

import com.seattlesolvers.solverslib.command.*;
import org.firstinspires.ftc.teamcode.common.robot.Globals;

public class ShootGreen extends SequentialCommandGroup {

    public ShootGreen() {

        Integer greenSlot = findFirstGreenSlot();

        if (greenSlot == null) {
            if (anyBallDetected()) {
                addCommands(
                        new InstantCommand(() -> {
                            Globals.shooterState  = Globals.ShooterState.SHOOTING;
                            Globals.transferState = Globals.TransferState.TRANSFERRING;
                            Globals.turretState   = Globals.TurretState.FOLLOWING;
                        })
                );
            }
            return;
        }

        addCommands(
                new InstantCommand(() -> Globals.gateState = Globals.GateState.CLOSED),
                new InstantCommand(() -> Globals.transferState = Globals.TransferState.TRANSFERRING),

                new RunCommand(() -> kick(greenSlot)).withTimeout(Globals.KICK_WAIT_TIME),

                new WaitCommand(Globals.KICK_FAILSAFE),
                new InstantCommand(() -> Globals.failsafeState = Globals.FailsafeState.KICK),
                new WaitCommand(Globals.KICK_WAIT_TIME),

                new InstantCommand(() -> {
                    if (!anyBallDetected()) {
                        resetAllLogic();
                    } else {
                        Globals.shooterState  = Globals.ShooterState.SHOOTING;
                        Globals.transferState = Globals.TransferState.TRANSFERRING;
                        Globals.turretState   = Globals.TurretState.FOLLOWING;
                    }
                })
        );
    }


    private Integer findFirstGreenSlot() {
        if (Globals.ballColor1 == Globals.BallColor1.G) return 1;
        if (Globals.ballColor2 == Globals.BallColor2.G) return 2;
        if (Globals.ballColor3 == Globals.BallColor3.G) return 3;
        return null;
    }

    // ============================================
    // ✅ BALL PRESENCE CHECK
    // ============================================
    private boolean anyBallDetected() {
        return Globals.ballColor1 != Globals.BallColor1.NONE ||
                Globals.ballColor2 != Globals.BallColor2.NONE ||
                Globals.ballColor3 != Globals.BallColor3.NONE;
    }

    // ============================================
    // ✅ KICK TARGET SLOT
    // ============================================
    private void kick(int slot) {
        switch (slot) {
            case 1: Globals.kicker1State = Globals.Kicker1State.KICK; break;
            case 2: Globals.kicker2State = Globals.Kicker2State.KICK; break;
            case 3: Globals.kicker3State = Globals.Kicker3State.KICK; break;
        }
    }

    // ============================================
    // ✅ FULL RESET (ONLY WHEN EMPTY)
    // ============================================
    private void resetAllLogic() {
        Globals.transferState = Globals.TransferState.STOPPED;
        Globals.shooterState  = Globals.ShooterState.STOPPED;
        Globals.kicker1State  = Globals.Kicker1State.RESET;
        Globals.kicker2State  = Globals.Kicker2State.RESET;
        Globals.kicker3State  = Globals.Kicker3State.RESET;
        Globals.turretState   = Globals.TurretState.RESET;
        Globals.hoodState     = Globals.HoodState.RESET;
        Globals.failsafeState = Globals.FailsafeState.RESET;
    }
}
