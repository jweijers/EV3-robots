package nl.ocs.lejos;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class FindRedBehavior implements Behavior {

    private static final Logger LOG = LoggerFactory.getLogger(FindRedBehavior.class);

    private static final int MAX_SPEED = 100;

    private final EV3ColorSensor colorSensor;
    private final RobotState robotState;
    private final AtomicBoolean suppressed = new AtomicBoolean(true);
    private final SampleProvider colorSampler;
    private final float[] sample;
    private final EV3LargeRegulatedMotor leftMotor;
    private final EV3LargeRegulatedMotor rightMotor;

    public FindRedBehavior(final EV3ColorSensor colorSensor, final EV3LargeRegulatedMotor leftMotor,
            final EV3LargeRegulatedMotor rightMotor, final RobotState robotState) {
        this.colorSensor = colorSensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.robotState = robotState;
        colorSampler = colorSensor.getColorIDMode();
        sample = new float[colorSampler.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        LOG.info("Finding red");
        return continueSearch();
    }

    private boolean continueSearch() {
        colorSampler.fetchSample(sample, 0);
        final boolean takeControl = Color.RED != (int) sample[0] && !robotState.getPause().get();
        LOG.info("Continue search: {}", takeControl);
        return takeControl;
    }

    @Override
    public void action() {
        LOG.info("Starting find red behavior");
        suppressed.set(false);
        int rotation = 60;
        while (!robotState.getPause().get() && takeControl() && !suppressed.get()) {

            leftMotor.setSpeed(MAX_SPEED);
            rightMotor.setSpeed(MAX_SPEED);
            LOG.info("Rotating: {}", rotation);
            leftMotor.rotate(rotation, true);
            rightMotor.rotate(rotation * -1, true);

            while (leftMotor.isMoving() || rightMotor.isMoving()) {
                if (suppressed.get() || !continueSearch()) {
                    leftMotor.stop();
                    rightMotor.stop();
                    LOG.info("Found red or robot paused");
                    return;
                } else {
                    Thread.yield();
                }
            }
            rotation *= -1.5;
        }
        leftMotor.stop();
        rightMotor.stop();
        LOG.info("Stopping find red behavior");
    }

    @Override
    public void suppress() {
        suppressed.set(true);
    }
}
