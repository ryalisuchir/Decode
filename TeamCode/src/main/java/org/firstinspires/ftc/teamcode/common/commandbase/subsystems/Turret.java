package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;
import org.firstinspires.ftc.teamcode.common.utility.turret.BlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.RedTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.Globals;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final BlueTurretLUT blueTurretLUT = new BlueTurretLUT();
    private final RedTurretLUT redTurretLUT = new RedTurretLUT();

    public double visionOffset = 0.0;
    private boolean visionLocked = false;

    private final Follower follower;
    private final Globals.Side side;
    public double goalX, goalY;

    public Pose customPose = null;

    Limelight3A ll;

    private double lastSetPosition = -999;

    public Turret(
            Globals.Side side,
            ServoImplEx turret1,
            ServoImplEx turret2,
            Follower follower,
            double goalX,
            double goalY
    ) {
        this.side = side;
        this.turret1 = turret1;
        this.turret2 = turret2;
        this.follower = follower;
        this.goalX = goalX;
        this.goalY = goalY;
    }

    private void setPositionOnce(double pos) {
        if (Math.abs(pos - lastSetPosition) > 0.00001) {
            turret1.setPosition(pos);
            turret2.setPosition(pos);
            lastSetPosition = pos;
        }
    }

    public void applyVisionCorrectionOnce() {
        if (visionLocked) return;
        if (!Vision.hasCorrectFiducial()) return;
        if (!robotIsStable()) return;

        double tx = Vision.getTx();
        visionOffset = Vision.txToServoPos(tx);
//        if (Math.abs(tx) < 1.3 && follower.getPose().getY() > 60 && Globals.match != Globals.Match.AUTO) {
//            visionOffset = 0;
//        } else {
//            visionOffset = Vision.txToServoPos(tx);
//        }

        visionLocked = true;
    }

    public void applyVisionCorrection() {
        if (!Vision.hasCorrectFiducial()) return;
        if (!robotIsStable()) return;

        double tx = Vision.getTx();
        visionOffset = Vision.txToServoPos(tx);
        visionLocked = true;
    }

    public void clearVisionCorrection() {
        visionOffset = 0.0;
        visionLocked = false;
    }

    public InstantCommand reset() {
        clearVisionCorrection();
        customPose = null;
        return new InstantCommand(() -> setPositionOnce(Globals.TURRET_RESET));
    }

    public InstantCommand initClose() {
        clearVisionCorrection();
        return new InstantCommand(() ->setPositionOnce(Globals.TURRET_RESET));
    }

    public InstantCommand initFarBlue() {
        clearVisionCorrection();
        return new InstantCommand(() ->setPositionOnce(Globals.TURRET_BLUE_FAR_READ));
    }

    public InstantCommand initFarRed() {
        clearVisionCorrection();
        return new InstantCommand(() ->setPositionOnce(Globals.TURRET_RED_FAR_READ));
    }

    public InstantCommand blueObeliskRead() {
        customPose = null;
        Globals.turretState = Globals.TurretState.BLUE_CLOSE_OBELISK;
        return new InstantCommand(() -> setFixedPosition(Globals.TURRET_BLUE_CLOSE_READ));
    }

    public InstantCommand redObeliskRead() {
        customPose = null;
        Globals.turretState = Globals.TurretState.RED_CLOSE_OBELISK;
        return new InstantCommand(() -> setFixedPosition(Globals.TURRET_RED_CLOSE_READ));
    }

    public void setFixedPosition(double pos) {
        setPositionOnce(clamp(pos, Globals.MIN_TURRET, Globals.MAX_TURRET));
    }

    public void followGoal() {
        double turretAngle;
        if (customPose != null) {
            turretAngle = getTurretAngleToGoal(
                    customPose.getX(),
                    customPose.getY(),
                    customPose.getHeading()
            );
        } else {
            turretAngle = getTurretAngleToGoal(
                    follower.getPose().getX(),
                    follower.getPose().getY(),
                    follower.getPose().getHeading()
            );
        }

        boolean isFar = follower.getPose().getY() < 50;
        boolean isClose = follower.getPose().getY() > 132;

        double servoPosition;
        if (side == Globals.Side.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        servoPosition += visionOffset;

        if (side == Globals.Side.RED && isClose) {
            if (visionOffset!=0) servoPosition += Globals.CLOSE_TURRET_OFFSET;
        }

        if (side == Globals.Side.BLUE && isClose) {
            if (visionOffset!=0) servoPosition += Globals.CLOSE_TURRET_OFFSET;
        }

        if (side == Globals.Side.RED && isFar) {
            if (visionOffset !=0) servoPosition -=Globals.FAR_TURRET_OFFSET;
        }

        if (side == Globals.Side.BLUE && isFar) {
            if (visionOffset !=0) servoPosition -= 0;
        }

        setFixedPosition(servoPosition);
    }

    private boolean robotIsStable() {
        if (Globals.match == Globals.Match.AUTO) {
            return follower.getVelocity().getMagnitude() < Globals.VISION_MAX_VEL_AUTO;
        } else {
            return follower.getVelocity().getMagnitude() < Globals.VISION_MAX_VEL_TELE;
        }
    }



    public void followObelisk() {
        double turretAngle = getTurretAngleToObelisk(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading()
        );

        double servoPosition;
        if (side == Globals.Side.BLUE) {
            servoPosition = blueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = redTurretLUT.getServoValue(turretAngle);
        }

        setFixedPosition(servoPosition);
    }

    public void loop() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {
            followGoal();
        }

        if (Globals.turretState == Globals.TurretState.RED_FAR_GOAL) {
            setFixedPosition(0.595);
        }

        if (Globals.turretState == Globals.TurretState.BLUE_FAR_GOAL) {
            setFixedPosition(0.175);
        }

        if (Globals.turretState == Globals.TurretState.RED_CLOSE_GOAL) {
            setFixedPosition(0.525);
        }

        if (Globals.turretState == Globals.TurretState.RED_CLOSE_DIFF_GOAL) {
            setFixedPosition(0.66);
        }

        if (Globals.turretState == Globals.TurretState.BLUE_CLOSE_GOAL) {
            setFixedPosition(0.213);
        }

        if (Globals.turretState == Globals.TurretState.BLUE_CLOSE_DIFF_GOAL) {
            setFixedPosition(0.12);
        }

        if (Globals.turretState == Globals.TurretState.RED_FAR_GOAL_TELE) {
            setFixedPosition(0.56);
        }

        if (Globals.turretState == Globals.TurretState.BLUE_CLOSE_OBELISK || Globals.turretState == Globals.TurretState.RED_CLOSE_OBELISK) {
            followObelisk();
        }

        if (visionLocked && ((follower.getVelocity().getMagnitude() > Globals.VISION_MAX_VEL_TELE && Globals.match == Globals.Match.TELEOP) || follower.getVelocity().getMagnitude() > Globals.VISION_MAX_VEL_AUTO && Globals.match == Globals.Match.AUTO)) {
            clearVisionCorrection();
        }
    }

    public double getTurretAngleToGoal(
            double robotX,
            double robotY,
            double robotHeadingRadians
    ) {
        double dx = goalX - robotX;
        double dy = goalY - robotY;
        double angleToGoal = Math.atan2(dy, dx);

        double turretAngle = angleToGoal - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }

    public double getTurretAngleToObelisk(
            double robotX,
            double robotY,
            double robotHeadingRadians
    ) {

        double dx = 72 - robotX;
        double dy = 144 - robotY;
        double angleToGoal = Math.atan2(dy, dx);

        double turretAngle = angleToGoal - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}