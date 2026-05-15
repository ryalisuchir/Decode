package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import static org.firstinspires.ftc.teamcode.common.TurretConfig.GEAR_RATIO;
import static org.firstinspires.ftc.teamcode.common.TurretConfig.MAX_SERVO_DEG;
import static org.firstinspires.ftc.teamcode.common.TurretConfig.MAX_SIDE_ROTATION;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.Globals;
import org.firstinspires.ftc.teamcode.common.TurretConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.utility.turret.TurretMath;

@Config
public class Turret {
    private static final double SERVO_UNITS_PER_DEGREE = 1.0 / (MAX_SERVO_DEG * GEAR_RATIO);
    private static final double CACHE_TOLERANCE = 0.001;
    public static double BACKLASH_PRELOAD_DEG = 8;
    private static final double BACKLASH_PRELOAD = BACKLASH_PRELOAD_DEG * SERVO_UNITS_PER_DEGREE;

    private final ServoImplEx s1;
    private final ServoImplEx s2;
    private final Follower follower;
    private final Globals.Alliance alliance;
    private final Shooter shooter;

    private double targetDeg = 0;
    private double manualOffsetDeg = 1.0;
    private double cachedServoPos = Double.NaN;

    public Turret(Globals.Alliance alliance, ServoImplEx s1, ServoImplEx s2,
                  Follower follower, Shooter shooter) {
        this.alliance = alliance;
        this.s1 = s1;
        this.s2 = s2;
        this.follower = follower;
        this.shooter = shooter;
    }

    public void resetTurret() {
        s1.setPosition(TurretConfig.TURRET_FORWARD);
        s2.setPosition(TurretConfig.TURRET_FORWARD);
        cachedServoPos = TurretConfig.TURRET_FORWARD;
    }

    /** Directly sets the turret target angle in degrees. Used by DBZ teleop. */
    public void setTargetDeg(double deg) {
        targetDeg = deg;
    }

    public InstantCommand farB() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 90;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -90;
            });
        }
    }

    public InstantCommand farBoi() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 65;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -65;
            });
        }
    }

    //Red 21s:
    public InstantCommand redCloseClose() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 88;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -88-10;
            });
        }
    }

    public InstantCommand redFirstSpike() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 42;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -42-10;
            });
        }
    }

    public InstantCommand redShootSpec1() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 68;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -68-10;
            });
        }
    }

    public InstantCommand redLarperShot() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 75;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -75-10;
            });
        }
    }

    public InstantCommand redShootGateShoot() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 63;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -63-10;
            });
        }
    }

    //Red Push 21s:
    public InstantCommand redPush21() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 88;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -88;
            });
        }
    }

    public InstantCommand redPushFirstSpike() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 42;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -42;
            });
        }
    }

    //Red Sorted 18s:
    public InstantCommand redSorted1() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -6;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = +6;
            });
        }
    }

    public InstantCommand redSorted2() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 67;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -67;
            });
        }
    }

    public InstantCommand redSortedGate() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 67;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -67;
            });
        }
    }

    public InstantCommand redSortedFar() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 83;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -83;
            });
        }
    }

    public InstantCommand redSortedClose() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 42;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -42;
            });
        }
    }

    //Sorted Pusher:
    public InstantCommand redPushShoot0() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 88;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -88;
            });
        }
    }

    //Far:
    public InstantCommand redFarReset() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 45;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -45;
            });
        }
    }

    public InstantCommand redFar0() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 20;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -20;
            });
        }
    }

    public InstantCommand redFar1() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 10;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -10;
            });
        }
    }

    public InstantCommand redFar2() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 30;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -30;
            });
        }
    }

    public InstantCommand redFarRest() {
        if (alliance == Globals.Alliance.RED) {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = 10;
            });
        } else {
            return new InstantCommand(() -> {
                Globals.turretState = Globals.TurretState.SET_POSITION;
                targetDeg = -10;
            });
        }
    }

    public InstantCommand followObeliskCmd() {
        return new InstantCommand(() -> {
            Globals.turretState = Globals.TurretState.FOLLOWING_OBELISK;
        });
    }

    public InstantCommand reset() {
        return new InstantCommand(this::resetTurret);
    }

    public void incrementOffset(boolean up) {
        manualOffsetDeg = up ? manualOffsetDeg + 1 : manualOffsetDeg - 1;
    }

    private void followGoal() {
        Pose pose = follower.getPose();

        TurretMath.CornerGoal cornerGoal = (alliance == Globals.Alliance.BLUE)
                ? TurretMath.CornerGoal.LEFT_BLUE
                : TurretMath.CornerGoal.RIGHT_RED;

        double turretRad = TurretMath.getTurretAngleSOTM(
                pose.getX(), pose.getY(), pose.getHeading(),
                TurretConfig.pivotX, TurretConfig.pivotY,
                cornerGoal,
                shooter.compensatedTurretDelta
        );

        targetDeg = Math.toDegrees(turretRad);
    }

    private void followObelisk() {
        Pose pose = follower.getPose();

        double turretRad = TurretMath.getTurretAngleToObelisk(
                pose.getX(), pose.getY(), pose.getHeading(),
                TurretConfig.pivotX, TurretConfig.pivotY
        );

        targetDeg = Math.toDegrees(turretRad);
    }

    private double calculateServoPos(double angleDeg) {
        double inputAngle = AngleUnit.normalizeDegrees(angleDeg) + manualOffsetDeg;
        inputAngle = Range.clip(inputAngle, -MAX_SIDE_ROTATION + manualOffsetDeg, MAX_SIDE_ROTATION + manualOffsetDeg);
        return TurretConfig.TURRET_FORWARD + (SERVO_UNITS_PER_DEGREE * inputAngle);
    }

    private void applyTarget() {
        double pos = calculateServoPos(targetDeg);
        if (Double.isNaN(cachedServoPos) || Math.abs(pos - cachedServoPos) > CACHE_TOLERANCE) {
            s1.setPosition(pos - BACKLASH_PRELOAD);
            s2.setPosition(pos + BACKLASH_PRELOAD);
            cachedServoPos = pos;
        }
    }

    public void loop() {
        switch (Globals.turretState) {
            case FOLLOWING_GOAL:
                followGoal();
                applyTarget();
                break;
            case FOLLOWING_OBELISK:
                followObelisk();
                applyTarget();
                break;
            case RESET:
                resetTurret();
                break;
            case SET_POSITION:
                applyTarget();
                break;
        }
    }
}