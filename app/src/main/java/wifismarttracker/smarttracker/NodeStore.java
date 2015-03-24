package wifismarttracker.smarttracker;

import android.content.Context;

import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.content.Context;

/**
 * Created by graydensmith on 15-01-30.
 */
public class NodeStore {
    String nodeFileName = "nodes.txt";
    String string = "Hello world!";
    FileOutputStream outputStream;


    public NodeStore()
    {

    }

    public void saveNode(Node toSave, Context ctx)
    {
        String toSaveString;
        // File file = new File(context.getFilesDir(), nodeFileName);

        // write node in text format
        // openFileOutput(nodeFileName, Context.MODE_PRIVATE)

        //convert the Node to a string format

        try{
            // open file
            outputStream = ctx.openFileOutput(nodeFileName, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            // close file
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteNode(Node toDelete, Context ctx)
    {
        String toDeleteString = "some string";
        // seek and destroy
        // openFileInput(nodeFileName, Context.MODE_PRIVATE)

        try{
            // open file
            outputStream = ctx.openFileOutput(nodeFileName, Context.MODE_PRIVATE);

            // gets an array of files that are in the Files folder
            File[] nodeFileName = ctx.getFilesDir().listFiles();

            // compare and delete the toDelete file
            for(File file : nodeFileName){
                if(file.getName().equals(toDeleteString)){
                    file.delete();
                }
            }
            // close file
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Node> getAllNodes()
    {
        // read file
        // parse lines in file
        // make array list of nodes

        ArrayList<Node> seeds = new ArrayList<Node>();

        seeds.add(new Node("1234", "Jimmy"));
        seeds.add(new Node("2468", "Timmy"));
        seeds.add(new Node("3457", "Lucy"));
        seeds.add(new Node("7890", "Sally"));

        return seeds;
    }

    private ArrayList<String> readAllFromStorage()
    {
        return new ArrayList<String>();
    }

    private Node parseLine(String line)
    {
        return new Node("asdf", "Jimmy");
    }
}
