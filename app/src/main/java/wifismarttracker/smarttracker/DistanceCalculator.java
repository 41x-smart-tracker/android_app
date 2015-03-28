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
        scan();

        if (_wifiManager.getScanResults() == null)
            return (float) 0.0;

         //free space formala
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(_node);

        _signalHistory.addSignal(result.level);

        /*http://en.wikipedia.org/wiki/Free-space_path_loss*/

        if (result == null)
            return (float) 0.0;

        double distance = (27.55 - (20 * Math.log10(result.frequency)) + Math.abs(_signalHistory.runningAverage())) / 20.0;

        return (float) Math.pow(10.0, distance);
    }
    public float distanceToMethodTwo(Node node)
    {
        // Results are device specific and therefore not generally accessible

        //RSSI formula need calbiration
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(node);
         /*http://www.ijitee.org/attachments/File/v2i2/A0359112112.pdf*/

        if (result == null)
            return (float) 0.0;

        // RSSI (dBm) = -10n log10(d) + A
        //n is the propagation constant or path-loss exponent
        //A is the received signal strength in dBm at 1 metre
        //RSSI is level
        double A = 46.6777;
        double N = 46.6777;
        double num = result.level / (-10 * N) - A;
        double distance = Math.pow(10,num);

        return (float) distance;
    }
    public float distanceToMethodThree(Node node)
    {
        // Produces very inaccurate results


        //inverse square method need calbiration
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(node);

        if (result == null)
            return (float) 0.0;

        double IntensityOne = 36; // measured dBm at 1m

        double distance = Math.sqrt(Math.abs(result.level / IntensityOne));

        return (float) distance;
    }

    public int angle() {
        if (moving() && gettingCloser()) {
            return 0;
        } else if (moving() && gettingFarther()) {
            return 180;
        } else {
            return 0;
        }
    }

    public void scan() {
        _wifiManager.startScan();
    }

    private ScanResult findNodeInScanResults(Node toFind) {
        List<ScanResult> results = _wifiManager.getScanResults();

        Iterator<ScanResult> iterator = results.iterator();

        while(iterator.hasNext()) {
            ScanResult result = iterator.next();

            if (result.SSID.equals(toFind.ssid()))
                return result;
        }

        return null;
    }

    private boolean moving() {
        return Math.abs(_speed) > 50;
    }

    private boolean gettingCloser() {
        return _signalHistory.diff() > 0;
    }

    private boolean gettingFarther() {
        return _signalHistory.diff() < 0;
    }

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
            Log.v("speed", String.valueOf(speed));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
