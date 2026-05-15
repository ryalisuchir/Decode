package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.common.Globals;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.common.TurretConfig;
import org.firstinspires.ftc.teamcode.common.utility.ShooterLUT;
import org.firstinspires.ftc.teamcode.common.utility.functions.ShooterParams;
import org.firstinspires.ftc.teamcode.common.utility.tables.TimeOfFlightLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.TurretMath;

public class Shooter extends SubsystemBase {

    private final PIDFController flywheelController = new PIDFController(Globals.shooterCoefficients);

    public boolean reached = false;
    private double targetVelocity = 0.0;
    public double compensatedTurretDelta = 0.0;

    private final TimeOfFlightLUT tofLUT = new TimeOfFlightLUT();

    private final Motor shooterMotor1, shooterMotor2;
    private final ServoImplEx hood;
    private final Follower follower;

    private Vector2d customPosition = null;
    private final ShooterLUT shooterLUT = new ShooterLUT();

    public double hoodPose, velPos;

    private double velocityCap = 1700;
    private double velocityFloor = 1100;

    // ── Low-pass filtered velocities ──────────────────────────────────────────
    private double filteredVx = 0;
    private double filteredVy = 0;

    // ── Acceleration tracking ─────────────────────────────────────────────────
    private double prevFilteredVx = 0;
    private double prevFilteredVy = 0;
    private long   prevLoopTimeNs = System.nanoTime();

    public Shooter(
            Motor shooterMotor1,
            Motor shooterMotor2,
            ServoImplEx hood,
            Follower follower
    ) {
        this.shooterMotor1 = shooterMotor1;
        this.shooterMotor2 = shooterMotor2;
        this.hood = hood;
        this.follower = follower;
        flywheelController.setTolerance(Globals.SHOOTER_VEL_TOLERANCE);
    }

    public InstantCommand startShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.SHOOTING;
            if (
                    (Globals.match == Globals.Match.AUTO &&
                            Globals.turretState != Globals.TurretState.SET_POSITION &&
                            Globals.turretState != Globals.TurretState.FOLLOWING_OBELISK) ||
                            Globals.match == Globals.Match.TESTING
            ) {
                Globals.turretState = Globals.TurretState.FOLLOWING_GOAL;
            }
        });
    }

    public InstantCommand stopShooter() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            Globals.turretState = Globals.TurretState.RESET;
            targetVelocity = 0;
        });
    }

    public InstantCommand stopShooterFollow() {
        return new InstantCommand(() -> {
            Globals.shooterState = Globals.ShooterState.STOPPED;
            targetVelocity = 0;
        });
    }

    public void setVelocityCap(double cap) {
        this.velocityCap = cap;
    }

    public void clearVelocityCap() {
        this.velocityCap = 1700;
    }

    public void setVelocityFloor(double floor) {
        this.velocityFloor = floor;
    }

    public void clearVelocityFloor() {
        this.velocityFloor = 1100;
    }

    public void setCustomDistance(double x, double y) {
        this.customPosition = new Vector2d(x, y);
    }

    public void clearCustomDistance() {
        this.customPosition = null;
    }

    public void loop() {
        reached = flywheelController.atSetPoint();

        // ── Timing ────────────────────────────────────────────────────────────
        long nowNs = System.nanoTime();
        double dt  = (nowNs - prevLoopTimeNs) / 1e9;
        prevLoopTimeNs = nowNs;
        // Guard against first-loop dt spikes
        if (dt <= 0 || dt > 0.5) dt = 0.02;

        if (Globals.shooterState != Globals.ShooterState.SHOOTING &&
                Globals.match != Globals.Match.AUTO) {
            compensatedTurretDelta = 0.0;
            // Reset filters so they don't carry stale state into the next shot
            filteredVx     = 0;
            filteredVy     = 0;
            prevFilteredVx = 0;
            prevFilteredVy = 0;
            setShooterPower(Globals.MIN_SHOOTER_POWER);
            return;
        }

        Pose robotPose = follower.getPose();
        double rx  = robotPose.getX();
        double ry  = robotPose.getY();
        double rawVx = follower.getVelocity().getXComponent();
        double rawVy = follower.getVelocity().getYComponent();
        double omega = follower.getAngularVelocity();

        // ── Low-pass filter on velocity (tune VEL_FILTER_ALPHA: 0=smooth, 1=raw) ──
        double alpha = Globals.SOTM.VEL_FILTER_ALPHA;
        filteredVx = alpha * rawVx + (1.0 - alpha) * filteredVx;
        filteredVy = alpha * rawVy + (1.0 - alpha) * filteredVy;

        // ── Acceleration from filtered velocity ───────────────────────────────
        double ax = (filteredVx - prevFilteredVx) / dt;
        double ay = (filteredVy - prevFilteredVy) / dt;
        prevFilteredVx = filteredVx;
        prevFilteredVy = filteredVy;

        // Compute turret pivot position in field frame
        Pose pivotPose = getPivotFieldPose(robotPose);

        Pose lutInputPose = (customPosition != null)
                ? new Pose(customPosition.getX(), customPosition.getY())
                : new Pose(rx, ry);

        ShooterParams params = shooterLUT.getShooterValue(lutInputPose, Globals.alliance);

        TurretMath.CornerGoal cornerGoal = (Globals.alliance == Globals.Alliance.BLUE)
                ? TurretMath.CornerGoal.LEFT_BLUE
                : TurretMath.CornerGoal.RIGHT_RED;
        double[] goalCenter = TurretMath.getCornerGoalCenter(cornerGoal, rx, ry);
        double gx = goalCenter[0];
        double gy = goalCenter[1];

        double velocityOffset = Globals.SOTM.SHOOTER_ENABLED
                ? computeVelocityOffset(filteredVx, filteredVy, ax, ay, pivotPose, gx, gy)
                : 0.0;

        compensatedTurretDelta = Globals.SOTM.SHOOTER_ENABLED
                ? computeTurretDelta(filteredVx, filteredVy, omega, pivotPose, gx, gy)
                : 0.0;

        targetVelocity = params.shooterVel + velocityOffset;

        hood.setPosition(clamp(params.hoodPos, Globals.HOOD.getMin(), Globals.HOOD.getMax()));

        velPos   = targetVelocity;
        hoodPose = params.hoodPos;

        // Apply floor then cap so floor can never exceed cap
        double clampedVelocity = Math.min(Math.max(targetVelocity, velocityFloor), velocityCap);
        flywheelController.setSetPoint(clampedVelocity);
        double power = flywheelController.calculate(shooterMotor2.getCorrectedVelocity());
        setShooterPower(power);
    }

    /**
     * Returns the turret pivot position in field frame, accounting for robot heading.
     */
    public Pose getPivotFieldPose(Pose robotPose) {
        double heading = robotPose.getHeading();
        double cosH    = Math.cos(heading);
        double sinH    = Math.sin(heading);
        double pivotX  = TurretConfig.pivotX;
        double pivotY  = TurretConfig.pivotY;
        double fieldX  = robotPose.getX() + pivotX * cosH - pivotY * sinH;
        double fieldY  = robotPose.getY() + pivotX * sinH + pivotY * cosH;
        return new Pose(fieldX, fieldY, robotPose.getHeading());
    }

    /**
     * Shooter RPM offset based on radial velocity AND radial acceleration.
     * The accel term compensates for the changing radial component mid-decel/accel.
     */
    private double computeVelocityOffset(double vx, double vy, double ax, double ay,
                                         Pose pivotPose, double gx, double gy) {
        double dx   = gx - pivotPose.getX();
        double dy   = gy - pivotPose.getY();
        double dist = Math.hypot(dx, dy);
        if (dist < 1.0) return 0;

        double ux = dx / dist;
        double uy = dy / dist;

        double vRadial = vx * ux + vy * uy;
        double aRadial = ax * ux + ay * uy;

        double vRadialMps = vRadial * 0.0254;
        double aRadialMps = aRadial * 0.0254;

        double flywheelCircumferenceM  = Math.PI * (Globals.SOTM.SHOOTER_WHEEL_DIAMETER_MM / 1000.0);
        double counterCircumferenceM   = Math.PI * (Globals.SOTM.COUNTER_ROLLER_DIAMETER_MM  / 1000.0);
        double effectiveCircumferenceM = (flywheelCircumferenceM + 2.0 * counterCircumferenceM) / 3.0;

        // Velocity term (existing)
        double rpmDeltaVel  = -(vRadialMps / effectiveCircumferenceM) * 60.0;
        double velDeltaTicks = rpmDeltaVel / 60.0 * 28.0;

        // Acceleration term (new) — uses half flight-time lookahead
        double flightTime    = tofLUT.get(dist);
        double rpmDeltaAccel = -(aRadialMps * (flightTime * 0.5) / effectiveCircumferenceM) * 60.0;
        double accelDeltaTicks = rpmDeltaAccel / 60.0 * 28.0;

        return (velDeltaTicks * Globals.SOTM.SHOOTER_RPM_GAIN)
                + (accelDeltaTicks * Globals.SOTM.SHOOTER_ACCEL_GAIN);
    }

    /**
     * Turret lateral lead with added acceleration feedforward.
     * Accel term accounts for mid-reversal overshoot/undershoot.
     */
    private double computeTurretDelta(double vx, double vy, double omega,
                                      Pose pivotPose, double gx, double gy) {
        if (!Globals.SOTM.TURRET_TOF_COMP_ENABLED) return 0;

        double dx   = gx - pivotPose.getX();
        double dy   = gy - pivotPose.getY();
        double dist = Math.hypot(dx, dy);
        if (dist < 1.0) return 0;

        double robotSpeed = Math.hypot(vx, vy);
        if (robotSpeed < Globals.SOTM.TURRET_MIN_SPEED
                && Math.abs(omega) < 0.05) return 0;

        double ux = dx / dist;
        double uy = dy / dist;
        double lx = -uy;
        double ly =  ux;

        double flightTime = tofLUT.get(dist);

        double vLateral    = vx * lx + vy * ly;
        double lateralLead = Math.atan2(vLateral * flightTime, dist)
                * Globals.SOTM.TURRET_LINEAR_GAIN;

        double rotationalLead = omega * flightTime * Globals.SOTM.TURRET_ROTATIONAL_GAIN;

        return lateralLead + rotationalLead;
    }

    private void setShooterPower(double power) {
        shooterMotor1.set(power);
        shooterMotor2.set(power);
    }

    public boolean shooterIsSpunUp() {
        return flywheelController.atSetPoint();
    }

    public double getShooterVelocity() {
        return shooterMotor2.getCorrectedVelocity();
    }

    public double getShooterGoal() {
        return targetVelocity;
    }

    public double getShooterRPM() {
        return getShooterVelocity() / 28.0 * 60.0;
    }

    public double getShooterPower() {
        return shooterMotor2.get();
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}