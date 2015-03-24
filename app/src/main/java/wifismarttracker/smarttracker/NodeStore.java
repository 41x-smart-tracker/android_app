package wifismarttracker.smarttracker;

import java.util.ArrayList;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeStore {
    String nodeFileName = "nodes.txt";

    public NodeStore()
    {

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

        seeds.add(new Node("1234", "guest", "Cisco01048"));
        seeds.add(new Node("2468", "roam", "eduroam"));

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
