package gr.teicrete.istlab.repros.model.microcontroller;

import android.content.Context;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class AirConditioner extends ElectricAppliance {

    private int currentTemp = 19;
    private int btu = 0;

    public AirConditioner(Context context, String id) {
        this(context, id, id);
    }

    public AirConditioner(Context context, String id, String name) {
        super(context, id, name);
    }

    public AirConditioner(Context context, String id, int watt, int currentTemp, int btu) {
        super(context, id, watt);
        this.currentTemp = currentTemp;
        this.btu = btu;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getBtu() {
        return btu;
    }

    public void setBtu(int btu) {
        this.btu = btu;
    }
}
