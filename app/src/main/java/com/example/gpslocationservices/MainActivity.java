package com.example.gpslocationservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GPS";
    private static final int PERMISSION_CODE = 200;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2 * 60 * 1000; // in Milliseconds
    protected LocationManager locationManager;
    protected Button retrieveLocationButton;
    static int count = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestPermission();


        retrieveLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // showCurrentLocation();
            }
        });

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "PERMISSION GRANTED");
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                showCurrentLocation();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "SHOW CURRENT LOCATION");
            return;
        }
        Log.d(TAG, "SHOW CURRENT LOCATION2");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);


    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            count++;
            Log.d(TAG, "ONLOCATION CHANGED, COUNT:" + count);


            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
           // Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(lastKnownLocationGPS.getTime());
                SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm:ssa z");
              /*  Log.d(TAG, "GPS Time Zone: " + calendar.getTimeZone().getDisplayName());
                Log.d(TAG, "GPS Time :" + FORMATTER.format(calendar.getTime()));
                Log.d(TAG, "HOUR_OF_DAY :" + calendar.get(Calendar.HOUR));
                Log.d(TAG, "MINUTE :" + calendar.get(Calendar.MINUTE));
                Log.d(TAG, "SECOND :" + calendar.get(Calendar.SECOND));*/
                String msg =  " $$$GPS Time : " + calendar.getTime() + " HOUR_OF_DAY : " + calendar.get(Calendar.HOUR_OF_DAY) + " MINUTE" + calendar.get(Calendar.MINUTE)+ " SECOND : " + calendar.get(Calendar.SECOND) + " YEAR :   " + calendar.get(Calendar.YEAR) + "MONTH :  " + calendar.get(Calendar.MONTH) + "DAY:  " + calendar.get(Calendar.DATE);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
                if (location != null) {
                Log.d(TAG, "mLocation time = " + new Date(location.getTime()));
                Log.d(TAG, "mLocation time in millis = " + location.getTime());
                calculateOffset(location.getTime());
            } else
                Log.d(TAG, " mLocation is null ");

        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MainActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    };

    private void calculateOffset(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat FORMATTER = new SimpleDateFormat("MM/dd/yyyy 'at' hh:mm:ssa z");
        Log.d(TAG, "GPS Time Zone: " + calendar.getTimeZone().getDisplayName());
        Log.d(TAG, "GPS Time :" + FORMATTER.format(calendar.getTime()));
        Log.d(TAG, "HOUR_OF_DAY :" + calendar.get(Calendar.HOUR));
        Log.d(TAG, "MINUTE :" + calendar.get(Calendar.MINUTE));
        Log.d(TAG, "SECOND :" + calendar.get(Calendar.SECOND));
        String msg =  "COUNT: "+ count+ " GPS Time : " + calendar.getTime() + " HOUR_OF_DAY : " + calendar.get(Calendar.HOUR_OF_DAY) + " MINUTE" + calendar.get(Calendar.MINUTE)+ " SECOND : " + calendar.get(Calendar.SECOND) + " YEAR :   " + calendar.get(Calendar.YEAR) + "MONTH :  " + calendar.get(Calendar.MONTH) + "DAY:  " + calendar.get(Calendar.DATE);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }


}