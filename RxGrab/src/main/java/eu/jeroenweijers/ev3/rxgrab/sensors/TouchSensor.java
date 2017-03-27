package eu.jeroenweijers.ev3.rxgrab.sensors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

import static io.reactivex.schedulers.Schedulers.newThread;


public class TouchSensor {

    private final Observable<Boolean> touched;

    TouchSensor(Port port) {
        touched = Observable.using(
                () -> createSensor(port),
                sensor -> new Sampler(() -> sensor.getTouchMode()).sample(),
                sensor -> closeSensor(sensor)
        )
                .share()
                .map(sample -> sample.values[sample.offset])
                .distinctUntilChanged()
                .map(value -> value == 1);
    }

    private EV3TouchSensor createSensor(Port port) {
        System.out.println("Create LeJOS sensor");
        return new EV3TouchSensor(port);
    }

    private void closeSensor(EV3TouchSensor sensor) {
        System.out.println("Close LeJOS sensor");
        sensor.close();
    }

    public Observable<Boolean> touched() {
        return touched;
    }

    private class Sampler {

        private final Observable<Sample> sample;

        Sampler(SensorModeProvider sensorModeProvider) {
            this.sample = createSampleObservable(sensorModeProvider)
                    .subscribeOn(newThread())
                    .share();
        }

        private Observable<Sample> createSampleObservable(SensorModeProvider sensorModeProvider) {
            return Observable.create(subscriber -> {
                try {
                    sample(sensorModeProvider.getSensorMode(), subscriber);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
        }

        private void sample(SensorMode sensorMode, ObservableEmitter<Sample> observableEmitter) throws InterruptedException {
            System.out.println("Start sampling " + sensorMode.getName() + ", sample size: " + sensorMode.sampleSize());
            observableEmitter.onNext(getSample(sensorMode));
        }

        private Sample getSample(SensorMode sensorMode){
            Sample sample = new Sample(new float[sensorMode.sampleSize()], 0);
            sensorMode.fetchSample(sample.values, sample.offset);
            return sample;
        }

        Observable<Sample> sample() {
            return sample;
        }

        private class Sample {

            final float[] values;
            final int offset;

            Sample(float[] values, int offset) {
                this.values = values;
                this.offset = offset;
            }
        }
    }

    @FunctionalInterface
    interface SensorModeProvider {

        SensorMode getSensorMode();

    }
}