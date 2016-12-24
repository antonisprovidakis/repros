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

public class InitializationActivity extends AppCompatActivity {

    private static final String TAG = "InitializationActivity";

    private boolean isInitialized = false;

    private Button btnScanSQCode;
    private Button btn_intrusive_start_profiling;

    private BluetoothSPP bt;


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

        bt = BluetoothServiceSingleton.getInstance(getApplicationContext());
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

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(InitializationActivity.this, InitializationReadyActivity.class);
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

        String roomProfileId = "";
        String accessPointSSID = "";
        String accessPointPassword = "";
        String arduinoBTMacAddress = "";

        try {
            roomProfileId = scannedData.getString("room_profile_id");
            accessPointSSID = scannedData.getJSONObject("access_point_info").getString("ssid");
            accessPointPassword = scannedData.getJSONObject("access_point_info").getString("password");
            arduinoBTMacAddress = scannedData.getString("arduino_bt_mac_address");

            // show progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(this, "Initializing", "Please wait...", true, true);

            // do initializations (need to be in a thread???)
            // TODO: 1) connect to access point
            connectToAccessPoint("", "");
            // TODO: 2) connect to Arduino through Bluetooth
            connectoToArduino("");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void connectoToArduino(String macAddress) {
        // bt_mac_address: 98:D3:31:40:13:78

        String btMacAddress = "98:D3:31:40:13:78";

//        if (!bt.isBluetoothEnabled()) {
//            bt.enable();
//        }

        if (!bt.isServiceAvailable()) {
            bt.setupService();
        }
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.connect(btMacAddress);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (bt != null) {
//            bt.stopService();
//        }
//    }

    private boolean connectToAccessPoint(String ssid, String password) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//            Toast.makeText(this, "Wifi enabled", Toast.LENGTH_SHORT).show();
//        }

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        String properSSID = String.format("\"%s\"", "OTEc62cc8");
        wifiConfiguration.SSID = properSSID;
        String properPassword = String.format("\"%s\"", "1C8c1418");
        wifiConfiguration.preSharedKey = properPassword;

        int id = wifiManager.addNetwork(wifiConfiguration);
        boolean networkEnabledSuccessfully = wifiManager.enableNetwork(id, true);

        return networkEnabledSuccessfully;
    }
}
