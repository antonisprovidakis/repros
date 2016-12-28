package gr.teicrete.istlab.repros.model.profiler;

import android.content.Context;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SKSensorModuleType;
import org.sensingkit.sensingkitlib.SensingKitLib;
import org.sensingkit.sensingkitlib.SensingKitLibInterface;
import org.sensingkit.sensingkitlib.data.SKLightData;
import org.sensingkit.sensingkitlib.data.SKSensorData;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class LightSensor implements MobileSensor {

    private SensingKitLibInterface mSensingKitLib = null;
    private SKSensorDataListener skSensorDataListener = null;

    private double lightLevel;

    public LightSensor(Context context) {
        try {
            mSensingKitLib = SensingKitLib.getSensingKitLib(context);
            mSensingKitLib.registerSensorModule(SKSensorModuleType.LIGHT);
            mSensingKitLib.subscribeSensorDataListener(SKSensorModuleType.LIGHT, new SKSensorDataListener() {
                @Override
                public void onDataReceived(SKSensorModuleType skSensorModuleType, SKSensorData skSensorData) {
                    SKLightData lightData = (SKLightData) skSensorData;
                    lightLevel = lightData.getLight(); // update internal state
                }
            });
        } catch (SKException e) {
            e.printStackTrace();
        }
    }

    public double getLightLevel() {
        return lightLevel;
    }

    @Override
    public void startSensing() {
        try {
            if (skSensorDataListener != null) {
                mSensingKitLib.subscribeSensorDataListener(SKSensorModuleType.LIGHT, skSensorDataListener);
            }
            mSensingKitLib.startContinuousSensingWithSensor(SKSensorModuleType.LIGHT);
        } catch (SKException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopSensing() {
        try {
            mSensingKitLib.stopContinuousSensingWithSensor(SKSensorModuleType.LIGHT);
            if (skSensorDataListener != null) {
                mSensingKitLib.unsubscribeSensorDataListener(SKSensorModuleType.LIGHT, skSensorDataListener);
            }
            mSensingKitLib.deregisterSensorModule(SKSensorModuleType.LIGHT);
        } catch (SKException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOnDataSensedListener(Object listener) {
        skSensorDataListener = (SKSensorDataListener) listener;
    }

}
