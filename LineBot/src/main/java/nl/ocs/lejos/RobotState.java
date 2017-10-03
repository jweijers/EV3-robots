package nl.ocs.lejos;

import java.util.concurrent.atomic.AtomicBoolean;

public class RobotState {

    private final AtomicBoolean pause = new AtomicBoolean(false);

    private int drivingMotorSpeed = -200;

    public AtomicBoolean getPause() {
        return pause;
    }

    public int getDrivingMotorSpeed() {
        return drivingMotorSpeed;
    }

    public void setDrivingMotorSpeed(final int drivingMotorSpeed) {
        this.drivingMotorSpeed = drivingMotorSpeed;
    }
}
