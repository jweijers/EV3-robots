package nl.ocs.ev3.canonbot.ir;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;


public class InfraredSensor {

    private float[] seekSampleData;

    private final EV3IRSensor sensor;

    public InfraredSensor(Port port) {
        sensor = createSensor(port);
        seekSampleData = new float[sensor.sampleSize()];
    }

    private EV3IRSensor createSensor(Port port) {
        return new EV3IRSensor(port);
    }


    public void readData(){
        SensorMode seekMode = sensor.getSeekMode();
        seekSampleData = new float[seekMode.sampleSize()];
        seekMode.fetchSample(seekSampleData, 0);
        Beacon[] beacons = toBeacon(seekSampleData);
        for (Beacon beacon : beacons) {
            System.out.println(beacon);
        }
    }

    private Beacon[] toBeacon(float[] sampleData){
        Beacon[] beacons = new Beacon[4];
        for (int i = 0; i < 4; i++){
            int angle = (int)sampleData[i*2];
            int distance = (int)sampleData[(i*2) + 1];
            beacons[i] = new Beacon(i, angle, distance);
        }
        return beacons;
    }

    public static class Beacon{

        public int angle;

        public int distance;

        public int id;

        public Beacon(int id, int angle, int distance){
            this.id = id;
            this.distance = distance;
            this.angle = angle;
        }

        public int getAngle() {
            return angle;
        }

        public int getDistance() {
            return distance;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString(){
            return "[Beacon: " + id + " angle: " + angle + " distance: " + distance + "]";
        }
    }



}