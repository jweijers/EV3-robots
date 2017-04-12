package eu.jeroenweijers.ev3.canonbot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

public class CanonBot {

    private static final Port CANON_MOTOR_PORT = MotorPort.A;



    public static void main(String[] args) {

        LCD.drawString("Starting CanonBot", 2, 3);
        Canon canon = new Canon(CANON_MOTOR_PORT);

        LCD.drawString("Kill GrabBot - click to continue", 2, 3);
        Button.waitForAnyPress();

    }

    private static class Canon{
        EV3MediumRegulatedMotor canonEngine;

        public Canon(Port port){
            canonEngine = new EV3MediumRegulatedMotor(port);
            resetMotor();
            fire();

        }

        private void resetMotor(){
            canonEngine.resetTachoCount();
            canonEngine.rotateTo(0);
            canonEngine.setSpeed(1050);
            canonEngine.setAcceleration(200);
            //canonEngine.setStallThreshold(1,1);
        }

        private void fire(){
            canonEngine.rotate(720);
            resetMotor();
        }

    }


}
