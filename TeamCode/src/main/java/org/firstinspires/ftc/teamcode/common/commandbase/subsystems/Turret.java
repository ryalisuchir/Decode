package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;


import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.G;
import org.firstinspires.ftc.teamcode.common.utility.functions.TurretMath;
import org.firstinspires.ftc.teamcode.common.utility.functions.luts.TimeOfFlightLUT;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.math.Vector;
import org.firstinspires.ftc.teamcode.common.utility.turret.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.RedTurretLUT;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();
    private final TimeOfFlightLUT timeOfFlightLUT = new TimeOfFlightLUT();
    private double lastPos = -1;

    private final Follower follower;
    private final G.Side side;
    private final TurretMath.CornerGoal cornerGoal;
    public static double virtualGoalX, virtualGoalY;
    public static double baseGoalX, baseGoalY;
    public double originalGoalX, originalGoalY;

    public Turret(
            G.Side side,
            ServoImplEx turret1,
            ServoImplEx turret2,
            Follower follower
    ) {
        this.side = side;
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.cornerGoal = (side == G.Side.BLUE)
                ? TurretMath.CornerGoal.LEFT_BLUE
                : TurretMath.CornerGoal.RIGHT_RED;

        double[] g = TurretMath.getCornerGoalCenter(cornerGoal);
        this.baseGoalX = g[0];
        this.baseGoalY = g[1];
        this.virtualGoalX = g[0];
        this.virtualGoalY = g[1];
        this.originalGoalX = g[0];
        this.originalGoalY = g[1];
    }

    public void setPositionOnce(double pos) {
        if (Math.abs(pos - lastPos) > 0.001) {
            turret1.setPosition(pos);
            turret2.setPosition(pos);
            lastPos = pos;
        }
    }

    public InstantCommand clearCustom() {
        return new InstantCommand(() -> G.turretState = G.TurretState.FOLLOWING);
    }

    public InstantCommand customRedFar() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.595);
        });
    }

    public void tiltPosition() {
        G.turretState = G.TurretState.SET_POSITION;
        setPositionOnce(1);
    }

    public InstantCommand customblueFar() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.175);
        });
    }


    public InstantCommand customRedOriginal() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.75);
        });
    }

    public InstantCommand customRedClose() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.570);
        });
    }

    public InstantCommand customRedAudClose() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.333);
        });
    }

    public InstantCommand customRedCloser() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.37);
        });
    }


    public InstantCommand customBlueOriginal() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.213);
        });
    }

    public InstantCommand customBlueClose() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.213);
        });
    }

    public InstantCommand customBlueCloser() {
        G.turretState = G.TurretState.SET_POSITION;
        return new InstantCommand(() -> {
            setPositionOnce(0.12);
        });
    }


    public InstantCommand reset() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RESET));
    }

    public InstantCommand initClose() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RESET));
    }

    public InstantCommand initFarBlue() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_BLUE_FAR_READ));
    }

    public InstantCommand initFarRed() {
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RED_FAR_READ));
    }

    public InstantCommand blueObeliskRead() {
        G.turretState = G.TurretState.BLUE_CLOSE_OBELISK;
        return new InstantCommand(() -> setPositionOnce(G.TURRET_BLUE_CLOSE_READ));
    }

    public InstantCommand redObeliskRead() {
        G.turretState = G.TurretState.RED_CLOSE_OBELISK;
        return new InstantCommand(() -> setPositionOnce(G.TURRET_RED_CLOSE_READ));
    }

    public double getAngleToGoal() {
        return TurretMath.getTurretAngleToCornerGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                G.pivotX,
                G.pivotY,
                cornerGoal
        );
    }

    public void followGoal() {
        double turretAngle = TurretMath.getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                G.pivotX,
                G.pivotY,
                virtualGoalX,
                virtualGoalY
        );

        double servoPosition;
        if (side == G.Side.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        //cursed asf:
        if (side == G.Side.BLUE && G.match == G.Match.AUTO && follower.getPose().getY() > 50) {
            servoPosition-=0.008; //0.018
        }

        setPositionOnce(servoPosition);
    }

    public void followObelisk() {
        double turretAngle = TurretMath.getTurretAngleToObelisk(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading(),
                G.pivotX,
                G.pivotY
        );

        double servoPosition;
        if (side == G.Side.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        setPositionOnce(servoPosition);
    }

    public void loop() {
        double robotY = follower.getPose().getY();

        double[] goalCenter = TurretMath.getCornerGoalCenter(cornerGoal, robotY);
        baseGoalX = goalCenter[0];
        baseGoalY = goalCenter[1];
        originalGoalX = baseGoalX;
        originalGoalY = baseGoalY;

        virtualGoalX = baseGoalX;
        virtualGoalY = baseGoalY;

        if (G.TURRET_TOF_COMP_ENABLED) {
            Vector velocity = follower.getVelocity();
            double speed = velocity.getMagnitude();
            if (speed >= G.TURRET_SOTM_MIN_SPEED) {
                double distance = TurretMath.getDistanceToGoalPinpoint(follower, baseGoalX, baseGoalY);
                double tof = timeOfFlightLUT.get(distance) * G.TURRET_TOF_COMP_GAIN;

                virtualGoalX = baseGoalX - velocity.getXComponent() * tof;
                virtualGoalY = baseGoalY - velocity.getYComponent() * tof;
            }
        }

        if (G.turretState == G.TurretState.FOLLOWING) {
            followGoal();
        }

        if (G.turretState == G.TurretState.BLUE_CLOSE_OBELISK || G.turretState == G.TurretState.RED_CLOSE_OBELISK) {
            followObelisk();
        }
    }
}
