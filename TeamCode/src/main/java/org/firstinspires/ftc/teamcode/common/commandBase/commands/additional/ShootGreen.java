//package org.firstinspires.ftc.teamcode.common.commandBase.commands.additional;
//
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.seattlesolvers.solverslib.command.CommandBase;
//
//import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.IntakeSubsystem;
//import org.firstinspires.ftc.teamcode.common.commandBase.subsystems.ShooterSubsystem;
//import org.firstinspires.ftc.teamcode.common.robot.Globals;
//
//public class ShootGreen extends CommandBase {
//    private final IntakeSubsystem intake;
//    private final ShooterSubsystem shooter;
//    private final boolean[] greenStates;
//    private final int KICK_WAIT_TIME = 250; // ms
//
//    private int targetIndex = -1;
//    private ElapsedTime timer = new ElapsedTime();
//    private enum State { SCAN, KICK, WAIT, RESET, DONE }
//    private State state = State.SCAN;
//
//    public ShootGreen(IntakeSubsystem intake, ShooterSubsystem shooter, boolean[] greenStates) {
//        this.intake = intake;
//        this.shooter = shooter;
//        this.greenStates = greenStates;
//
//        addRequirements(intake, shooter);
//    }
//
//    @Override
//    public void initialize() {
//        state = State.SCAN;
//        targetIndex = -1;
//
//        // Find FIRST green slot
//        for (int i = 0; i < greenStates.length; i++) {
//            if (greenStates[i]) {
//                targetIndex = i;
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void execute() {
//        switch (state) {
//            case SCAN:
//                if (targetIndex == -1) {
//                    state = State.DONE;   // no green → do nothing
//                } else {
//                    state = State.KICK;
//                }
//                break;
//
//            case KICK:
//                kick(targetIndex);
//                timer.reset();
//                state = State.WAIT;
//                break;
//
//            case WAIT:
//                if (timer.milliseconds() >= KICK_WAIT_TIME) {
//                    state = State.RESET;
//                }
//                break;
//
//            case RESET:
//                shooter.resetAllKickers();
//                state = State.DONE;
//                break;
//
//            case DONE:
//                break;
//        }
//    }
//
//    private void kick(int slot) {
//        switch (slot) {
//            case 1: Globals.kicker1State = Globals.Kicker1State.KICK; break;
//            case 2: Globals.kicker2State = Globals.Kicker2State.KICK; break;
//            case 3: Globals.kicker3State = Globals.Kicker3State.KICK; break;
//        }
//    }
//
//    @Override
//    public boolean isFinished() {
//        return state == State.DONE;
//    }
//}
