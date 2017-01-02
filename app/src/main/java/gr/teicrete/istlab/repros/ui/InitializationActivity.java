package gr.teicrete.istlab.repros.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import gr.teicrete.istlab.repros.R;
import gr.teicrete.istlab.repros.global.BluetoothServiceSingleton;
import gr.teicrete.istlab.repros.model.communication.BluetoothModule;
import gr.teicrete.istlab.repros.model.communication.WiFiModule;

public class InitializationActivity extends AppCompatActivity {

    private static final String TAG = "InitializationActivity";

    private Button btnScanSQCode;

    private WiFiModule wiFiModule;
    private BluetoothModule bluetoothModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization_qrscan);

        btnScanSQCode = (Button) findViewById(R.id.btn_scan_qr_code);
        btnScanSQCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(InitializationActivity.this).initiateScan(); // qr scan
            }
        });

        wiFiModule = new WiFiModule(getApplicationContext());
        bluetoothModule = new BluetoothModule(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject scannedData = new JSONObject(result.getContents());
                    initConnections(scannedData);

                    final String roomId = scannedData.getString("roomId");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(InitializationActivity.this, InitializationReadyActivity.class);
                            intent.putExtra("EXTRA_ROOM_ID", roomId);
                            startActivity(intent);
                        }
                    }, 6000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initConnections(final JSONObject scannedData) {

        String accessPointSSID = "";
        String accessPointPassword = "";
        String arduinoBTMacAddress = "";

        try {
            accessPointSSID = scannedData.getJSONObject("accessPointInfo").getString("ssid");
            accessPointPassword = scannedData.getJSONObject("accessPointInfo").getString("password");
            arduinoBTMacAddress = scannedData.getString("arduinoBTMacAddress");

            // show progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Initializing", "Please wait...", true, false);

            wiFiModule.connectToAccessPoint(accessPointSSID, accessPointPassword);
            bluetoothModule.connectToDevice(arduinoBTMacAddress);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
