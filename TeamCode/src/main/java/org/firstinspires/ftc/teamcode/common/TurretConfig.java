package org.firstinspires.ftc.teamcode.common;

import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;

@Config
@Configurable
public class TurretConfig {
    public static double TURRET_FORWARD = 0.5;

    public static double pivotX = 0;
    public static double pivotY = 0;

    public static double closeOffsetX = 0.0;
    public static double closeOffsetY = 8.0;
    public static double farOffsetX   = 0.0;
    public static double farOffsetY   = 0.0;

    public static double GEAR_RATIO = 1.20146520147;
    public static double MAX_SERVO_DEG = 305;

    public static double MAX_SIDE_ROTATION = 178;
}