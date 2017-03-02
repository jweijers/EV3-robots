package eu.jeroenweijers.ev3.jetty.rs;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class SimpleRestServiceImpl implements SimpleRestService{

    @Override
    public void display(String text) {
        LCD.drawString("Hello world", 2, 3);
        Button.waitForAnyPress();
    }
}
