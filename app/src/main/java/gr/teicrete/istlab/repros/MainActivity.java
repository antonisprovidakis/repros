package gr.teicrete.istlab.repros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnNonIntrusive;
    private Button btnIntrusive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        btnNonIntrusive = (Button) findViewById(R.id.btn_non_intrusive);
        btnNonIntrusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimeDefinitionActivity.class);
                startActivity(intent);
            }
        });


        btnIntrusive = (Button) findViewById(R.id.btn_intrusive);
        btnIntrusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntrusiveProfilingActivity.class);
                startActivity(intent);
            }
        });
    }
}
