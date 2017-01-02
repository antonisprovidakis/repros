package gr.teicrete.istlab.repros.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.model.db.DBHandler;

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
                Intent intent = new Intent(MainActivity.this, TimeDefinitionActivity.class);
                startActivity(intent);
            }
        });


        btnIntrusive = (Button) findViewById(R.id.btn_intrusive);
        btnIntrusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InitializationActivity.class);
                startActivity(intent);
            }
        });

    }
}
