package nl.ocs.ev3.canonbot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import nl.ocs.ev3.canonbot.canon.Canon;
import nl.ocs.ev3.canonbot.ir.InfraredSensor;
import nl.ocs.ev3.canonbot.server.RobotServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CanonBot {

    private static final Port CANON_MOTOR_PORT = MotorPort.D;

    private static final Port CANON_ROTATION_PORT = MotorPort.C;

    private static final Port INFRARED_SENSOR_PORT = SensorPort.S1;

    private final Canon canon;

    private final InfraredSensor infraredSensor;

    public static void main(String[] args) throws IOException {

        LCD.drawString("Starting CanonBot", 2, 3);

        ApplicationContext context =
                new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");

        RobotServer server = context.getBean(RobotServer.class);

        LCD.clear();
        LCD.drawString(server.toString(), 2, 3);

        LCD.drawString("Kill GrabBot - click to continue", 2, 3);

        Button.waitForAnyPress();

    }

    public CanonBot(){
         canon = new Canon(CANON_MOTOR_PORT, CANON_ROTATION_PORT);
         infraredSensor = new InfraredSensor(INFRARED_SENSOR_PORT);

    }

    public Canon getCanon(){
        return canon;
    }

    public InfraredSensor getInfraredSensor(){
        return infraredSensor;
    }

}
