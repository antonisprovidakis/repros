package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import java.util.ArrayList;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import gr.teicrete.istlab.repros.model.communication.BluetoothModule;
import gr.teicrete.istlab.repros.model.db.DBHandler;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class IntrusiveProfiler implements Profiler {

    private boolean profiling;

    //    private ArduinoBoard arduinoBoard;
    private DBHandler dbHandler;
    private BluetoothModule bluetoothModule;

    private ArrayList<MobileSensor> sensors = new ArrayList<>();

    private LightSensor lightSensor;
    private Microphone microphone;

    public IntrusiveProfiler(Context context, String roomId) {
        lightSensor = new LightSensor(context);
        microphone = new Microphone(context);
        sensors.add(lightSensor);
        sensors.add(microphone);


//        this.arduinoBoard = new ArduinoBoard();
        dbHandler = new DBHandler(roomId);
        bluetoothModule = new BluetoothModule(context);
    }

    @Override
    public void startProfiling() {
        profiling = true;
        dbHandler.prepareForProfiling();

        for (MobileSensor sensor : sensors) {
            sensor.startSensing();
        }
    }

    @Override
    public void stopProfiling() {
        if (profiling) {
            for (MobileSensor sensor : sensors) {
                sensor.stopSensing();
            }
            profiling = false;
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
    public void getArduinoData() {
        final String GET_DATA_COMMAND = "get_data";
        bluetoothModule.sendMessage(GET_DATA_COMMAND);
    }

    // TODO: Also, should it be IntrusiveProfiler.getArduinoData() or ArduinoBoard.getData()? Think!

    public void setOnDataReceivedFromArduinoListener(BluetoothSPP.OnDataReceivedListener onDataReceivedFromArduinoListener) {
        bluetoothModule.setOnDataReceivedListener(onDataReceivedFromArduinoListener);
    }

    public LightSensor getLightSensor() {
        return lightSensor;
    }

    public Microphone getMicrophone() {
        return microphone;
    }
}
