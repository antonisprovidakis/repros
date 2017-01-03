package gr.teicrete.istlab.repros.model.communication;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * Created by Antonis on 26-Dec-16.
 */

public class WiFiModule extends CommunicationModule {

    private WifiManager wifiManager;

    public WiFiModule(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean isEnabled() {
        return wifiManager.isWifiEnabled();
    }

    @Override
    public void enable() {
        wifiManager.setWifiEnabled(true);
    }

    @Override
    public boolean isConnected() {
        // not implemented
        return true;
    }

    public String getCurrentConnectedAccessPoint() {
        return wifiManager.getConnectionInfo().getSSID();
    }

    public boolean connectToAccessPoint(String ssid, String password) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        String properSSID = String.format("\"%s\"", ssid);
        wifiConfiguration.SSID = properSSID;
        String properPassword = String.format("\"%s\"", password);
        wifiConfiguration.preSharedKey = properPassword;

        int id = wifiManager.addNetwork(wifiConfiguration);
        boolean networkEnabledSuccessfully = wifiManager.enableNetwork(id, true);

        return networkEnabledSuccessfully;
    }

}
