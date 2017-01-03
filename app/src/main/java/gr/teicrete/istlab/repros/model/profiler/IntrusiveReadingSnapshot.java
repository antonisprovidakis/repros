package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class IntrusiveReadingSnapshot {

    private long timestamp;
    private double temperatureIndoors;
    private double temperatureOutdoors;
    private double humidityIndoors;
    private double humidityOutdoors;
    private double lightLevel;
    private double audioLevel;
    private double co;
    private double totalEnergyConsumption;

    public IntrusiveReadingSnapshot() {
    }

    public IntrusiveReadingSnapshot(long timestamp, double temperatureIndoors, double temperatureOutdoors,
                                    double humidityIndoors, double humidityOutdoors, double lightLevel, double audioLevel,
                                    double co, double totalEnergyConsumption) {
        this.timestamp = timestamp;
        this.temperatureIndoors = temperatureIndoors;
        this.temperatureOutdoors = temperatureOutdoors;
        this.humidityIndoors = humidityIndoors;
        this.humidityOutdoors = humidityOutdoors;
        this.lightLevel = lightLevel;
        this.audioLevel = audioLevel;
        this.co = co;
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

    public double getLightLevel() {
        return lightLevel;
    }

    public double getAudioLevel() {
        return audioLevel;
    }

    public double getCO() {
        return co;
    }

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }
}
