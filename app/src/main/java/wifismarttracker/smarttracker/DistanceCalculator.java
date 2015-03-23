package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by graydensmith on 15-01-30.
 */
public class DistanceCalculator {

    private WifiManager _wifiManager;
    private Sensor _accelerometer;
    private Node _node;
    private SignalHistory _signalHistory;

    public DistanceCalculator(WifiManager wifiManager, Sensor accelerometer, Node node) {
        _wifiManager = wifiManager;
        _accelerometer = accelerometer;

        _signalHistory = new SignalHistory();
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

    public int direction(Node node)
    {
        return 1;
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
}
