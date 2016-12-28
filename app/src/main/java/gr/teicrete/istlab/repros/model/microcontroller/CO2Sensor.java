package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class CO2Sensor extends Sensor {

    private double co2;

    public CO2Sensor(String id) {
        super(id);
    }

    public void meterCO2(){

    }

    public double getCO2(){
        return co2;
    }
}
