package com.example.gpsspped2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import android.os.Looper;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    TextView myTextView ;
    TextView myTextView2 ;

    private AtomicInteger last_update_counter=new AtomicInteger(0);

    private GoogleMap map;
    private CheckBox mapViewCheckbox;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler(Looper.getMainLooper());
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        myTextView2.setText(String.valueOf(last_update_counter));
                        last_update_counter.incrementAndGet();
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000); // updates each second

        myTextView = (TextView) findViewById(R.id.my_textview);
        myTextView2 = (TextView) findViewById(R.id.my_textview4);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                float speed = location.getSpeed(); // speed in meters/second
                float speedInMilesPerHour = speed * 2.23694f; // convert to miles per hour
                //System.out.println("" + speedInMilesPerHour);
                myTextView.setText(""+ speedInMilesPerHour );
                last_update_counter.set(0);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}