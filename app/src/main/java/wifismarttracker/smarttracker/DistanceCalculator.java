package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by graydensmith on 15-01-30.
 */
public class DistanceCalculator implements SensorEventListener {

    private WifiManager _wifiManager;
    private SensorManager _sensorManager;
    private Node _node;
    private SignalHistory _signalHistory;

    private float _x;
    private float _y;
    private float _z;

    private float _x1;
    private float _y1;
    private float _z1;

    private int _lastAngle;

    private long _lastUpdate = 0;

    private float _speed;

    private static final int SHAKE_THRESHOLD = 50;

    public DistanceCalculator(WifiManager wifiManager, SensorManager sensorManager, Node node) {
        _wifiManager = wifiManager;
        _sensorManager = sensorManager;
        _signalHistory = new SignalHistory();

        _node = node;

        _lastAngle = 0;

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float distanceTo()
    {
        if (_wifiManager.getScanResults() == null)
            return (float) 0.0;

         //free space formala
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults();

        if (result == null)
            return 0;

        _signalHistory.addSignal(result.level);

        /*http://en.wikipedia.org/wiki/Free-space_path_loss*/

        if (result == null)
            return (float) 0.0;

        double distance = (27.55 - (20 * Math.log10(result.frequency)) + Math.abs(_signalHistory.runningAverage())) / 20.0;

        return (float) Math.pow(10.0, distance);
    }

    public int angle() {
        if (gettingCloser() && moving()) {
            return 0;
        } else if (gettingFarther() && moving()) {
            return 180;
        } else {
            return 0;
        }
    }

    public void scan() {
        _wifiManager.startScan();
    }

    private ScanResult findNodeInScanResults() {
        List<ScanResult> results = _wifiManager.getScanResults();

        Iterator<ScanResult> iterator = results.iterator();

        while(iterator.hasNext()) {
            ScanResult result = iterator.next();

            if (result.SSID.equals(_node.ssid()))
                return result;
        }

        return null;
    }

    private boolean moving() {
        return Math.abs(_speed) > 50;
    }

    private boolean gettingCloser() {
        return _signalHistory.runningDiff() > 0;
    }

    private boolean gettingFarther() { return _signalHistory.runningDiff() < 0; }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();

        long diffTime = curTime - _lastUpdate;

        _y1 = _y;
        _z1 = _z;

        _y = sensorEvent.values[1];
        _z = sensorEvent.values[2];

        _lastUpdate = curTime;

        float speed = (_y + _z - _y1 - _z1) / diffTime * 10000;

        if (Math.abs(speed) > SHAKE_THRESHOLD || Math.abs(_speed) > SHAKE_THRESHOLD) {
            if (Math.abs(speed) < 10)
                _speed = 0;
            else
                _speed = speed;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
