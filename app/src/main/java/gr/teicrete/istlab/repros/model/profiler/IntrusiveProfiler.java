package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import cz.msebera.android.httpclient.Header;
import gr.teicrete.istlab.repros.model.communication.BluetoothModule;
import gr.teicrete.istlab.repros.model.db.DBHandler;
import gr.teicrete.istlab.repros.services.ArduinoData;
import gr.teicrete.istlab.repros.services.WeatherData;
import gr.teicrete.istlab.repros.services.WeatherService;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class IntrusiveProfiler implements Profiler {
    public final static int WEATHER_POLLING_DELAY = 60 * 1000;
    public final static int ARDUINO_POLLING_DELAY = 2 * 1000;
    public final static int DB_PUSHING_DELAY = 5 * 1000;
    public final static String ARDUINO_GET_DATA_COMMAND = "get_data";

    private RoomProfile roomProfile;

    private boolean profiling;

    //    private ArduinoBoard arduinoBoard;
    private DBHandler dbHandler;
    private BluetoothModule bluetoothModule;

    private List<MobileSensor> sensors = new ArrayList<>();
    private LightSensor lightSensor;
    private Microphone microphone;

    private WeatherDataReceivedListener weatherDataReceivedListener = null;
    private ArduinoDataReceivedListener arduinoDataReceivedListener = null;
    private DataAnalysisEndedListener dataAnalysisEndedListener = null;

    private WeatherData weatherData = new WeatherData();
    private ArduinoData arduinoData = new ArduinoData();

    private Timer timer;


    private List<String> recommendations = new ArrayList<>();

    public IntrusiveProfiler(Context context, String roomId, RoomProfile roomProfile) {
        this.roomProfile = roomProfile;

        lightSensor = new LightSensor(context);
        microphone = new Microphone(context);
        sensors.add(lightSensor);
        sensors.add(microphone);

//        this.arduinoBoard = new ArduinoBoard();
        dbHandler = new DBHandler(roomId);

        bluetoothModule = new BluetoothModule(context);
        bluetoothModule.setDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                String[] dataFromArduino = message.split("~");

                double temperatureIndoors = Double.valueOf(dataFromArduino[0]);
                double humidityIndoors = Double.valueOf(dataFromArduino[1]);
                double co2 = Double.valueOf(dataFromArduino[2]);
                double energyConsumption = Double.valueOf(dataFromArduino[3]);

                System.out.println(temperatureIndoors + " - " + humidityIndoors + " - " + co2 + " - " + energyConsumption);

                arduinoData = new ArduinoData(temperatureIndoors, humidityIndoors, co2, energyConsumption);

                // fire event
                if (arduinoDataReceivedListener != null) {
                    arduinoDataReceivedListener.onArduinoDataReceived(arduinoData);
                }
            }
        });

        timer = new Timer();
    }

    @Override
    public void startProfiling() {
        profiling = true;
        dbHandler.prepareForProfiling();

        for (MobileSensor sensor : sensors) {
            sensor.startSensing();
        }

        startPollingForWeatherData();
        startPollingForArduinoData();
        startPushingReadingsToDB();
    }

    @Override
    public void stopProfiling() {
        if (profiling) {
            for (MobileSensor sensor : sensors) {
                sensor.stopSensing();
            }
            profiling = false;

            timer.cancel();
            timer.purge();
        }
    }

    @Override
    public boolean isProfiling() {
        return profiling;
    }

    @Override
    public void analyzeData() {

        DatabaseReference roomReadingSnapshotsRef = dbHandler.getRoomReadingSnapshotsRef();

        if (roomReadingSnapshotsRef != null) {

            roomReadingSnapshotsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // TODO: data analysis code goes here
                    for (DataSnapshot readingDataSnapshot: dataSnapshot.getChildren()) {

                        ReadingsSnapshot readingsSnapshot = readingDataSnapshot.getValue(ReadingsSnapshot.class);
                        System.out.println(readingsSnapshot.getTimestamp());


                    }

                    // TODO: uncomment below when analysis finished
                    // fire event
//                    if(dataAnalysisEndedListener!= null){
//                        dataAnalysisEndedListener.onDataAnalysisEnd();
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void askForArduinoData() {
        bluetoothModule.sendMessage(ARDUINO_GET_DATA_COMMAND);
    }

    private void fetchOutdoorsWeatherData() {
        WeatherService.fetchWeatherData(roomProfile.getCity(), weatherDataHandler);
//        WeatherService.fetchWeatherData(roomProfile.getLatitude() + "," + roomProfile.getLongitude(), weatherHandler);
    }

    private JsonHttpResponseHandler weatherDataHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonWeatherData) {
            try {
                JSONObject objCurrent = jsonWeatherData.getJSONObject("current");

                double temp = objCurrent.getDouble("temp_c");
                double humid = objCurrent.getDouble("humidity");
                int isDay = objCurrent.getInt("is_day");

                weatherData = new WeatherData(temp, humid, isDay);

                // fire event
                if (weatherDataReceivedListener != null) {
                    weatherDataReceivedListener.onWeatherDataReceived(weatherData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void startPollingForWeatherData() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fetchOutdoorsWeatherData();
            }
        };

        timer.scheduleAtFixedRate(task, 0, WEATHER_POLLING_DELAY);
    }

    private void startPollingForArduinoData() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                askForArduinoData();
            }
        };

        timer.scheduleAtFixedRate(task, 0, ARDUINO_POLLING_DELAY);
    }

    private void startPushingReadingsToDB() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                postAReadingsSnapshot();
            }
        };

        timer.scheduleAtFixedRate(task, 0, DB_PUSHING_DELAY);
    }

    private void postAReadingsSnapshot() {
        long timestamp = System.currentTimeMillis();
        double temperatureIndoors = arduinoData.getTemperature();
        double temperatureOutdoors = weatherData.getTemperature();
        double humidityIndoors = arduinoData.getTemperature();
        double humidityOutdoors = weatherData.getHumidity();
        double lightLevel = lightSensor.getLightLevel();
        double audioLevel = microphone.getAudioLevel();
        double co = arduinoData.getCO();
        double totalEnergyConsumption = arduinoData.getEnergyConsumption();

        ReadingsSnapshot readingsSnapshot = new ReadingsSnapshot(timestamp, temperatureIndoors, temperatureOutdoors,
                humidityIndoors, humidityOutdoors, lightLevel, audioLevel, co, totalEnergyConsumption);

        dbHandler.pushNewReadingsSnapshot(readingsSnapshot);
    }

    public LightSensor getLightSensor() {
        return lightSensor;
    }

    public Microphone getMicrophone() {
        return microphone;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public ArduinoData getArduinoData() {
        return arduinoData;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setWeatherDataReceivedListener(WeatherDataReceivedListener weatherDataReceivedListener) {
        this.weatherDataReceivedListener = weatherDataReceivedListener;
    }

    public void setArduinoDataReceivedListener(ArduinoDataReceivedListener arduinoDataReceivedListener) {
        this.arduinoDataReceivedListener = arduinoDataReceivedListener;
    }

    public void setDataAnalysisEndedListener(DataAnalysisEndedListener dataAnalysisEndedListener) {
        this.dataAnalysisEndedListener = dataAnalysisEndedListener;
    }

    public static interface WeatherDataReceivedListener {
        public void onWeatherDataReceived(WeatherData weatherData);
    }

    public static interface ArduinoDataReceivedListener {
        public void onArduinoDataReceived(ArduinoData arduinoData);
    }

    public static interface DataAnalysisEndedListener {
        public void onDataAnalysisEnd();
    }

}
