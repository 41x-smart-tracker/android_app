package wifismarttracker.smarttracker;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by graydensmith on 15-01-30.
 */
public class DistanceCalcuator {

    private WifiManager wifiManager;

    private boolean scanning = false;

    public DistanceCalcuator(WifiManager wifi)
    {
        wifiManager = wifi;
    }

    public void startScanning()
    {

    }

    public void stopScanning()
    {

    }

    public void pingNode(Node node)
    {

    }

    public double distanceTo(Node node)
    {
        return 0.0;
    }

    public int direction(Node node)
    {
        return 1;
    }

    private void scan()
    {
        wifiManager.startScan();
    }

    private void scanData()
    {
        wifiManager.getScanResults();
    }
}
