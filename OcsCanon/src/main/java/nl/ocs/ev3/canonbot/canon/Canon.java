package nl.ocs.ev3.canonbot.canon;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;

public class Canon {

    private final EV3MediumRegulatedMotor canonEngine;

    private final EV3LargeRegulatedMotor rotationEngine;

    public Canon(Port canonEnginePort, Port rotationEnginePort) {
        canonEngine = new EV3MediumRegulatedMotor(canonEnginePort);
        rotationEngine = new EV3LargeRegulatedMotor(rotationEnginePort);
        resetEngine(canonEngine);
        resetEngine(rotationEngine);

    }

    private void resetEngine(BaseRegulatedMotor engine) {
        engine.resetTachoCount();
        engine.rotateTo(0);
        engine.setSpeed(1050);
        engine.setAcceleration(200);
        //canonEngine.setStallThreshold(1,1);
    }


    public void fire() {
        canonEngine.rotate(720);
        resetEngine(canonEngine);
    }

    public void rotateCanon(int angle){
        rotationEngine.rotate(angle);
        resetEngine(rotationEngine);
    }
}
