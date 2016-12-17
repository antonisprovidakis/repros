package gr.teicrete.istlab.repros;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import gr.teicrete.istlab.repros.speedometer.SpeedometerGauge;

public class NonIntrusiveProfilingActivity extends AppCompatActivity {
    private static final String TAG = "NonIntrusProfilActivity";


    private SpeedometerGauge gaugeSoundLevel;
    private SpeedometerGauge gaugeLightLevel;

    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_intrusive_profiling);

        btnStop = (Button) findViewById(R.id.btn_stop);

        setupGauges();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogEarlyStop();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showDialogCancel();
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
    }
}
