package nl.ocs.lejos;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * Hello Lego!
 */
public class App {

    //Engines for driving
    private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

    //Engine for arms
    private static final EV3MediumRegulatedMotor armsMotor = new EV3MediumRegulatedMotor(MotorPort.A);

    //Sensors
    private static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S3);
    private static final EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S4);

    public static void main(final String[] args) {
        setupShutdownHooks();

        clearLCD();

        LCD.drawString("Hello Lego!", 10, 10);
        Sound.twoBeeps();
        LCD.refresh();
        System.out.println("Hello Lego!");
        Button.waitForAnyPress();
        Sound.beep();
        System.out.println("Setup motors.");
        final EV3LargeRegulatedMotor[] motors = { leftMotor, rightMotor };
        leftMotor.setSpeed(-200);
        rightMotor.setSpeed(-200);
        leftMotor.synchronizeWith(new EV3LargeRegulatedMotor[] { rightMotor });
        rightMotor.synchronizeWith(new EV3LargeRegulatedMotor[] { leftMotor });
        final RobotState robotState = new RobotState();
        final Behavior[] behaviors = { new DriveForwardBehavior(robotState, leftMotor, rightMotor),
                new BeepOnRedBehavior(colorSensor, robotState),
                new BackOffBehavior(irSensor, leftMotor, rightMotor, robotState) };
        final Arbitrator arbitrator = new Arbitrator(behaviors);
        System.out.println("Launching kill thread");
        final Thread killThread = new Thread(() -> {
            Button.ESCAPE.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(final Key key) {
                    System.out.println("Stopping program");
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
                    System.out.println("Setting robot pause = " + !pause);
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
        System.out.println("Launching behaviors");
        arbitrator.go();
        System.out.println("Arbitrator quit. No behaviours requiring action...");
        clearLCD();
        LCD.refresh();
        System.out.println("Shutting down.");

    }

    public static void setupShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("ShutdownHook - Stopping motors.");
            armsMotor.stop();
            leftMotor.stop();
            rightMotor.stop();
            System.out.println("ShutdownHook - Motors stopped.");
        }));
    }

    public static void clearLCD() {
        LCD.clear();
    }
}
