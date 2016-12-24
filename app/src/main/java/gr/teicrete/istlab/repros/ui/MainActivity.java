package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gr.teicrete.istlab.repros.R;

public class MainActivity extends AppCompatActivity {

    private Button btnNonIntrusive;
    private Button btnIntrusive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNonIntrusive = (Button) findViewById(R.id.btn_non_intrusive);
        btnNonIntrusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TimeDefinitionActivity.class);
                Intent intent = new Intent(MainActivity.this, NonIntrusiveProfilingActivity.class);
                startActivity(intent);
            }
        });

        btnIntrusive = (Button) findViewById(R.id.btn_intrusive);
        btnIntrusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntrusiveProfilingActivity.class);
//                Intent intent = new Intent(MainActivity.this, InitializationActivity.class);
//                Intent intent = new Intent(MainActivity.this, InitializationReadyActivity.class);
//                Intent intent = new Intent(MainActivity.this, IntrusiveAssessmentActivity.class);
                startActivity(intent);
            }
        });
    }
}
