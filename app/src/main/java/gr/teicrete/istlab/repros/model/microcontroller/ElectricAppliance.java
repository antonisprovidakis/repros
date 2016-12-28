package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public abstract class ElectricAppliance extends Appliance {

    private int watt;

    public ElectricAppliance(String id) {
        super(id);
        watt = 0;
    }

    public ElectricAppliance(String id, String name) {
        super(id, name);
        watt = 0;
    }

    public ElectricAppliance(String id, int watt){
        super(id);
        this.watt = watt;
    }


    public int getWatt() {
        return watt;
    }

    public void setWatt(int watt) {
        this.watt = watt;
    }
}
