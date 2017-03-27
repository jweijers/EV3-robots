package eu.jeroenweijers.ev3.eventgrab.sensors;

@FunctionalInterface
public interface SensorEventListener<E> {

    void handle(SensorEvent<E> event);
}
