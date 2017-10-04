package nl.ocs.lejos;

import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

import java.util.concurrent.atomic.AtomicBoolean;

public class BeepOnRedBehavior implements Behavior {

    private final EV3ColorSensor colorSensor;

    private final RobotState robotState;

    private final AtomicBoolean suppressed = new AtomicBoolean(true);

    private final SampleProvider colorSampler;

    private final float[] sample;

    public BeepOnRedBehavior(final EV3ColorSensor colorSensor, final RobotState robotState) {
        this.colorSensor = colorSensor;
        this.robotState = robotState;

        colorSampler = colorSensor.getColorIDMode();
        sample = new float[colorSampler.sampleSize()];
    }

    @Override
    public boolean takeControl() {
        colorSampler.fetchSample(sample, 0);
        return Color.RED == (int) sample[0] && !robotState.getPause().get();
    }

    @Override
    public void action() {
        suppressed.set(false);
        Sound.twoBeeps();
        Sound.twoBeeps();
    }

    @Override
    public void suppress() {
        suppressed.set(true);
    }
}
