package wifismarttracker.smarttracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import android.net.wifi.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class NodeListActivity extends FragmentActivity {

    private FragmentManager fragmentManager;
    private WifiManager wifiManager;
    private NodeStore nodeStore;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<NodeFragment> fragments;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_node_list);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        fragmentManager = getSupportFragmentManager();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        nodeStore = new NodeStore(wifiManager, accelerometer);

        wifiManager.startScan();

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

        Iterator<Node> iterator = nodeStore.getAllNodes().iterator();

        while (iterator.hasNext()) {
            Node node = iterator.next();

            Bundle bundle = new Bundle();
            bundle.putString("name", node.name());
            bundle.putFloat("distance", (float) node.distance());
            bundle.putInt("direction", node.direction());

            NodeFragment fragment = new NodeFragment();
            fragment.setArguments(bundle);

            Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(node.name());

            if (fragment1 != null) {
                fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag(node.name()));
            }
            fragmentTransaction.add(R.id.nodeListLayout, fragment, node.name());

            i++;
        }

        fragmentTransaction.commit();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

    }
}
