import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Hello {

    public static void main(String[] args) {
        LCD.drawString("Hello world", 2, 3);
        Button.waitForAnyPress();

    }
}
