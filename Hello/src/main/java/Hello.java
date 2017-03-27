import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class Hello {

    public static void main(String[] args) {
        LCD.drawString("Hello world", 2, 3);
        Button.waitForAnyPress();
        EV3MediumRegulatedMotor ev3MediumRegulatedMotor = new EV3MediumRegulatedMotor(MotorPort.C);
        resetSmallEngine(ev3MediumRegulatedMotor);
        grab(ev3MediumRegulatedMotor);
        LCD.drawString("Position: " + ev3MediumRegulatedMotor.getPosition(), 2, 3);
        resetSmallEngine(ev3MediumRegulatedMotor);

        Button.waitForAnyPress();
    }

    private static void resetSmallEngine(EV3MediumRegulatedMotor ev3MediumRegulatedMotor){
        ev3MediumRegulatedMotor.setStallThreshold(2, 10);
        ev3MediumRegulatedMotor.setSpeed(500);
        ev3MediumRegulatedMotor.rotate(500, false);
        ev3MediumRegulatedMotor.waitComplete();
    }

    private static void grab(EV3MediumRegulatedMotor ev3MediumRegulatedMotor){
        ev3MediumRegulatedMotor.setStallThreshold(2, 10);
        ev3MediumRegulatedMotor.setSpeed(1000);
        ev3MediumRegulatedMotor.rotate(-500, false);
        ev3MediumRegulatedMotor.waitComplete();
    }
}
