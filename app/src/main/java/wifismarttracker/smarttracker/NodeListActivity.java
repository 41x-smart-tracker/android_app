package wifismarttracker.smarttracker;

import android.app.Activity;
import android.app.Fragment;
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
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import android.net.wifi.*;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NodeListActivity extends Activity {

    private FragmentManager fragmentManager;
    private WifiManager wifiManager;
    private NodeStore nodeStore;
    private DistanceCalcuator _distanceCalculator;

    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<NodeFragment> fragments;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_node_list);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        fragmentManager = getFragmentManager();
        nodeStore = new NodeStore();

        wifiManager.startScan();

        _distanceCalculator = new DistanceCalcuator(wifiManager, nodeStore.getAllNodes());

        startTimer();
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

    private void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshDistances();
                    }
                });
            }
        };
    }

    private void refreshDistances() {
        int i = 0;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ArrayList<DistanceResult> results = _distanceCalculator.distanceResults();

        Iterator<DistanceResult> iterator = results.iterator();

        while (iterator.hasNext()) {
            DistanceResult result = iterator.next();

            Bundle bundle = new Bundle();
            bundle.putString("name", result.name());
            bundle.putFloat("distance", result.distance());

            NodeFragment fragment = new NodeFragment();
            fragment.setArguments(bundle);

            Fragment fragment1 = getFragmentManager().findFragmentByTag(result.name());

            if (fragment1 != null) {
                fragmentTransaction.remove(getFragmentManager().findFragmentByTag(result.name()));
            }
            fragmentTransaction.add(R.id.nodeListLayout, fragment, result.name());

            i++;
        }

        fragmentTransaction.commit();
    }
}
