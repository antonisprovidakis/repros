package gr.teicrete.istlab.repros.services;

/**
 * Created by Antonis on 29-Dec-16.
 */

public class WeatherData {

    private double temperature = 0;
    private double humidity = 0;
    private int isDay = 0;

    public WeatherData() {
    }

    public WeatherData(double temperature, double humidity, int isDay) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.isDay = isDay;
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

    public int isDay() {
        return isDay;
    }

    public void setDay(int day) {
        isDay = day;
    }
}
