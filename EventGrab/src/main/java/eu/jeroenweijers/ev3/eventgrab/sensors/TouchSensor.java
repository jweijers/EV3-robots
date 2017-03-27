package eu.jeroenweijers.ev3.eventgrab.sensors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

import java.util.ArrayList;
import java.util.List;


public class TouchSensor extends Thread {

    private final List<SensorEventListener<Boolean>> listeners = new ArrayList<>();

    private boolean lastState = false;

    private float[] touchSample;

    private final EV3TouchSensor sensor;

    private boolean started;

    public TouchSensor(Port port) {
        sensor = createSensor(port);
        touchSample = new float[sensor.sampleSize()];
    }

    private EV3TouchSensor createSensor(Port port) {
        return new EV3TouchSensor(port);
    }

    public synchronized void addListener(SensorEventListener<Boolean> listener){
        listeners.add(listener);
    }

    public synchronized void removeListener(SensorEventListener<Boolean> listener){
        listeners.remove(listener);
    }

    public synchronized void stopSensor(){
        started = false;
    }

    @Override
    public void run(){
        started = true;
        while(started){
            sensor.fetchSample(touchSample, 0);
            boolean pressed = touchSample[0] == 1;
            if (pressed != lastState){
                throwNewEvent(pressed);
                lastState = pressed;
            }
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void throwNewEvent(boolean b){
        System.out.println("Sending button pressed event");
        for (SensorEventListener<Boolean> listener : listeners) {
            listener.handle(new SensorEvent<>(b));
        }
    }

}