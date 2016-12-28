package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class AirConditioner extends ElectricAppliance {

    private int currentTemp;
    private int btu;

    public AirConditioner(String id) {
        super(id);
        currentTemp = 19;
        btu = 0;
    }

    public AirConditioner(String id, String name) {
        super(id, name);
        currentTemp = 19;
        btu = 0;
    }

    public AirConditioner(String id, int watt, int currentTemp, int btu) {
        super(id, watt);
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
