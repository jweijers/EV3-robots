package eu.jeroenweijers.ev3.rxgrab;

import eu.jeroenweijers.ev3.rxgrab.sensors.ForwardBehavior;
import eu.jeroenweijers.ev3.rxgrab.sensors.ObstacleBehavior;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class GrabBot {

    private RegulatedMotor left;
    private RegulatedMotor right;

    private EV3TouchSensor touch;

    private final Arbitrator arbitrator;

    private GrabBot(){
        left = new EV3LargeRegulatedMotor(MotorPort.A);
        right = new EV3LargeRegulatedMotor(MotorPort.B);
        touch = new EV3TouchSensor(SensorPort.S1);

        arbitrator = new Arbitrator(new Behavior[]{forward, hit});
    }

    private void init(){
        resetMotor(left);
        resetMotor(right);
    }

    private void resetMotor(RegulatedMotor motor){
        motor.resetTachoCount();
        motor.rotateTo(0);
        motor.setSpeed(1050);
        motor.setAcceleration(200);
    }

    public RegulatedMotor getLeft() {
        return left;
    }

    public RegulatedMotor getRight() {
        return right;
    }

    public EV3TouchSensor getTouch() {
        return touch;
    }

    public static void main(String[] args) {

        LCD.drawString("Starting GrabBot", 2, 3);

        GrabBot grabbie = new GrabBot();
        grabbie.init();


        LCD.drawString("Kill GrabBot - click to continue", 2, 3);
        Button.waitForAnyPress();

        grabbie.arbitrator.stop();
    }



    private static void grab(EV3MediumRegulatedMotor ev3MediumRegulatedMotor){
        ev3MediumRegulatedMotor.setStallThreshold(2, 10);
        ev3MediumRegulatedMotor.setSpeed(1000);
        ev3MediumRegulatedMotor.rotate(-500, false);
        ev3MediumRegulatedMotor.waitComplete();
    }
}
