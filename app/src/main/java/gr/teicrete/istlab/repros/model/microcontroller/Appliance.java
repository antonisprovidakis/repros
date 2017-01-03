package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;

import gr.teicrete.istlab.repros.model.communication.BluetoothModule;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class Appliance {

    private String id;
    private String name;
    private boolean enabled;

    private BluetoothModule bluetoothModule;

    public Appliance(Context context, String id) {
        this(context, id, id);
    }

    public Appliance(Context context, String id, String name) {
        this.id = id;
        this.name = name;
        this.bluetoothModule = new BluetoothModule(context);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void turnOn() {
        if (!enabled) {
            bluetoothModule.sendMessage(id + "_on");
            enabled = true;
        }
    }

    public void turnOff() {
        if (enabled) {
            bluetoothModule.sendMessage(id + "_off");
            enabled = false;
        }
    }
}
