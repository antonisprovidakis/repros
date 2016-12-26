package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class CTSensor extends Sensor {

    private double totalEnergyConsumption;

    public CTSensor(String id) {
        super(id);
    }


    public void meterTotalEnergyConsumption(){

    }

    public double getTotalEnergyConsumption(){
        return totalEnergyConsumption;
    }


}
