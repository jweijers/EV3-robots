package eu.jeroenweijers.ev3.boot.rs;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import org.springframework.stereotype.Component;

@Component
public class SimpleRestServiceImpl implements SimpleRestService{

    @Override
    public void display(String text) {
        LCD.drawString(text, 2, 3);
        Button.waitForAnyPress();
    }
}
