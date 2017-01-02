package gr.teicrete.istlab.repros.model.profiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class RoomProfile implements Serializable {

    private String roomName;
    private String country;
    private String city;
    private String buildingName;
    private double latitude;
    private double longitude;
    private double sqMeters;
    private Map<String, String> accessPointInfo;
    private List<HashMap<String, String>> installedAppliances;
    private List<HashMap<String, String>> installedSensors;
    private Map<String, Double> optimumTemperature;
    private Map<String, Double> optimumHumidity;

    public RoomProfile() {
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSqMeters() {
        return sqMeters;
    }

    public Map<String, String> getAccessPointInfo() {
        return accessPointInfo;
    }

    public List<HashMap<String, String>> getInstalledAppliances() {
        return installedAppliances;
    }

    public List<HashMap<String, String>> getInstalledSensors() {
        return installedSensors;
    }

    public Map<String, Double> getOptimumTemperature() {
        return optimumTemperature;
    }

    public Map<String, Double> getOptimumHumidity() {
        return optimumHumidity;
    }

    @Override
    public String toString() {
        return roomName + " " + country + " " + city + " " + buildingName + " " + latitude + " " + longitude + " " + sqMeters + " " + accessPointInfo +
                " " + installedAppliances + " " + installedSensors;
    }
    
    //    public OptimalRange getOptimumTemperature() {
//        return optimumTemperature;
//    }
//
//    public void setOptimalTemperature(OptimalRange optimumTemperature) {
//        this.optimumTemperature = optimumTemperature;
//    }
//
//    public OptimalRange getOptimumHumidity() {
//        return optimumHumidity;
//    }
//
//    public void setOptimalHumidity(OptimalRange optimumHumidity) {
//        this.optimumHumidity = optimumHumidity;
//    }


//    public static class OptimalRange<T extends Number> {
//
//        private T min;
//        private T max;
//
//        public OptimalRange(T min, T max) {
//            this.min = min;
//            this.max = max;
//        }
//
//        public T min() {
//            return min;
//        }
//
//
//        public T max() {
//            return max;
//        }
//
//        public boolean isInRange(Integer value) {
//            return min.intValue() <= value && value <= max.intValue();
//        }
//
//        public boolean isInRange(Double value) {
//            return min.doubleValue() <= value && value <= max.doubleValue();
//        }
//    }

    public static boolean isInRange(Map<String, Double> map, Double value) {
        return map.get("min") <= value && value <= map.get("max");
    }
}
