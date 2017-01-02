package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class ElectricAppliance extends Appliance {

    private int watt = 0;

    public ElectricAppliance(Context context, String id) {
        this(context, id, 0);
    }

    public ElectricAppliance(Context context, String id, String name) {
        super(context, id, name);
    }

    public ElectricAppliance(Context context, String id, int watt){
        this(context, id, id);
        this.watt = watt;
    }

    public int getWatt() {
        return watt;
    }

    public void setWatt(int watt) {
        this.watt = watt;
    }
}
