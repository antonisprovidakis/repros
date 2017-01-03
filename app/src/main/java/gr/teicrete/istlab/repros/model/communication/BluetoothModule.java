package gr.teicrete.istlab.repros.model.communication;

import android.content.Context;
import android.util.Log;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import gr.teicrete.istlab.repros.global.BluetoothServiceSingleton;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class BluetoothModule extends CommunicationModule {

    public static String TAG = "BluetoothModule";

    private BluetoothSPP bt;

    public BluetoothModule(Context context) {
        bt = BluetoothServiceSingleton.getInstance(context);

        if (!bt.isServiceAvailable()) {
            bt.setupService();
        }
    }

    public String getCurrentConnectedDeviceName() {
        return bt.getConnectedDeviceName();
    }

    public String getCurrentConnectedDeviceAddress() {
        return bt.getConnectedDeviceAddress();
    }

    @Override
    public boolean isEnabled() {
        return bt.isBluetoothEnabled();
    }

    @Override
    public void enable() {
        bt.enable();
    }

    @Override
    public boolean isConnected() {
        return bt.getConnectedDeviceName() != null;
    }

    public void connectToDevice(String macAddress) {
        if (bt.isBluetoothEnabled()) {
            bt.startService(BluetoothState.DEVICE_OTHER);
            bt.connect(macAddress);
        } else {
            Log.i(TAG, "Bluetooth not enabled. Can't connect to device");
        }
    }

    public void sendMessage(String message) {
        bt.send(message, false);
    }

    public void setDataReceivedListener(BluetoothSPP.OnDataReceivedListener onDataReceivedListener){
        bt.setOnDataReceivedListener(onDataReceivedListener);
    }

    public void setBluetoothConnectionListener(BluetoothSPP.BluetoothConnectionListener bluetoothConnectionListener) {
        bt.setBluetoothConnectionListener(bluetoothConnectionListener);
    }
}
