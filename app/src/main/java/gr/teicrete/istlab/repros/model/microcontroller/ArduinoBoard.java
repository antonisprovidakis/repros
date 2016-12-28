package gr.teicrete.istlab.repros.model.microcontroller;

import java.util.HashMap;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class ArduinoBoard {

    private HashMap<String, Sensor> sensors;
    private HashMap<String, Appliance> appliances;


    public ArduinoBoard() {

        // TODO: here sensors and appliances are created ad-hoc.
        // Normally, they would be created according to database
        // and passed here through setters

        TemperatureSensor temperatureSensor= new TemperatureSensor("temperature_indoors");
        HumiditySensor humiditySensor = new HumiditySensor("humidity_indoors");
        CO2Sensor co2Sensor = new CO2Sensor("co2");
        CTSensor ctSensor = new CTSensor("ct");

        sensors.put(temperatureSensor.getId(), temperatureSensor);
        sensors.put(humiditySensor.getId(), humiditySensor);
        sensors.put(co2Sensor.getId(), co2Sensor);
        sensors.put(ctSensor.getId(), ctSensor);


        AirConditioner airConditioner = new AirConditioner("air_conditioner_1", 100, 17, 20000);
        Lightbulb lightbulb = new Lightbulb("lightbulb_1");
        Window window = new Window("window_1");

        appliances.put(airConditioner.getId(), airConditioner);
        appliances.put(lightbulb.getId(), lightbulb);
        appliances.put(window.getId(), window);
    }

    public HashMap<String, Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(HashMap<String, Sensor> sensors) {
        this.sensors = sensors;
    }

    public HashMap<String, Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(HashMap<String, Appliance> appliances) {
        this.appliances = appliances;
    }
}
