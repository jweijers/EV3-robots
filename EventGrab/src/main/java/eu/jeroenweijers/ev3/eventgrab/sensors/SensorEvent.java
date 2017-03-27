package eu.jeroenweijers.ev3.eventgrab.sensors;

public class SensorEvent<E> {

    private final E data;

    public SensorEvent(E e){
        data = e;
    }

    public E getData(){
        return data;
    }

}
