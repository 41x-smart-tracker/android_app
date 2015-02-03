package wifismarttracker.smarttracker;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import android.net.wifi.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeListActivity extends Activity {

    FragmentManager fragmentManager;
    WifiManager wifiManager;
    NodeStore nodeStore;

    public NodeListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_node_list);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        fragmentManager = getFragmentManager();
        nodeStore = new NodeStore();

        setupNodeFragments();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_node_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupNodeFragments() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ArrayList<Node> nodes = nodeStore.getAllNodes();

        Iterator<Node> iterator = nodes.iterator();

        while (iterator.hasNext()) {
            Node node = iterator.next();

            Bundle bundle = new Bundle();
            bundle.putString("name", node.name());

            NodeFragment fragment = new NodeFragment();
            fragment.setArguments(bundle);

            fragmentTransaction.add(R.id.nodeListLayout, fragment);
        }

        fragmentTransaction.commit();
    }
}
