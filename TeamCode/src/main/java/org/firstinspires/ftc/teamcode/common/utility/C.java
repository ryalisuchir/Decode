package org.firstinspires.ftc.teamcode.common.utility;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.utility.peacock.control.FilteredPIDFCoefficients;
import org.firstinspires.ftc.teamcode.common.utility.peacock.control.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.Follower;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.FollowerBuilder;
import org.firstinspires.ftc.teamcode.common.utility.peacock.follower.FollowerConstants;
import org.firstinspires.ftc.teamcode.common.utility.peacock.localization.MecanumConstants;
import org.firstinspires.ftc.teamcode.common.utility.peacock.localization.PinpointConstants;
import org.firstinspires.ftc.teamcode.common.utility.peacock.paths.PathConstraints;
import org.firstinspires.ftc.teamcode.common.utility.peacock.util.PeacockBrakingCoefficients;

public class C {
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 50, 0.83, 1);
    public static FollowerConstants followerConstants = new FollowerConstants()
            .forwardZeroPowerAcceleration(-48.208544)
            .lateralZeroPowerAcceleration(-88.1314)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.16, 0, 0, 0.013))
            .headingPIDFCoefficients(new PIDFCoefficients(1, 0, 0, 0.02))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.04, 0, 0, 0.6, 0.05))
            .centripetalScaling(0.0005)
            .predictiveBrakingCoefficients(new PeacockBrakingCoefficients(0.3, 0.0533697, 0.0021535))
            .mass(12.03);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0.846)
            .strafePodX(-6.25)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightFront")
            .rightRearMotorName("rightRear")
            .leftRearMotorName("leftRear")
            .leftFrontMotorName("leftFront")
            .xVelocity(75.78)
            .yVelocity(57.263)
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
