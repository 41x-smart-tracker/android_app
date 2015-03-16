package wifismarttracker.smarttracker;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by graydensmith on 15-01-30.
 */
public class DistanceCalcuator {

    private WifiManager _wifiManager;
    private ArrayList<Node> _nodes;

    public DistanceCalcuator(WifiManager wifi, ArrayList<Node> nodes) {
        _wifiManager = wifi;

        _nodes = new ArrayList<Node>();

        Iterator<Node> iterator = nodes.iterator();

        while(iterator.hasNext()) {
            _nodes.add(iterator.next().clone());
        }
    }

    public ArrayList<DistanceResult> distanceResults() {
        ArrayList<DistanceResult> results = new ArrayList<DistanceResult>();

        Iterator<Node> iterator = _nodes.iterator();

        while(iterator.hasNext()) {
            Node next = iterator.next();
            results.add(new DistanceResult(next.name(), distanceTo(next)));
        }

        return results;
    }

    public void startScanning() {

    }

    public void stopScanning() {

    }

    public void pingNode(Node node) {

    }

    public float distanceTo(Node node)
    {
        if (_wifiManager.getScanResults() == null)
            return (float) 0.0;

         //free space formala
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;


        ScanResult result = findNodeInScanResults(node);

        /*http://en.wikipedia.org/wiki/Free-space_path_loss*/

        if (result == null)
            return (float) 0.0;

        double distance = (27.55 - (20 * Math.log10(result.frequency)) + Math.abs(result.level)) / 20.0;

        return (float) Math.pow(10.0, distance);
    }
    public float distanceToMethodTwo(Node node)
    {
        //RSSI formula need calbiration
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(node);
         /*http://www.ijitee.org/attachments/File/v2i2/A0359112112.pdf*/

        // RSSI (dBm) = -10n log10(d) + A
        //n is the propagation constant or path-loss exponent
        //A is the received signal strength in dBm at 1 metre
        //RSSI is level
        double A = 46.6777;
        double N = 46.6777;
        double num = result.level/(-10 * N) - A;
        double distance = Math.pow(10,num);

        return (float) distance;
    }
    public float distanceToMethodThree(Node node)
    {
        //inverse square method need calbiration
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(node);

        double IntensityOne = 46.6777; // dBm at 1 metre distance
        double refDistance = 1;

        double distance = Math.sqrt(IntensityOne*Math.pow(2,refDistance)/ result.level);

        return (float) distance;
    }

    public int direction(Node node)
    {
        return 1;
    }

    public void scan() {
        _wifiManager.startScan();
    }

    private void scanData() {
        _wifiManager.getScanResults();
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
