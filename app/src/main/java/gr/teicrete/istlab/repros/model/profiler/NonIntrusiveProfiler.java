package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gr.teicrete.istlab.repros.model.db.DBHandler;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class NonIntrusiveProfiler implements Profiler {
    public final static int DB_PUSHING_DELAY = 5 * 1000;

    private boolean profiling;

    private Profiler.DataAnalysisEndedListener dataAnalysisEndedListener = null;

    private List<MobileSensor> sensors = new ArrayList<>();

    private LightSensor lightSensor;
    private Microphone microphone;
    private Camera camera;

    private DBHandler dbHandler;
    private List<String> recommendations = new ArrayList<>();

    private Timer timer;

    public NonIntrusiveProfiler(Context context, String uniqueId) {
        lightSensor = new LightSensor(context);
        microphone = new Microphone(context);
        camera = new Camera();
        sensors.add(lightSensor);
        sensors.add(microphone);
        sensors.add(camera);

//        String uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        dbHandler = new DBHandler(uniqueId, false);

        timer = new Timer();
    }

    @Override
    public void startProfiling() {
        profiling = true;
        dbHandler.prepareForProfiling();

        for (MobileSensor sensor : sensors) {
            sensor.startSensing();
        }

        startPushingReadingsToDB();
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

        dbHandler.getlastReadingKeyRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String key = (String) dataSnapshot.getValue();

                dbHandler.getReadingSnapshotsRef(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            /*
                            for (DataSnapshot readingDataSnapshot : dataSnapshot.getChildren()) {

                                NonIntrusiveReadingSnapshot readingsSnapshot = readingDataSnapshot.getValue(NonIntrusiveReadingSnapshot.class);

                                boolean motionDetected = readingsSnapshot.isMotionDetected();
                                double lightLevel = readingsSnapshot.getLightLevel();
                                double audioLevel = readingsSnapshot.getAudioLevel();

                                // TODO: some algorithm runs here in order to generate recommendations
                            }
                            */

                                List<String> recommendations = Arrays.asList(new String[]{RecommendationsSet.NON_INTRUSIVE_NO_MOTION_LIGHT, RecommendationsSet.NON_INTRUSIVE_NO_MOTION_AUDIO});

                                dbHandler.pushRecommendations(recommendations);

                                // fire event
                                if (dataAnalysisEndedListener != null) {
                                    dataAnalysisEndedListener.onDataAnalysisEnd();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void startPushingReadingsToDB() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dbHandler.pushNewNonIntrusiveReadingSnapshot(takeSnapshot());
            }
        };

        timer.scheduleAtFixedRate(task, 0, DB_PUSHING_DELAY);
    }

    private NonIntrusiveReadingSnapshot takeSnapshot() {
        long timestamp = System.currentTimeMillis();
        boolean motionDetected = camera.isMotionDetected();
        double lightLevel = lightSensor.getLightLevel();
        double audioLevel = microphone.getAudioLevel();

        NonIntrusiveReadingSnapshot readingSnapshot = new NonIntrusiveReadingSnapshot(timestamp, motionDetected, lightLevel, audioLevel);

        return readingSnapshot;
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

    public void setDataAnalysisEndedListener(Profiler.DataAnalysisEndedListener dataAnalysisEndedListener) {
        this.dataAnalysisEndedListener = dataAnalysisEndedListener;
    }
}
