package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class NonIntrusiveProfiler implements Profiler {

    private boolean profiling;

    private IntrusiveProfiler.DataAnalysisEndedListener dataAnalysisEndedListener = null;

    private List<MobileSensor> sensors = new ArrayList<>();

    private LightSensor lightSensor;
    private Microphone microphone;
    private Camera camera;

    private Timer timer;


    private List<String> recommendations = new ArrayList<>();


    public NonIntrusiveProfiler(Context context) {
        lightSensor = new LightSensor(context);
        microphone = new Microphone(context);
        camera = new Camera();
        sensors.add(lightSensor);
        sensors.add(microphone);
        sensors.add(camera);

        timer = new Timer();
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

            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public boolean isProfiling() {
        return profiling;
    }

    @Override
    public void analyzeData() {




        // TODO: uncomment below when analysis finished
        // fire event
//        if (dataAnalysisEndedListener != null) {
//            dataAnalysisEndedListener.onDataAnalysisEnd();
//        }

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

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setDataAnalysisEndedListener(IntrusiveProfiler.DataAnalysisEndedListener dataAnalysisEndedListener) {
        this.dataAnalysisEndedListener = dataAnalysisEndedListener;
    }

    public static interface DataAnalysisEndedListener {
        public void onDataAnalysisEnd();
    }
}
