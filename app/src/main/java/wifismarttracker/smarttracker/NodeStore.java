package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;

import java.util.ArrayList;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeStore {
    String nodeFileName = "nodes.txt";

    private WifiManager _wifiManager;
    private SensorManager _sensorManager;

    private ArrayList<Node> _nodes;

    public NodeStore(WifiManager wifiManager, SensorManager sensorManager)
    {
        _wifiManager = wifiManager;
        _sensorManager = sensorManager;

        _nodes = new ArrayList<Node>();

        _nodes.add(new Node("1234", "guest", "Cisco01048", _wifiManager, _sensorManager));
        _nodes.add(new Node("2468", "roam", "eduroam", _wifiManager, _sensorManager));
    }

    public void saveNode(Node toSave)
    {
        // open file
        // write node in text format
        // openFileOutput(nodeFileName, Context.MODE_PRIVATE)
        // close file
    }

    public void deleteNode(Node toDelete)
    {
        // open file
        // seek and destroy
        // openFileInput(nodeFileName, Context.MODE_PRIVATE)
        // close file
    }

    public ArrayList<Node> getAllNodes()
    {
        // read file
        // parse lines in file
        // make array list of nodes

        return _nodes;
    }

    private ArrayList<String> readAllFromStorage()
    {
        return new ArrayList<String>();
    }

    private Node parseLine(String line)
    {
        return null;
    }
}
