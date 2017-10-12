package nl.ocs.lejos;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.robotics.subsumption.Behavior;

import java.util.concurrent.atomic.AtomicBoolean;

public class DriveForwardBehavior implements Behavior {

    private final AtomicBoolean suppressed = new AtomicBoolean(false);
    private final EV3LargeRegulatedMotor rightMotor;
    private final EV3LargeRegulatedMotor leftMotor;
    private final AtomicBoolean paused;

    public DriveForwardBehavior(final AtomicBoolean paused, final EV3LargeRegulatedMotor leftMotor,
            final EV3LargeRegulatedMotor rightMotor) {
        this.paused = paused;
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
    }

    private boolean driveOn() {
        return !paused.get() && !suppressed.get();
    }

    @Override
    public boolean takeControl() {
        return !(paused.get());
    }

    @Override
    public void action() {
        suppressed.set(false);
        leftMotor.setSpeed(-200);
        rightMotor.setSpeed(-200);
        leftMotor.forward();
        rightMotor.forward();
        while (driveOn()) {
            Thread.yield();
        }
        leftMotor.stop();
        rightMotor.stop();
    }

    @Override
    public void suppress() {
        suppressed.set(true);
    }
}
