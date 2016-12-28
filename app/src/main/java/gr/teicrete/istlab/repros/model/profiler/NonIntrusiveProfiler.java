package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class NonIntrusiveProfiler implements Profiler {

    private boolean profiling;

    private ArrayList<MobileSensor> sensors = new ArrayList<>();

    private LightSensor lightSensor;
    private Microphone microphone;
    private Camera camera;

    public NonIntrusiveProfiler(Context context) {
        lightSensor = new LightSensor(context);
        microphone = new Microphone(context);
        camera = new Camera();
        sensors.add(lightSensor);
        sensors.add(microphone);
        sensors.add(camera);
    }

    @Override
    public void startProfiling() {
        profiling = true;
        for (MobileSensor sensor : sensors) {
            sensor.startSensing();
        }
    }

    @Override
    public void stopProfiling() {
        if (profiling) {
            for (MobileSensor sensor : sensors) {
                sensor.stopSensing();
            }
            profiling = false;
        }
    }

    @Override
    public boolean isProfiling() {
        return profiling;
    }

    @Override
    public void analyzeData() {
    }

    public LightSensor getLightSensor() {
        return lightSensor;
    }

    public Microphone getMicrophone() {
        return microphone;
    }

    public Camera getCamera() {
        return camera;
    }
}
