package gr.teicrete.istlab.repros.global;

import android.content.Context;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Antonis on 23-Dec-16.
 */

public class BluetoothServiceSingleton {

    private static BluetoothSPP instance = null;

    private BluetoothServiceSingleton(){}

    public static BluetoothSPP getInstance(Context context) {

        if (instance == null){
            instance = new BluetoothSPP(context);
        }

        return instance;
    }
}
