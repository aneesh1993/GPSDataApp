package com.example.aneesh.gpsdataapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.LogRecord;


public class GPSDataService extends Service {
    public GPSDataService() {
    }

    private static final String LOG_TAG = "gpsdataapp-LOG";
    private static final int WAIT_TIME = 10000;

    Handler timeHandler;
    public String[] dataFromGDP = new String[3];

    MyDBHandler dbHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getGPSData();
        dbHandler = new MyDBHandler(this, null, null, 1);

        timeHandler = new Handler();
        Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {

                if(dataFromGDP[0] != null && dataFromGDP[1] != null && dataFromGDP[2] != null){
                    System.out.println(dataFromGDP[0]);
                    System.out.println(dataFromGDP[1]);
                    System.out.println(dataFromGDP[2]);

                    GPSDataDb dbEntry = new GPSDataDb(dataFromGDP[0], dataFromGDP[1], dataFromGDP[2]);
                    dbHandler.addEntry(dbEntry);

                    Intent broadcast = new Intent();
                    broadcast.setAction(MainActivity.broadcastString);
                    sendBroadcast(broadcast);
                }
                timeHandler.postDelayed(this, WAIT_TIME);
            }
        };

        timeHandler.postDelayed(timeRunnable, 0);
        //return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    public void getGPSData(){
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            final Location loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Log.i(LOG_TAG, "" + loc.getLatitude() + "*_*" + loc.getLongitude());


            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    Date date = new Date(location.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss a");

                    sdf.setTimeZone(TimeZone.getTimeZone("EST"));
                    String formattedDate = sdf.format(date);

                    dataFromGDP[0] = formattedDate;
                    dataFromGDP[1] = location.getLatitude() + "";
                    dataFromGDP[2] = location.getLongitude() + "";

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy is called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
