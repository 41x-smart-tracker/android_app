package wifismarttracker.smarttracker;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    public void saveNode(Node toSave, Context ctx)
    {

        //String filename = "smartTrackerRegistry.txt";
        String string;
        FileOutputStream outputStream;
        // open file
        // write node in text format
        // openFileOutput(nodeFileName, Context.MODE_PRIVATE)
        // close file

        string = toSave.ssid() + ';' + toSave.name() + ';' + toSave.angle() + ';' + toSave.distance() + ';';

        try {
            outputStream = ctx.openFileOutput(nodeFileName, Context.MODE_APPEND | Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNode(Node toDelete, Context ctx) throws IOException
    {
        String currentLine;
        String toDeleteString;

        // open file
        // seek and destroy
        // openFileInput(nodeFileName, Context.MODE_PRIVATE)
        // close file

        File inputFile = new File(nodeFileName);
        File tempFile = new File("nodeFileName_temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        toDeleteString = toDelete.ssid() + ';' + toDelete.name() + ';' + toDelete.angle() + ';' + toDelete.distance() + ';';

        // checks line by line if it's the string we want, if it matches, skip that line (delete), otherwise copy line to temp file
        while((currentLine = reader.readLine()) != null) {
            if(null!=currentLine && !currentLine.equalsIgnoreCase(toDeleteString)){
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(inputFile); // renames the temp file to the original file

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
