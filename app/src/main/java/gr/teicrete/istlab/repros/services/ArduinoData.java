package gr.teicrete.istlab.repros.services;

/**
 * Created by Antonis on 29-Dec-16.
 */


public class ArduinoData {

    private double temperature = 0;
    private double humidity = 0;
    private double co = 0;
    private double energyConsumption = 0;

    public ArduinoData() {
    }

    public ArduinoData(double temperature, double humidity, double co, double energyConsumption) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.co = co;
        this.energyConsumption = energyConsumption;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCO() {
        return co;
    }

    public void setCO(double co) {
        co = co;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }
}
