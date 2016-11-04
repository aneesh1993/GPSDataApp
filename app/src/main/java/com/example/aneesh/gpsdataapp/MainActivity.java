package com.example.aneesh.gpsdataapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static final String broadcastString = "com.example.aneesh.gpsdataapp.broadcast";
    private IntentFilter intentFilter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        dbHandler = new MyDBHandler(this, null, null, 1);

        ////////////////// Start the Service ////////////////////////
        Intent intent = new Intent(this, GPSDataService.class);
        startService(intent);

        intentFilter = new IntentFilter();
        intentFilter.addAction(broadcastString);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        Toast.makeText(MainActivity.this, "Select a database - Local or Server from the toolbar",
                Toast.LENGTH_SHORT).show();


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_online){
            Toast.makeText(MainActivity.this, "archive", Toast.LENGTH_SHORT).show();

            ///////////// Replacing Fragment with Global //////////////////////////////////
            Fragment newServerFragment = new ServerFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newServerFragment);
            transaction.addToBackStack(null);

            transaction.commit();

        }

        if(item.getItemId() == R.id.action_offline){
            Toast.makeText(MainActivity.this, "offline", Toast.LENGTH_SHORT).show();

            ///////////// Replacing Fragment with Local //////////////////////////////////
            Fragment newLocalFragment = new LocalFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newLocalFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(broadcastString)){
                    if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null &&
                            getSupportFragmentManager().findFragmentById(R.id.fragment_container).toString().startsWith("LocalFragment")){

                        LocalFragment lf = (LocalFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        lf.populateList(dbHandler.readDB());
                    }
                    Intent stopIntent = new Intent(MainActivity.this, GPSDataService.class);
                    stopService(stopIntent);
                }

            }


    };
}
