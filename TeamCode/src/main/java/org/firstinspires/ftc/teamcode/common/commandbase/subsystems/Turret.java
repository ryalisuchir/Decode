package org.firstinspires.ftc.teamcode.common.commandbase.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.utility.functions.vision.Vision;
import org.firstinspires.ftc.teamcode.common.utility.turret.CloseBlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.CloseRedTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.turret.FarBlueTurretLUT;
import org.firstinspires.ftc.teamcode.common.utility.Globals;
import org.firstinspires.ftc.teamcode.common.utility.turret.FarRedTurretLUT;

import java.util.List;

public class Turret {
    private final ServoImplEx turret1, turret2;
    private final CloseBlueTurretLUT closeBlueTurretLUT = new CloseBlueTurretLUT();
    private final FarBlueTurretLUT farBlueTurretLUT = new FarBlueTurretLUT();

    private final CloseRedTurretLUT closeRedTurretLUT = new CloseRedTurretLUT();
    private final FarRedTurretLUT farRedTurretLUT = new FarRedTurretLUT();

    public double visionOffset = 0.0;
    private boolean visionLocked = false;

    private final Follower follower;
    private final Globals.Side side;
    public double goalX, goalY;
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
        return new InstantCommand(() ->setPositionOnce(Globals.TURRET_RESET));
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
        Globals.turretState = Globals.TurretState.BLUE_CLOSE_OBELISK;
        return new InstantCommand(() -> setFixedPosition(Globals.TURRET_BLUE_CLOSE_READ));
    }

    public InstantCommand redObeliskRead() {
        Globals.turretState = Globals.TurretState.RED_CLOSE_OBELISK;
        return new InstantCommand(() -> setFixedPosition(Globals.TURRET_RED_CLOSE_READ));
    }

    public void setFixedPosition(double pos) {
        setPositionOnce(clamp(pos, Globals.MIN_TURRET, Globals.MAX_TURRET));
    }

    public void followGoal() {
        double turretAngle = getTurretAngleToGoal(
                follower.getPose().getX(),
                follower.getPose().getY(),
                follower.getPose().getHeading()
        );

        boolean isFar = follower.getPose().getY() < 50;

        boolean isClose = follower.getPose().getY() > 132;

        double servoPosition; if (side == Globals.Side.BLUE) {
            servoPosition = isFar ? farBlueTurretLUT.getServoValue(turretAngle) : closeBlueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = isFar ? farRedTurretLUT.getServoValue(turretAngle) : closeRedTurretLUT.getServoValue(turretAngle);
        }

        servoPosition += visionOffset;

        if (side == Globals.Side.RED && isClose) {
            if (visionOffset!=0) servoPosition -= Globals.CLOSE_TURRET_OFFSET;
        }

        if (side == Globals.Side.BLUE && isClose) {
            if (visionOffset!=0) servoPosition -= Globals.CLOSE_TURRET_OFFSET;
        }

        if (side == Globals.Side.RED && isFar) {
            if (visionOffset !=0) servoPosition -=Globals.FAR_TURRET_OFFSET;
        }

        if (side == Globals.Side.BLUE && isFar) {
            if (visionOffset !=0) servoPosition +=Globals.FAR_TURRET_OFFSET;
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

        boolean isFar = follower.getPose().getY() < 50;

        double servoPosition;
        if (side == Globals.Side.BLUE) {
            servoPosition = isFar ? farBlueTurretLUT.getServoValue(turretAngle) : closeBlueTurretLUT.getServoValue(turretAngle);
        } else {
            servoPosition = isFar ? farRedTurretLUT.getServoValue(turretAngle) : closeRedTurretLUT.getServoValue(turretAngle);
        }

        setFixedPosition(servoPosition);
    }

    public void loop() {
        if (Globals.turretState == Globals.TurretState.FOLLOWING) {
            followGoal();
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
        double cos = Math.cos(robotHeadingRadians);
        double sin = Math.sin(robotHeadingRadians);

        double turretWorldX =
                robotX + Globals.TURRET_OFFSET_X * cos - Globals.TURRET_OFFSET_Y * sin;
        double turretWorldY =
                robotY + Globals.TURRET_OFFSET_X * sin + Globals.TURRET_OFFSET_Y * cos;

        double dx = goalX - turretWorldX;
        double dy = goalY - turretWorldY;
        double angleToGoal = Math.atan2(dy, dx);

        double barrelWorldX =
                turretWorldX + Globals.BARREL_LENGTH * Math.cos(angleToGoal);
        double barrelWorldY =
                turretWorldY + Globals.BARREL_LENGTH * Math.sin(angleToGoal);

        double bdx = goalX - barrelWorldX;
        double bdy = goalY - barrelWorldY;
        double correctedAngle = Math.atan2(bdy, bdx);

        double turretAngle = correctedAngle - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }

    public double getTurretAngleToObelisk(
            double robotX,
            double robotY,
            double robotHeadingRadians
    ) {
        double cos = Math.cos(robotHeadingRadians);
        double sin = Math.sin(robotHeadingRadians);

        double turretWorldX =
                robotX + Globals.TURRET_OFFSET_X * cos - Globals.TURRET_OFFSET_Y * sin;
        double turretWorldY =
                robotY + Globals.TURRET_OFFSET_X * sin + Globals.TURRET_OFFSET_Y * cos;

        double dx = 72 - turretWorldX;
        double dy = 144 - turretWorldY;
        double angleToGoal = Math.atan2(dy, dx);

        double barrelWorldX =
                turretWorldX + Globals.BARREL_LENGTH * Math.cos(angleToGoal);
        double barrelWorldY =
                turretWorldY + Globals.BARREL_LENGTH * Math.sin(angleToGoal);

        double bdx = 72 - barrelWorldX;
        double bdy = 144 - barrelWorldY;
        double correctedAngle = Math.atan2(bdy, bdx);

        double turretAngle = correctedAngle - robotHeadingRadians;

        return Math.atan2(Math.sin(turretAngle), Math.cos(turretAngle));
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}