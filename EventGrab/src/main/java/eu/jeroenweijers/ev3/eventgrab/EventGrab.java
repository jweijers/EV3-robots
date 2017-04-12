package eu.jeroenweijers.ev3.eventgrab;

import eu.jeroenweijers.ev3.eventgrab.sensors.InfraredSensor;
import eu.jeroenweijers.ev3.eventgrab.sensors.TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;

public class EventGrab {

    private RegulatedMotor left;
    private RegulatedMotor right;

    private TouchSensor touchSensor;

    private InfraredSensor infraredSensor;

    private EventGrab(){
        //left = new EV3LargeRegulatedMotor(MotorPort.A);
        //right = new EV3LargeRegulatedMotor(MotorPort.B);
        //touchSensor = new TouchSensor(SensorPort.S1);
        //touchSensor.start();
        infraredSensor = new InfraredSensor(SensorPort.S1);
    }

    private void init(){
        //resetMotor(left);
        //resetMotor(right);
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

    public static void main(String[] args) {

        LCD.drawString("Starting EventBot", 2, 3);

        EventGrab grabbie = new EventGrab();
        grabbie.init();

        //grabbie.touchSensor.addListener(event -> LCD.drawString("Touched!" + System.currentTimeMillis(), 2, 3));
        Button.waitForAnyPress();
        grabbie.infraredSensor.readData();
        Button.waitForAnyPress();
        grabbie.infraredSensor.readData();
        Button.waitForAnyPress();
        grabbie.infraredSensor.readData();
        Button.waitForAnyPress();
        grabbie.infraredSensor.readData();


        LCD.drawString("Kill EventBot - click to continue", 2, 3);
        Button.waitForAnyPress();
        //grabbie.touchSensor.stopSensor();

    }



    private static void grab(EV3MediumRegulatedMotor ev3MediumRegulatedMotor){
        ev3MediumRegulatedMotor.setStallThreshold(2, 10);
        ev3MediumRegulatedMotor.setSpeed(1000);
        ev3MediumRegulatedMotor.rotate(-500, false);
        ev3MediumRegulatedMotor.waitComplete();
    }
}
