package nl.ocs.lejos;

import ev3dev.actuators.Sound;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

import java.util.concurrent.atomic.AtomicBoolean;

public class BeepOnRedBehavior implements Behavior {

    private final EV3ColorSensor colorSensor;

    private final RobotState robotState;

    private final AtomicBoolean suppressed = new AtomicBoolean(true);
    private final Sound sound;

    private final SampleProvider colorSampler;

    private final float[] sample;

    public BeepOnRedBehavior(final EV3ColorSensor colorSensor, final Sound sound, final RobotState robotState) {
        this.colorSensor = colorSensor;
        this.robotState = robotState;
        this.sound = sound;
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
        sound.twoBeeps();
        sound.twoBeeps();
    }

    @Override
    public void suppress() {
        suppressed.set(true);
    }
}
