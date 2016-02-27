package com.example.atitude6430.speed;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BaseGpsListener,unitsFragment.buttonClickListener, movementFragment.OnMovementListener {
    TextView displaySpeed;

    public void pickFragment(Object object){
        MainActivity.this.updateSpeed(null);

        ShowHideFragment(true);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, (Fragment) object);
        fragmentTransaction.commit();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displaySpeed = (TextView) findViewById(R.id.textView2);

        //for GPS location============================
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

        //for communication witrh units fragment
        units = false; //to check which units are picked
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_movement:
                movementFragment movementObject = new movementFragment();
                pickFragment(movementObject);
                Toast.makeText(this,"pic movement type",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_units:
                unitsFragment unitsObject = new unitsFragment();
                pickFragment(unitsObject);
                Toast.makeText(this,"pick units",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        if (location != null){
            //location.setUserMetricUnits(this.useMetricUnits());
            location.setUserMetricUnits(units);
            nCurrentSpeed = location.getSpeed();
        }
        Formatter fat = new Formatter(new StringBuilder());
        fat.format(Locale.US,"%5.1f",nCurrentSpeed);
        String strCurrentSpeed = fat.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ','0');

        String strUnits = "MPH";
        //if (this.useMetricUnits()){
        if (units){
            strUnits = "KM/H";
        }
        displaySpeed.setText(strCurrentSpeed + " " + strUnits);
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
//methots for unitsFragment======================================
    private void ShowHideFragment(boolean sh){
        View hideFrag = findViewById(R.id.container);
        if (sh){
            hideFrag.setVisibility(View.VISIBLE);
        }else
            hideFrag.setVisibility(View.INVISIBLE);
    }
    boolean units;
    @Override
    public void milesButton(View view) {
        units = false;
        ShowHideFragment(false);
    }

    @Override
    public void kilometersButton(View view) {
        units = true;
        ShowHideFragment(false);
    }
    //methods for movementFragment================================
    String movementType = "car";
    @Override
    public void moveByCar(View view) {
        movementType = "car";
        ShowHideFragment(false);
    }

    @Override
    public void moveByBike(View view) {
        movementType = "bike";
        ShowHideFragment(false);
    }

    @Override
    public void moveOnFoot(View view) {
        movementType = "foot";
        ShowHideFragment(false);
    }


}
