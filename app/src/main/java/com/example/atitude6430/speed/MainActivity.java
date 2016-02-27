package com.example.atitude6430.speed;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BaseGpsListener {
    TextView displaySpeed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displaySpeed = (TextView) findViewById(R.id.textView2);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

    }
    private void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        if (location != null){
            nCurrentSpeed = location.getSpeed();
        }
        Formatter fat = new Formatter(new StringBuilder());
        fat.format(Locale.US,"%5.1f",nCurrentSpeed);
        String strCurrentSpeed = fat.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ','0');

        String strUnits = "miles/hour";
        displaySpeed.setText(strCurrentSpeed+" "+strUnits);
        //if (this.us)


    }
    public void finish(){
        super.finish();
        System.exit(0);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!= null){
            CLocation myLocation = new CLocation(location,true);
            this.updateSpeed(myLocation);

        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}
