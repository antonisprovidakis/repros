package gr.teicrete.istlab.repros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class InitializationActivity extends AppCompatActivity {

    private Button btnScanSQCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization_qrscan);

        btnScanSQCode = (Button) findViewById(R.id.btn_scan_qr_code);
        btnScanSQCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(InitializationActivity.this).initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                initConnections();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initConnections(){

        // show progress dialog
        final ProgressDialog progressDialog =  ProgressDialog.show(this, "Initializing", "Please wait...", true, true);

        // do initializations (need to be in a thread???)
        // TODO: 1) connect to access point
        // TODO: 2) connect to Arduino through Bluetooth

        // 3) dismiss progress dialog
//        progressDialog.dismiss();

        // 4) inflate activity_initialization_ready.xml
    }
}
