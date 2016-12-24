package gr.teicrete.istlab.repros.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import cn.iwgang.countdownview.CountdownView;
import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.speedometer.SpeedometerGauge;

public class NonIntrusiveProfilingActivity extends AppCompatActivity {
    private static final String TAG = "NonIntrusProfilActivity";


    private SpeedometerGauge gaugeSoundLevel;
    private SpeedometerGauge gaugeLightLevel;

    private Button btnStop;

    private CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_intrusive_profiling);

        long millis = 5000; // millis coming from previous activity
        countdownView = (CountdownView)findViewById(R.id.countdown_timer);
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                Toast.makeText(NonIntrusiveProfilingActivity.this, "timer ended", Toast.LENGTH_SHORT).show();
            }
        });
        countdownView.start(millis);

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEarlyStop();
            }
        });

        setupGauges();

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
                countdownView.stop();
                countdownView.allShowZero();
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
