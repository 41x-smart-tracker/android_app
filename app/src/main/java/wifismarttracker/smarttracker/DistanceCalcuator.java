package wifismarttracker.smarttracker;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

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
        if (_wifiManager.getScanResults().size() == 0)
            return (float) 0.0;

        ScanResult result = findNodeInScanResults(node);

        return (float) (result.level * 1.0);
    }

    public int direction(Node node)
    {
        return 1;
    }

    private void scan() {
        _wifiManager.startScan();
    }

    private void scanData() {
        _wifiManager.getScanResults();
    }

    private ScanResult findNodeInScanResults(Node toFind) {
        Iterator<ScanResult> iterator = _wifiManager.getScanResults().iterator();

        while(iterator.hasNext()) {
            ScanResult result = iterator.next();

            if (result.SSID == toFind.ssid())
                return result;
        }

        return null;
    }
}
