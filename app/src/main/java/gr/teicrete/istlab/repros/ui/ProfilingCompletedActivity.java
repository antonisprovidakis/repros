package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gr.teicrete.istlab.repros.R;

public class ProfilingCompletedActivity extends AppCompatActivity {

    private Button btn_display_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_completed);

        btn_display_results = (Button) findViewById(R.id.btn_display_results);
        btn_display_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                // TODO: depending on param ("NON_INTRUSIVE" or "INTRUSIVE"),
                // start appropriate activity

//                intent = new Intent(ProfilingCompletedActivity.this, NonIntrusiveResultsActivity.class);
//                startActivity(intent);
            }
        });

    }
}
