package org.firstinspires.ftc.teamcode.common.utility.peacock.control;

public interface Controller {
    double run();
    void reset();
    void updateError(double error);
    void updatePosition(double position);
}
