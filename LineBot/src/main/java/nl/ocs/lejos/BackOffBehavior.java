package nl.ocs.lejos;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackOffBehavior implements Behavior {

    private static final Logger LOG = LoggerFactory.getLogger(BackOffBehavior.class);

    private final static int MINIMAL_DISTANCE = 40;

    private final EV3IRSensor irSensor;

    private final AtomicBoolean suppressed = new AtomicBoolean(true);
    private final SampleProvider distanceSampler;
    private final float[] sample;
    private final EV3LargeRegulatedMotor leftMotor;
    private final EV3LargeRegulatedMotor rightMotor;
    private final AtomicBoolean paused;

    public BackOffBehavior(final EV3IRSensor irSensor, final EV3LargeRegulatedMotor leftMotor,
            final EV3LargeRegulatedMotor rightMotor, final AtomicBoolean paused) {
        this.irSensor = irSensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;

        distanceSampler = irSensor.getDistanceMode();
        this.paused = paused;
        sample = new float[distanceSampler.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        LOG.debug("Checking distance");
        distanceSampler.fetchSample(sample, 0);
        final int distance = (int) sample[0];
        LOG.debug("Distance is: {}", distance);
        return distance < MINIMAL_DISTANCE && !paused.get();
    }

    @Override
    public void action() {
        LOG.debug("Starting backing off");
        suppressed.set(false);
        final int speed = 200;
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
        leftMotor.forward();
        rightMotor.forward();
        while (!suppressed.get() && takeControl()) {
            Thread.yield();
        }
        leftMotor.stop();
        rightMotor.stop();
        suppressed.set(true);

        LOG.debug("Done backing off");
    }

    @Override
    public void suppress() {
        LOG.debug("Suppressing");
        suppressed.set(true);
    }
}
