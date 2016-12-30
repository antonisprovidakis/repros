package gr.teicrete.istlab.repros.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cardiomood.android.controls.gauge.SpeedometerGauge;

import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SKSensorModuleType;
import org.sensingkit.sensingkitlib.data.SKAudioLevelData;
import org.sensingkit.sensingkitlib.data.SKLightData;
import org.sensingkit.sensingkitlib.data.SKSensorData;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.profiler.IntrusiveProfiler;
import gr.teicrete.istlab.repros.model.profiler.RoomProfile;
import gr.teicrete.istlab.repros.services.ArduinoData;
import gr.teicrete.istlab.repros.services.WeatherData;

public class IntrusiveProfilingActivity extends AppCompatActivity {

    private static final String TAG = "IntrusProfilActivity";

    private SpeedometerGauge gaugeSoundLevel;
    private SpeedometerGauge gaugeLightLevel;
    private SpeedometerGauge gaugeTempIn;
    private SpeedometerGauge gaugeTempOut;
    private SpeedometerGauge gaugeHumidIn;
    private SpeedometerGauge gaugeHumidOut;
    private SpeedometerGauge gaugeCO;
    private SpeedometerGauge gaugeTotalEnergy;

    private Button btnStop;

    private IntrusiveProfiler profiler;

    private String roomId;
    private RoomProfile roomProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrusive_profiling);

//        roomId = getIntent().getStringExtra("EXTRA_ROOM_ID");
        roomId = "room_1"; // TODO: change ad-hoc roomId
        roomProfile = (RoomProfile) getIntent().getSerializableExtra("EXTRA_ROOM_PROFILE");

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStop();
            }
        });

        setupGauges();

        profiler = new IntrusiveProfiler(getApplicationContext(), roomId, roomProfile);

        profiler.getLightSensor().setOnDataSensedListener(new SKSensorDataListener() {
            @Override
            public void onDataReceived(SKSensorModuleType skSensorModuleType, SKSensorData skSensorData) {
                final SKLightData lightData = (SKLightData) skSensorData;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gaugeLightLevel.setSpeed(lightData.getLight());
                    }
                });
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

        profiler.setWeatherDataReceivedListener(new IntrusiveProfiler.WeatherDataReceivedListener() {
            @Override
            public void onWeatherDataReceived(final WeatherData weatherData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gaugeTempOut.setSpeed(weatherData.getTemperature());
                        gaugeHumidOut.setSpeed(weatherData.getHumidity());

                    }

                    // push in local variable ???

                    // send to firebase ???
                });
            }
        });

        profiler.setArduinoDataReceivedListener(new IntrusiveProfiler.ArduinoDataReceivedListener() {
            @Override
            public void onArduinoDataReceived(ArduinoData arduinoData) {
                updateGaugesRelatedToArduino(arduinoData);
            }
        });

        profiler.startProfiling();

    }

    private void updateGaugesRelatedToArduino(final ArduinoData arduinoData) {

        // update ui
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gaugeTempIn.setSpeed(arduinoData.getTemperature());
                gaugeHumidIn.setSpeed(arduinoData.getHumidity());
                gaugeCO.setSpeed(arduinoData.getCO());
                gaugeTotalEnergy.setSpeed(arduinoData.getEnergyConsumption());
            }
        });
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

    private void showDialogStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Stop Profiling")
                .setMessage("Are you sure you want to stop profiling early?");

        builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "stop clicked");
                profiler.stopProfiling();
                resetGauges();

                btnStop.setVisibility(View.INVISIBLE);

                final ProgressDialog progressDialog = ProgressDialog.show(IntrusiveProfilingActivity.this, "Analyzing Data", "Please wait...", true, false);

                profiler.setDataAnalysisEndedListener(new IntrusiveProfiler.DataAnalysisEndedListener() {
                    @Override
                    public void onDataAnalysisEnd() {
                        progressDialog.dismiss();
                        showDisplayResultsButton();
                    }
                });

                profiler.analyzeData();
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


        gaugeTempIn = (SpeedometerGauge) findViewById(R.id.gauge_temp_in);

        gaugeTempIn.setMinorTicks(5);
        gaugeTempIn.setMajorTickStep(10);

        gaugeTempIn.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeTempIn.setUnitsText("°C");
        gaugeTempIn.setMaxSpeed(60);
        gaugeTempIn.addColoredRange(0, 20, Color.CYAN);
        gaugeTempIn.addColoredRange(21, 30, Color.YELLOW);
        gaugeTempIn.addColoredRange(31, 40, Color.rgb(255, 165, 0));
        gaugeTempIn.addColoredRange(43, 60, Color.RED);

        gaugeTempOut = (SpeedometerGauge) findViewById(R.id.gauge_temp_out);

//        gaugeTempOut.setMinorTicks(5);
        gaugeTempOut.setMajorTickStep(10);

        gaugeTempOut.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeTempOut.setUnitsText("°C");
        gaugeTempOut.setMaxSpeed(60);
        gaugeTempOut.addColoredRange(0, 20, Color.CYAN);
        gaugeTempOut.addColoredRange(21, 30, Color.YELLOW);
        gaugeTempOut.addColoredRange(31, 40, Color.rgb(255, 165, 0));
        gaugeTempOut.addColoredRange(43, 60, Color.RED);


        gaugeHumidIn = (SpeedometerGauge) findViewById(R.id.gauge_humid_in);

        gaugeHumidIn.setMinorTicks(5);
        gaugeHumidIn.setMajorTickStep(10);

        gaugeHumidIn.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeHumidIn.setUnitsText("%");
        gaugeHumidIn.setMaxSpeed(100);
        gaugeHumidIn.addColoredRange(0, 30, Color.rgb(187, 222, 251));
        gaugeHumidIn.addColoredRange(31, 70, Color.rgb(100, 181, 246));
        gaugeHumidIn.addColoredRange(71, 100, Color.rgb(21, 101, 192));

        gaugeHumidOut = (SpeedometerGauge) findViewById(R.id.gauge_humid_out);

        gaugeHumidOut.setMinorTicks(5);
        gaugeHumidOut.setMajorTickStep(10);

        gaugeHumidOut.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeHumidOut.setUnitsText("%");
        gaugeHumidOut.setMaxSpeed(100);
        gaugeHumidOut.addColoredRange(0, 30, Color.rgb(187, 222, 251));
        gaugeHumidOut.addColoredRange(31, 70, Color.rgb(100, 181, 246));
        gaugeHumidOut.addColoredRange(71, 100, Color.rgb(21, 101, 192));

        gaugeCO = (SpeedometerGauge) findViewById(R.id.gauge_co);

        gaugeCO.setMinorTicks(5);
        gaugeCO.setMajorTickStep(100);

        gaugeCO.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeCO.setUnitsText("ppm");
        gaugeCO.setMaxSpeed(500);
        gaugeCO.addColoredRange(0, 50, Color.rgb(255, 255, 51));
        gaugeCO.addColoredRange(51, 100, Color.rgb(255, 165, 0));
        gaugeCO.addColoredRange(101, 500, Color.rgb(255, 0, 0));

        gaugeTotalEnergy = (SpeedometerGauge) findViewById(R.id.gauge_total_energy);

        gaugeTotalEnergy.setMinorTicks(5);
        gaugeTotalEnergy.setMajorTickStep(2000);

        gaugeTotalEnergy.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeTotalEnergy.setUnitsText("watt");
        gaugeTotalEnergy.setMaxSpeed(10000);
        gaugeTotalEnergy.addColoredRange(0, 3500, Color.GREEN);
        gaugeTotalEnergy.addColoredRange(3501, 7000, Color.YELLOW);
        gaugeTotalEnergy.addColoredRange(7001, 10000, Color.RED);
    }

    private void resetGauges() {
        gaugeSoundLevel.setSpeed(0);
        gaugeLightLevel.setSpeed(0);
        gaugeTempIn.setSpeed(0);
        gaugeTempOut.setSpeed(0);
        gaugeHumidIn.setSpeed(0);
        gaugeTempOut.setSpeed(0);
        gaugeCO.setSpeed(0);
        gaugeTotalEnergy.setSpeed(0);
    }

    private void showDisplayResultsButton() {
        btnStop.setText("Display Results");
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntrusiveProfilingActivity.this, IntrusiveAssessmentActivity.class);
//                intent.putExtra("EXTRA_ROOM_ID", roomId);
                startActivity(intent);
            }
        });
        btnStop.setVisibility(View.VISIBLE);
    }
}
