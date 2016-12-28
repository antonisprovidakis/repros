package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SKSensorModuleType;
import org.sensingkit.sensingkitlib.SensingKitLib;
import org.sensingkit.sensingkitlib.SensingKitLibInterface;
import org.sensingkit.sensingkitlib.data.SKAudioLevelData;
import org.sensingkit.sensingkitlib.data.SKSensorData;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class Microphone implements MobileSensor {


    private SensingKitLibInterface mSensingKitLib = null;
    private SKSensorDataListener skSensorDataListener = null;

    private double audioLevel;

    public Microphone(Context context) {
        try {
            mSensingKitLib = SensingKitLib.getSensingKitLib(context);
            mSensingKitLib.registerSensorModule(SKSensorModuleType.AUDIO_LEVEL);
            mSensingKitLib.subscribeSensorDataListener(SKSensorModuleType.AUDIO_LEVEL, new SKSensorDataListener() {
                @Override
                public void onDataReceived(SKSensorModuleType skSensorModuleType, SKSensorData skSensorData) {
                    SKAudioLevelData audioData = (SKAudioLevelData) skSensorData;
                    audioLevel = (20 * Math.log10(audioData.getLevel() / 0.6)); // update internal state

                }
            });
        } catch (SKException e) {
            e.printStackTrace();
        }
    }


    public double getAudioLevel() {
        return audioLevel;
    }

    @Override
    public void startSensing() {
        try {
            if (skSensorDataListener != null) {
                mSensingKitLib.subscribeSensorDataListener(SKSensorModuleType.AUDIO_LEVEL, skSensorDataListener);
            }
            mSensingKitLib.startContinuousSensingWithSensor(SKSensorModuleType.AUDIO_LEVEL);
        } catch (SKException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopSensing() {
        try {
            mSensingKitLib.stopContinuousSensingWithSensor(SKSensorModuleType.AUDIO_LEVEL);
            if (skSensorDataListener != null) {
                mSensingKitLib.unsubscribeSensorDataListener(SKSensorModuleType.AUDIO_LEVEL, skSensorDataListener);
            }
            mSensingKitLib.deregisterSensorModule(SKSensorModuleType.AUDIO_LEVEL);
        } catch (SKException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnDataSensedListener(Object listener) {
        skSensorDataListener = (SKSensorDataListener) listener;
    }
}
