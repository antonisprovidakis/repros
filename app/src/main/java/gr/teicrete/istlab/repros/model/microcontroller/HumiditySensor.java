package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class HumiditySensor extends Sensor {

    private double humidity;

    public HumiditySensor(String id) {
        super(id);
    }

    public void meterHumidity(){

    }

    public double getHumidity(){
        return humidity;
    }

}
