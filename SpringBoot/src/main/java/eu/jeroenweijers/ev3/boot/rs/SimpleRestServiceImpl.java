package eu.jeroenweijers.ev3.boot.rs;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Component
public class SimpleRestServiceImpl implements SimpleRestService{

    @Override
    public void display(String text) {
        LCD.drawString("Hello world", 2, 3);
        Button.waitForAnyPress();
    }
}