package gr.teicrete.istlab.repros.model.microcontroller;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class TemperatureSensor extends Sensor {

    private double temperature;

    public TemperatureSensor(String id) {
        super(id);
    }

    public void meterTemperature() {

    }

    public double getTemperature() {
        return temperature;
    }
}
