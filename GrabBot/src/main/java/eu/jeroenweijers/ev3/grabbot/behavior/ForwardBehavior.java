package eu.jeroenweijers.ev3.rxgrab.behavior;

import eu.jeroenweijers.ev3.grabbot.GrabBot;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;

public class ForwardBehavior implements Behavior {

    private boolean suppressed = false;

    private final GrabBot grabBot;

    public ForwardBehavior(GrabBot grabBot) {
        this.grabBot = grabBot;
    }

    @Override
    public boolean takeControl() {
        //We always roll, powerrrrrr!
        return true;
    }

    @Override
    public void action() {
        suppressed = false;

        RegulatedMotor leftMotor = grabBot.getLeft();
        RegulatedMotor rightMotor = grabBot.getRight();
        leftMotor.synchronizeWith(new RegulatedMotor[]{rightMotor});
        leftMotor.forward();
        rightMotor.forward();
        while( !suppressed ) {
            Thread.yield();
        }
        leftMotor.stop(); // clean up
        rightMotor.stop();
    }

    @Override
    public void suppress() {
        suppressed = true;
    }
}
