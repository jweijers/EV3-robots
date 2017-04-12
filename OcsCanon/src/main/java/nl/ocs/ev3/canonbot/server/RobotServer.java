package nl.ocs.ev3.canonbot.server;

import nl.ocs.ev3.canonbot.CanonBot;
import nl.ocs.java.ev3.ResetNeutralResult;
import nl.ocs.java.ev3.RobotInterface;
import nl.ocs.java.ev3.SeekBeaconResult;
import nl.ocs.java.ev3.Turn;
import nl.ocs.java.ev3.TurnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotServer implements RobotInterface {

    @Autowired
    private CanonBot canonBot;

    @Override
    public SeekBeaconResult seek() {
        return null;
    }

    @Override
    public TurnResult turn(Turn turn) {
        return null;
    }

    @Override
    public ResetNeutralResult reset() {
        return null;
    }

    @Override
    public void fire() {

    }
}
