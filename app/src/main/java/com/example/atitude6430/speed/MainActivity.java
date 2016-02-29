package com.example.atitude6430.speed;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements BaseGpsListener,unitsFragment.buttonClickListener, movementFragment.OnMovementListener {
    TextView displaySpeed;
    ///////////////////////////////TESTOWANIE SHARED PREF
    float highestSpeed;
    SharedPreferences preferences;
    EditText key;
    EditText value;

    public void SaveResult(String name,float result){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(name, result);
        editor.commit();
    }

    TextView results;

    public void ShowResults(){//to bedzie kolejny fragment wkladany do kontenera !!!!!!
        int i =0;
        StringBuilder shared =  new StringBuilder();
        Map<String,?> keys = sortResults(); //preferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
           //ograniczyć ilość wyswietlen np do 10:)
           shared.append(entry.getKey().toString() + " " + entry.getValue().toString() +" KM/H"+ "\n");
            i++;
        }
        results.setText(shared + "number of values: " + i);
    }

    public Map<String,?> sortResults(){
        Map<String, Float> unsortMap = (Map<String, Float>) preferences.getAll();

        List<Map.Entry<String,Float>> list = new LinkedList<Map.Entry<String,Float>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> lhs, Map.Entry<String, Float> rhs) {
                return rhs.getValue().compareTo(lhs.getValue());
            }
        });
        Map<String, Object> sortedMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String,Float>> it = list.iterator(); it.hasNext();){
            Map.Entry<String,?>entry = it.next();
            sortedMap.put(entry.getKey().toLowerCase(),entry.getValue());
        }
        StringBuilder buildResult = new StringBuilder();
        Map<String,?> map = sortedMap;
        for (Map.Entry<String,?>entry: map.entrySet()){
            Log.d("posortowane"," "+entry.getKey().toString()+" "+entry.getValue().toString());
            buildResult.append(entry.getKey().toString()+" "+entry.getValue().toString()+"\n");
        }
        results.setText(buildResult);
        return map;
    }

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

        preferences = MainActivity.this.getSharedPreferences("onFoot", Context.MODE_PRIVATE);
        results = (TextView) findViewById(R.id.textView3);
        highestSpeed =0;
        value = (EditText)findViewById(R.id.editText);
        key = (EditText) findViewById(R.id.editText2);
        //checkResult();
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
                highestSpeed =0;
                Toast.makeText(this,"pic movement type",Toast.LENGTH_SHORT).show();
                break;
           /* case R.id.action_units: //gdyby miały powstac mile na godzine
                unitsFragment unitsObject = new unitsFragment();
                pickFragment(unitsObject);
                highestSpeed =0;
                Toast.makeText(this,"pick units",Toast.LENGTH_SHORT).show();
                break;
                */
            case R.id.menuRead:
                ShowResults();
                //sortResults();
                break;
            case R.id.menuSave:

                SaveResult(key.getText().toString(),Float.parseFloat(value.getText().toString()));
                //sortResults();
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

        //update highestSpeed==================================
        if (nCurrentSpeed>highestSpeed)
            highestSpeed=nCurrentSpeed;


        String strUnits = "KM/H";//String strUnits = "MPH"; //na wypadek przyszłych zmian będzie można zrobić w milach na godzinę
        //if (this.useMetricUnits()){
        if (units){
            strUnits = "KM/H";
        }
        displaySpeed.setText(strCurrentSpeed + " " + strUnits + " max speed: "+highestSpeed);
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
        preferences = MainActivity.this.getSharedPreferences("byCar", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }

    @Override
    public void moveByBike(View view) {
        movementType = "bike";
        preferences = MainActivity.this.getSharedPreferences("byBike", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }

    @Override
    public void moveOnFoot(View view) {
        movementType = "foot";
        preferences = MainActivity.this.getSharedPreferences("onFoot", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }
}
