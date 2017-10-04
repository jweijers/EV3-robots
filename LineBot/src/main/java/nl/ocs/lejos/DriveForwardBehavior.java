package nl.ocs.lejos;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.util.concurrent.atomic.AtomicBoolean;

public class DriveForwardBehavior implements Behavior {

    private final AtomicBoolean suppressed = new AtomicBoolean(false);
    private final EV3LargeRegulatedMotor rightMotor;

    private final EV3LargeRegulatedMotor leftMotor;
    private final RobotState robotState;

    public DriveForwardBehavior(final RobotState robotState, final EV3LargeRegulatedMotor leftMotor,
            final EV3LargeRegulatedMotor rightMotor) {
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
        this.robotState = robotState;
    }

    private boolean driveOn() {
        return !robotState.getPause().get() && !suppressed.get();
    }

    @Override
    public boolean takeControl() {
        return !(robotState.getPause().get());
    }

    @Override
    public void action() {
        suppressed.set(false);
        leftMotor.startSynchronization();
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
        leftMotor.backward();
        rightMotor.backward();
        while (driveOn()) {
            Thread.yield();
        }
        leftMotor.stop();
        rightMotor.stop();
        rightMotor.startSynchronization();
    }

    @Override
    public void suppress() {
        suppressed.set(true);
    }
}
