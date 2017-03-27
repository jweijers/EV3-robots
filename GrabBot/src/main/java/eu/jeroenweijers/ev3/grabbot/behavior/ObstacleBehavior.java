package eu.jeroenweijers.ev3.grabbot.behavior;

import eu.jeroenweijers.ev3.grabbot.GrabBot;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

public class ObstacleBehavior implements Behavior {

    private boolean suppressed = false;

    private final GrabBot grabBot;

    float[] touchSample;

    public ObstacleBehavior(GrabBot grabBot) {
        this.grabBot = grabBot;
        touchSample = new float[grabBot.getTouch().sampleSize()];
    }

    @Override
    public boolean takeControl() {
        grabBot.getTouch().fetchSample(touchSample, 0);
        return touchSample[0] == 1;
    }

    @Override
    public void action() {
        suppressed = false;
        RegulatedMotor leftMotor = grabBot.getLeft();
        RegulatedMotor rightMotor = grabBot.getRight();

        leftMotor.rotate(-180, true);
        rightMotor.rotate(-360, true);

        while( (leftMotor.isMoving() || rightMotor.isMoving()) && !suppressed )
            Thread.yield();

        leftMotor.stop(); // clean up
        rightMotor.stop();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
}
