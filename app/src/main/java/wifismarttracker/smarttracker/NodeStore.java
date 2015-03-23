package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.net.wifi.WifiManager;

import java.util.ArrayList;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeStore {
    String nodeFileName = "nodes.txt";

    private WifiManager _wifiManager;
    private Sensor _accelerometer;

    public NodeStore(WifiManager wifiManager, Sensor accelerometer)
    {
        _wifiManager = wifiManager;
        _accelerometer = accelerometer;
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

        ArrayList<Node> seeds = new ArrayList<Node>();

        seeds.add(new Node("1234", "guest", "Cisco01048", _wifiManager, _accelerometer));
        seeds.add(new Node("2468", "roam", "eduroam", _wifiManager, _accelerometer));

        return seeds;
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
