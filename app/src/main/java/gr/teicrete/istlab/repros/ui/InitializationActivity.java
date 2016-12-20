package gr.teicrete.istlab.repros.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import gr.teicrete.istlab.repros.R;

public class InitializationActivity extends AppCompatActivity {

    private static final String TAG = "InitializationActivity";


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
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    JSONObject scannedData = new JSONObject(result.getContents());
                    Log.i(TAG, scannedData.getString("arduino_ip"));
                    initConnections(scannedData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initConnections(final JSONObject scannedData) {

        String roomProfileId = "";
        String accessPointSSID = "";
        String accessPointPassword = "";
        String arduinoBTMacAddress = "";

        try {
            roomProfileId = scannedData.getString("room_profile_id");
            accessPointSSID = scannedData.getJSONObject("access_point_info").getString("ssid");
            accessPointPassword = scannedData.getJSONObject("access_point_info").getString("password");
            arduinoBTMacAddress = scannedData.getString("arduino_bt_mac_address");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // show progress dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Initializing", "Please wait...", true, true);

        // do initializations (need to be in a thread???)
        // TODO: 1) connect to access point
        // TODO: 2) connect to Arduino through Bluetooth

        // 3) dismiss progress dialog
//        progressDialog.dismiss();

        // 4) inflate activity_initialization_ready.xml
    }
}
