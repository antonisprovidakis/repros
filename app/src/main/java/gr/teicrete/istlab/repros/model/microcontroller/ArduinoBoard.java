package gr.teicrete.istlab.repros.model.microcontroller;

import java.util.HashMap;

/**
 * Created by Antonis on 25-Dec-16.
 */

public class ArduinoBoard {

    private HashMap<String, Sensor> sensors;
    private HashMap<String, Appliance> appliances;


    //    public ArduinoBoard(HashMap<String, Sensor> sensors, HashMap<String, Appliance> appliances) {
    public ArduinoBoard(HashMap<String, Appliance> appliances) {

//        this.sensors = sensors;
        this.appliances = appliances;


//        TemperatureSensor temperatureSensor= new TemperatureSensor("temperature_indoors");
//        HumiditySensor humiditySensor = new HumiditySensor("humidity_indoors");
//        CO2Sensor co2Sensor = new CO2Sensor("co");
//        CTSensor ctSensor = new CTSensor("ct");
//
//        sensors.put(temperatureSensor.getId(), temperatureSensor);
//        sensors.put(humiditySensor.getId(), humiditySensor);
//        sensors.put(co2Sensor.getId(), co2Sensor);
//        sensors.put(ctSensor.getId(), ctSensor);

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

    public Appliance getApplianceWithId(String id) {
        return appliances.get(id);
    }

}
