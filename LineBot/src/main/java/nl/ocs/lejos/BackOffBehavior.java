package nl.ocs.lejos;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

import java.util.concurrent.atomic.AtomicBoolean;

public class BackOffBehavior implements Behavior {


    private final static int MINIMAL_DISTANCE = 40;

    private final EV3IRSensor irSensor;
    private final RobotState robotState;
    private final AtomicBoolean suppressed = new AtomicBoolean(true);
    private final SampleProvider distanceSampler;
    private final float[] sample;
    private final EV3LargeRegulatedMotor leftMotor;
    private final EV3LargeRegulatedMotor rightMotor;

    public BackOffBehavior(final EV3IRSensor irSensor, final EV3LargeRegulatedMotor leftMotor,
            final EV3LargeRegulatedMotor rightMotor, final RobotState robotState) {
        this.irSensor = irSensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.robotState = robotState;
        distanceSampler = irSensor.getDistanceMode();
        sample = new float[distanceSampler.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        System.out.println("Checking distance");
        distanceSampler.fetchSample(sample, 0);
        final int distance = (int) sample[0];
        System.out.println("Distance is: " + distance);
        return distance < MINIMAL_DISTANCE && !robotState.getPause().get();
    }

    @Override
    public void action() {
        System.out.println("Starting backing off");
        suppressed.set(false);
        final int speed = 200;
        leftMotor.startSynchronization();
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
        leftMotor.forward();
        //rightMotor.forward();
        while (!suppressed.get() && takeControl()) {
            Thread.yield();
        }
        leftMotor.stop();
        //rightMotor.stop();
        leftMotor.endSynchronization();
        suppressed.set(true);

        System.out.println("Done backing off");
    }

    @Override
    public void suppress() {
        System.out.println("Suppressing");
        suppressed.set(true);
    }
}
