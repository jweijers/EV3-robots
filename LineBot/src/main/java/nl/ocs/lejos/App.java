package nl.ocs.lejos;

import ev3dev.actuators.LCD;
import ev3dev.actuators.Sound;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.hardware.EV3DevDevice;
import ev3dev.sensors.Button;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3IRSensor;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;

/**
 * Hello Lego!
 */
public class App extends EV3DevDevice {

    //EV3 display -- make sure to kill brickman!
    private static final GraphicsLCD lcd = LCD.getInstance();
    //EV3 speaker
    private static final Sound sound = Sound.getInstance();
    //Engines for driving
    private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

    //Engine for arms
    private static final EV3MediumRegulatedMotor armsMotor = new EV3MediumRegulatedMotor(MotorPort.A);

    //Sensors
    private static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
    private static final EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S4);

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        setupShutdownHooks();
        final GraphicsLCD lcd = LCD.getInstance();
        clearLCD();
        lcd.setFont(new Font(MONOSPACED, PLAIN, 10));
        lcd.setColor(Color.BLACK);
        lcd.drawString("Hello Lego!", 10, 10, 0);
        sound.twoBeeps();
        lcd.refresh();
        LOG.info("Hello Lego!");
        Button.waitForAnyPress();
        sound.beep();
        LOG.info("Setup motors.");
        leftMotor.setSpeed(-200);
        rightMotor.setSpeed(-200);
        final RobotState robotState = new RobotState();
        final Behavior[] behaviors = { new DriveForwardBehavior(robotState, leftMotor, rightMotor),
                new BeepOnRedBehavior(colorSensor, sound, robotState),
                new BackOffBehavior(irSensor, leftMotor, rightMotor, robotState) };
        final Arbitrator arbitrator = new Arbitrator(behaviors);
        LOG.info("Launching kill thread");
        final Thread killThread = new Thread(() -> {
            Button.ESCAPE.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(final Key key) {
                    LOG.info("Stopping program");
                    System.exit(1);
                }

                @Override
                public void keyReleased(final Key key) {

                }
            });
            Button.ENTER.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(final Key key) {
                    final boolean pause = robotState.getPause().get();
                    LOG.info("Setting robot pause = {}", !pause);
                    robotState.getPause().set(!pause);
                    try {
                        Thread.sleep(1000);
                    } catch (final InterruptedException e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void keyReleased(final Key key) {

                }
            });

        });
        killThread.start();
        LOG.info("Launching behaviors");
        arbitrator.go();
        LOG.warn("Arbitrator quit. No behaviours requiring action...");
        clearLCD();
        lcd.refresh();
        LOG.info("Shutting down.");

    }

    public static void setupShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.debug("ShutdownHook - Stopping motors.");
            armsMotor.stop();
            leftMotor.stop();
            rightMotor.stop();
            LOG.debug("ShutdownHook - Motors stopped.");
        }));
    }

    public static void clearLCD() {
        lcd.setColor(Color.WHITE);
        lcd.fillRect(0, 0, lcd.getWidth(), lcd.getHeight());
    }
}
