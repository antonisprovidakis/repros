package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class ReadingsSnapshot {

    private long timestamp;
    private double temperatureIndoors;
    private double temperatureOutdoors;
    private double humidityIndoors;
    private double humidityOutdoors;
    private boolean motionDetected;
    private double lightLevel;
    private double co2;
    private double totalEnergyConsumption;

    public ReadingsSnapshot() {
    }

    public ReadingsSnapshot(long timestamp, double temperatureIndoors, double temperatureOutdoors,
                            double humidityIndoors, double humidityOutdoors, boolean motionDetected, double lightLevel,
                            double co2, double totalEnergyConsumption) {
        this.timestamp = timestamp;
        this.temperatureIndoors = temperatureIndoors;
        this.temperatureOutdoors = temperatureOutdoors;
        this.humidityIndoors = humidityIndoors;
        this.humidityOutdoors = humidityOutdoors;
        this.motionDetected = motionDetected;
        this.lightLevel = lightLevel;
        this.co2 = co2;
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getTemperatureIndoors() {
        return temperatureIndoors;
    }

    public double getTemperatureOutdoors() {
        return temperatureOutdoors;
    }

    public double getHumidityIndoors() {
        return humidityIndoors;
    }

    public double getHumidityOutdoors() {
        return humidityOutdoors;
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    public double getLightLevel() {
        return lightLevel;
    }

    public double getCo2() {
        return co2;
    }

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }
}
