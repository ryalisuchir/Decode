package org.firstinspires.ftc.teamcode.common.commandBase.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.common.robot.Globals;
import org.firstinspires.ftc.teamcode.util.Interpolated2DLUT;
import org.firstinspires.ftc.teamcode.util.PIDController;

public class ShooterSubsystem {

    private final DcMotorEx shooterMotor;
    private final DcMotorEx transferMotor;
    private final ServoImplEx hoodServo;
    private final ServoImplEx kicker1, kicker2, kicker3;

    private final PIDController velocityPID;
    private final Interpolated2DLUT shooterLUT;
    private final ElapsedTime timer = new ElapsedTime();

    // Runtime vars
    private boolean shooting = false;
    private double distance = 0;
    private double angle = 0;
    private double targetVelocity = 0;
    private double targetHoodPos = 0;
    private double shooterPower = 0;
    private int currentKicker = 0;
    private boolean kicking = false;

    public ShooterSubsystem(HardwareMap hwMap) {
        shooterMotor  = hwMap.get(DcMotorEx.class, "shooter");
        transferMotor = hwMap.get(DcMotorEx.class, "transfer");
        hoodServo     = hwMap.get(ServoImplEx.class, "hood");
        kicker1       = hwMap.get(ServoImplEx.class, "kicker1");
        kicker2       = hwMap.get(ServoImplEx.class, "kicker2");
        kicker3       = hwMap.get(ServoImplEx.class, "kicker3");

        shooterMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        transferMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        velocityPID = new PIDController(Globals.SHOOTER_kP, Globals.SHOOTER_kI, Globals.SHOOTER_kD);
        velocityPID.setTolerance(Globals.SHOOTER_VELOCITY_TOLERANCE);

        // Build lookup table (distance, angle → hood, velocity)
        shooterLUT = new Interpolated2DLUT();
        shooterLUT.addPoint(10, 0, 0.25, 2100);
        shooterLUT.addPoint(20, 0, 0.32, 2400);
        shooterLUT.addPoint(30, 5, 0.39, 2600);
        shooterLUT.addPoint(40, 10, 0.46, 2850);
        shooterLUT.addPoint(50, 15, 0.52, 3100);
    }

    // ---------------------------------------------------------------------
    // Periodic update (called each loop)
    // ---------------------------------------------------------------------
    public void periodic() {
        if (shooting) {
            updateTargets();
            controlShooterVelocity();
            maintainHoodPosition();

            if (!kicking && atTargetVelocity()) {
                advanceKicker();
            }
            handleKickCycle();
        } else {
            idleState();
        }
    }

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------
    public void startShooting(double distance, double angle) {
        this.distance = distance;
        this.angle = angle;
        shooting = true;
        currentKicker = 0;
        kicking = false;
    }

    public void stopShooting() {
        shooting = false;
    }

    public void shootColor(Globals.KickerColor color) {
        if (!atTargetVelocity()) return;
        int kickerToFire = getKickerForColor(color);
        if (kickerToFire != -1) performKick(kickerToFire);
    }

    private void updateTargets() {
        double[] out = shooterLUT.get(distance, angle);
        targetHoodPos = out[0];
        targetVelocity = out[1];
    }

    private void controlShooterVelocity() {
        double currentVel = shooterMotor.getVelocity();
        double pidOut = velocityPID.calculate(currentVel, targetVelocity);
        shooterPower = clip(pidOut, 0, Globals.MAX_SHOOTER_POWER);
        shooterMotor.setPower(shooterPower);
    }

    private void maintainHoodPosition() {
        hoodServo.setPosition(targetHoodPos);
    }

    private boolean atTargetVelocity() {
        double err = Math.abs(shooterMotor.getVelocity() - targetVelocity);
        return err <= Globals.SHOOTER_VELOCITY_TOLERANCE;
    }

    private void idleState() {
        shooterMotor.setPower(0);
        transferMotor.setPower(0);
        hoodServo.setPosition(Globals.HOOD_IDLE_POS);
        kicking = false;
    }

    private void advanceKicker() {
        currentKicker++;
        if (currentKicker > 3) {
            shooting = false;
            return;
        }
        performKick(currentKicker);
    }

    private void performKick(int num) {
        kicking = true;
        timer.reset();

        ServoImplEx kicker = switch (num) {
            case 1 -> kicker1;
            case 2 -> kicker2;
            case 3 -> kicker3;
            default -> null;
        };
        if (kicker == null) return;

        kicker.setPosition(Globals.KICKER_KICK_POS);
        transferMotor.setPower(Globals.MAX_TRANSFER_POWER);
    }

    private void handleKickCycle() {
        if (!kicking) return;

        if (timer.milliseconds() > Globals.KICK_WAIT_TIME) {
            // reset kickers and stop transfer
            kicker1.setPosition(Globals.KICKER_RESET_POS);
            kicker2.setPosition(Globals.KICKER_RESET_POS);
            kicker3.setPosition(Globals.KICKER_RESET_POS);
            transferMotor.setPower(0);
            kicking = false;
        }
    }

    private int getKickerForColor(Globals.KickerColor color) {
        return switch (Globals.obeliskOptions) {
            case PPG -> (color == Globals.KickerColor.PURPLE ? 1 :
                    color == Globals.KickerColor.GREEN ? 2 : 3);
            case PGP -> (color == Globals.KickerColor.PURPLE ? 2 :
                    color == Globals.KickerColor.GREEN ? 1 : 3);
            case GPP -> (color == Globals.KickerColor.GREEN ? 1 :
                    color == Globals.KickerColor.PURPLE ? 2 : 3);
        };
    }

    private double clip(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // For telemetry/debug
    public double getTargetVelocity() { return targetVelocity; }
    public double getShooterPower() { return shooterPower; }
    public double getTargetHoodPos() { return targetHoodPos; }
}
