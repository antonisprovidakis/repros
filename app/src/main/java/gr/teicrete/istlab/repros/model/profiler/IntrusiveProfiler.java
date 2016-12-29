package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

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

    private WeatherData weatherData = new WeatherData();
    private ArduinoData arduinoData = new ArduinoData();

    private Timer timer;

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
    }

    // TODO: Is it really needed?
    public void pushNewReadingsSnapshotToDB(ReadingsSnapshot readingsSnapshot) {
        dbHandler.pushNewReadingsSnapshot(readingsSnapshot);
    }

    // TODO: change return type, if continue this way
    // and not getting data by each component individually
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

    public void setWeatherDataReceivedListener(WeatherDataReceivedListener weatherDataReceivedListener) {
        this.weatherDataReceivedListener = weatherDataReceivedListener;
    }

    public void setArduinoDataReceivedListener(ArduinoDataReceivedListener arduinoDataReceivedListener) {
        this.arduinoDataReceivedListener = arduinoDataReceivedListener;
    }

    public static interface WeatherDataReceivedListener {
        public void onWeatherDataReceived(WeatherData weatherData);
    }

    public static interface ArduinoDataReceivedListener {
        public void onArduinoDataReceived(ArduinoData arduinoData);
    }


}
