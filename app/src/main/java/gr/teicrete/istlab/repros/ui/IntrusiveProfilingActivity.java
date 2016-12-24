package gr.teicrete.istlab.repros.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.global.BluetoothServiceSingleton;
import gr.teicrete.istlab.repros.speedometer.SpeedometerGauge;

public class IntrusiveProfilingActivity extends AppCompatActivity {

    private static final String TAG = "IntrusProfilActivity";

    private SpeedometerGauge gaugeSoundLevel;
    private SpeedometerGauge gaugeLightLevel;

    private SpeedometerGauge gaugeTempIn;
    private SpeedometerGauge gaugeTempOut;
    private SpeedometerGauge gaugeHumidIn;
    private SpeedometerGauge gaugeHumidOut;
    private SpeedometerGauge gaugeTotalEnergy;

    private Button btnStop;

    private BluetoothSPP bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrusive_profiling);
        btnStop = (Button) findViewById(R.id.btn_stop);

        setupGauges();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEarlyStop();
            }
        });

        bt = BluetoothServiceSingleton.getInstance(getApplicationContext());
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                handleDataReceivedFromArduino(message);
            }
        });
    }
    private void handleDataReceivedFromArduino(String message) {
        String[] dataFromArduino = message.split("~");

        double temp = Double.valueOf(dataFromArduino[0]);
        double humid = Double.valueOf(dataFromArduino[1]);
        double co2 = Double.valueOf(dataFromArduino[2]);

        System.out.println(temp + " - " + humid + " - " + co2);

        // update ui

        // push in local variable ???

        // send to firebase ???
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showDialogCancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bt != null) {
            bt.stopService();
        }
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
        gaugeLightLevel.setMajorTickStep(100);

        gaugeLightLevel.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeLightLevel.setUnitsText("Lux");
        gaugeLightLevel.setMaxSpeed(500);
        gaugeLightLevel.addColoredRange(0, 500, Color.YELLOW);


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


        gaugeTotalEnergy = (SpeedometerGauge) findViewById(R.id.gauge_total_energy);

        gaugeTotalEnergy.setMinorTicks(5);
        gaugeTotalEnergy.setMajorTickStep(50);

        gaugeTotalEnergy.setLabelConverter(new SpeedometerGauge.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        gaugeTotalEnergy.setUnitsText("kWh");
        gaugeTotalEnergy.setMaxSpeed(300);
        gaugeTotalEnergy.addColoredRange(0, 150, Color.GREEN);
        gaugeTotalEnergy.addColoredRange(151, 250, Color.YELLOW);
        gaugeTotalEnergy.addColoredRange(251, 300, Color.RED);

    }
}
