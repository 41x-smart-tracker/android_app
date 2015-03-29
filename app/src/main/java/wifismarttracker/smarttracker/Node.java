package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by graydensmith on 15-01-30.
 */

public class Node {

    private String _securityKey;

    private String _name;

    private String _ssid;

    private DistanceCalculator _calculator;

    public Node(String securityKey, String name, String ssid, WifiManager wifiManager, SensorManager sensorManager)
    {
        _securityKey = securityKey;
        _name = name;
        _ssid = ssid;

        _calculator = new DistanceCalculator(wifiManager, sensorManager, this);
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
        return _calculator.angle();
    }

    public void refresh() {
        _calculator.scan();
    }
}
