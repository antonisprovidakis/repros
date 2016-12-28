package gr.teicrete.istlab.repros.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;
import com.jwetherell.motiondetection.detection.MotionStateChangeListener;

import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SKSensorModuleType;
import org.sensingkit.sensingkitlib.data.SKAudioLevelData;
import org.sensingkit.sensingkitlib.data.SKLightData;
import org.sensingkit.sensingkitlib.data.SKSensorData;

import cn.iwgang.countdownview.CountdownView;
import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.profiler.NonIntrusiveProfiler;

public class NonIntrusiveProfilingActivity extends AppCompatActivity {
    private static final String TAG = "NonIntrusProfilActivity";


    private CountdownView countdownTimer;

    private ImageView imageViewMotion;
    private SurfaceView surfaceView;

    private SpeedometerGauge gaugeSoundLevel;
    private SpeedometerGauge gaugeLightLevel;

    private Button btnStop;

    private NonIntrusiveProfiler profiler;

    private long profilingMillis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_intrusive_profiling);

        profilingMillis = getIntent().getLongExtra("EXTRA_PROFILING_MILLIS", 30000);

        imageViewMotion = (ImageView) findViewById(R.id.image_view_motion);

        surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEarlyStop();
            }
        });

        setupGauges();

        countdownTimer = (CountdownView) findViewById(R.id.countdown_timer);
        countdownTimer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                profiler.stopProfiling();
                resetGauges();
                Toast.makeText(NonIntrusiveProfilingActivity.this, "timer ended", Toast.LENGTH_SHORT).show();
                showDisplayResultsButton();
            }
        });


        profiler = new NonIntrusiveProfiler(getApplicationContext());

        profiler.getCamera().setSurfacePreview(surfaceView);
        profiler.getCamera().setOnDataSensedListener(new MotionStateChangeListener() {
            @Override
            public void onMotionStateChange(final boolean motionDetected) {
                System.out.println(motionDetected);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (motionDetected) {
                            imageViewMotion.setImageResource(R.drawable.motion);
                        } else {
                            imageViewMotion.setImageResource(R.drawable.no_motion);
                        }
                    }
                });
            }
        });

        profiler.getLightSensor().setOnDataSensedListener(new SKSensorDataListener() {
            @Override
            public void onDataReceived(SKSensorModuleType skSensorModuleType, SKSensorData skSensorData) {
                SKLightData lightData = (SKLightData) skSensorData;
                gaugeLightLevel.setSpeed(lightData.getLight());
            }
        });

        profiler.getMicrophone().setOnDataSensedListener(new SKSensorDataListener() {
            @Override
            public void onDataReceived(SKSensorModuleType skSensorModuleType, SKSensorData skSensorData) {
                final SKAudioLevelData audioData = (SKAudioLevelData) skSensorData;
                final double audioLevel = (20 * Math.log10(audioData.getLevel() / 4));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (audioLevel >= 0) {
                            gaugeSoundLevel.setSpeed(audioLevel);
                        }
                    }
                });
            }
        });

        profiler.startProfiling();
        countdownTimer.start(profilingMillis);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showDialogCancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profiler = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        profiler.stopProfiling();
//        profiler = null;
        countdownTimer.stop();
        countdownTimer.allShowZero();
        resetGauges();
    }

    private void showDialogCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cancel Profiling")
                .setMessage("Are you sure you want to cancel profiling and go back to Time Definition?");

        builder.setPositiveButton("Cancel Profiling", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "cancel clicked");
                finish();
            }
        });
        builder.setNegativeButton("Continue Profiling", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "continue pressed");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialogEarlyStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Stop Profiling")
                .setMessage("Are you sure you want to stop profiling early?");

        builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "stop clicked");
                profiler.stopProfiling();
                resetGauges();
                countdownTimer.stop();
                countdownTimer.allShowZero();

                showDisplayResultsButton();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "cancel clicked");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupGauges() {
        gaugeSoundLevel = (SpeedometerGauge) findViewById(R.id.gauge_sound_level);

        gaugeSoundLevel.setMajorTickStep(10);

        gaugeSoundLevel.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeSoundLevel.setUnitsText("dB");
        gaugeSoundLevel.setMaxSpeed(120);
        gaugeSoundLevel.addColoredRange(0, 60, Color.GREEN);
        gaugeSoundLevel.addColoredRange(61, 100, Color.YELLOW);
        gaugeSoundLevel.addColoredRange(101, 120, Color.RED);

        gaugeLightLevel = (SpeedometerGauge) findViewById(R.id.gauge_light_level);

        gaugeLightLevel.setMinorTicks(5);
        gaugeLightLevel.setMajorTickStep(200);

        gaugeLightLevel.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeLightLevel.setUnitsText("Lux");
        gaugeLightLevel.setMaxSpeed(1000);
        gaugeLightLevel.addColoredRange(0, 1000, Color.YELLOW);
    }

    private void resetGauges() {
        gaugeSoundLevel.setSpeed(0);
        gaugeLightLevel.setSpeed(0);
    }

    private void showDisplayResultsButton() {
        btnStop.setText("Display Results");
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NonIntrusiveProfilingActivity.this, NonIntrusiveAssessmentActivity.class);
//                        intent.putExtra("EXTRA_ROOM_ID", roomId);
                startActivity(intent);
            }
        });
    }
}
