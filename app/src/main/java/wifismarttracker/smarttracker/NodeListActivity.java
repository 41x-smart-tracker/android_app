package wifismarttracker.smarttracker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class NodeListActivity extends FragmentActivity implements OnClickListener {

    private FragmentManager fragmentManager;
    private WifiManager wifiManager;
    private NodeStore nodeStore;
    private DistanceCalcuator _distanceCalculator;

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
        nodeStore = new NodeStore();

        wifiManager.startScan();

        _distanceCalculator = new DistanceCalcuator(wifiManager, nodeStore.getAllNodes());

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

        _distanceCalculator.scan();

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

            Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(result.name());

            if (fragment1 != null) {
                fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag(result.name()));
            }
            fragmentTransaction.add(R.id.nodeListLayout, fragment, result.name());

            i++;
        }

        fragmentTransaction.commit();
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
