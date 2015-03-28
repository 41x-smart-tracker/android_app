package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.net.wifi.WifiManager;

/**
 * Created by graydensmith on 15-01-30.
 */

public class Node {

    private String _securityKey;

    private String _name;

    private String _ssid;

    private DistanceCalculator _calculator;

    public Node(String securityKey, String name, String ssid, WifiManager wifiManager, Sensor accelerometer)
    {
        _securityKey = securityKey;
        _name = name;
        _ssid = ssid;

        _calculator = new DistanceCalculator(wifiManager, accelerometer, this);
    }

    public String name()
    {
        return _name;
    }

    public String ssid() {
        return _ssid;
    }

    public void setName(String name) {
        _name = name;
    }

    public double distance() {
        return _calculator.distanceTo();
    }

    public int angle() {
        return 180;
    }
}
