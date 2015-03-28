package wifismarttracker.smarttracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class NodeListActivity extends FragmentActivity implements OnClickListener {

    private FragmentManager fragmentManager;
    private WifiManager wifiManager;
    private NodeStore nodeStore;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Timer timer;
    private TimerTask timerTask;

    private ArrayList<NodeFragment> fragments;

    private final Handler handler = new Handler();

    private Button scanBtn;
    private TextView formatTxt, contentTxt;

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

        timer = new Timer();

        //instantiates the Button and TextView variables relative to the ID in the layout
        /*
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);
        */
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

        switch(id){
            case R.id.action_search:
                //do search stuff
                Log.v("MenuBarTest", "Searching stuff");
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
                return true;

            case R.id.action_add:
                //do add stuff
                Log.v("MenuBarTest", "Adding stuff");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startTimer() {
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
            bundle.putInt("angle", node.angle());

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
        timer.cancel();
    }


    //------------------
    //QR code scanner

    public void onClick(View v){
        //on button click, check if the scan button was pressed
/*
        if(v.getId()==R.id.scan_button){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //formatTxt.setText("FORMAT: " + scanFormat);
            //contentTxt.setText("CONTENT: " + scanContent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
