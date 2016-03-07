package com.example.atitude6430.speed;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BaseGpsListener,unitsFragment.buttonClickListener, movementFragment.OnMovementListener,resultsFragment.OnResultsClosed,CNewResultDialog.OnNewResultListener {
    TextView displaySpeed; //wyswietlanie aktualnej predkosci
    ///////////////////////////////TESTOWANIE SHARED PREF
    float highestSpeed; // zmienna do wyswietlania predkosci z metody getSpeed
    SharedPreferences preferences;
    ArcProgress arcProgress;
    TextView maxSpeed;
    ProgressBar maxSpeedProgress;

    int test =0;
    public void test (View view){
        maxSpeedProgress.setProgress(test);
        arcProgress.setProgress(test);
        test+=5;
        maxSpeed.setText("45");
    }
    public void SaveResult(String name,float result){//zapisywanie highesSpeed wraz z nazwą uzytkownika
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(name, result);
        editor.commit();
    }
    public void pickFragment(Object object){//przełączanie miedzy fragmentami
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
        Typeface speed = Typeface.createFromAsset(getAssets(),"fonts/digital.ttf");
        displaySpeed.setTypeface(speed);
        displaySpeed.bringToFront();


        //for GPS location============================
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

        //for communication witrh units fragment
        units = true; //to check which units are picked

        preferences = MainActivity.this.getSharedPreferences("onFoot", Context.MODE_PRIVATE);// domyslnie zapisywanie wyników dla przemieszczania na piechote
        highestSpeed =0;

        arcProgress = (ArcProgress)findViewById(R.id.arc_progress);
        arcProgress.setMax(250);

        maxSpeed = (TextView) findViewById(R.id.textViewMaxSpeed);
        Typeface speedMax = Typeface.createFromAsset(getAssets(),"fonts/diediedie.regular.ttf");
        maxSpeed.setTypeface(speedMax);
        maxSpeed.bringToFront();
        maxSpeedProgress = (ProgressBar) findViewById(R.id.progressBar5);
        maxSpeedProgress.setProgress(1);

        ShowHideFragment(false);
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
            case R.id.menuRead:
                Bundle bundle = new Bundle();
                bundle.putString("pref",movementType);
                Log.d("what","is movement type???"+" "+movementType);
                resultsFragment res =  new resultsFragment();
                res.setArguments(bundle);
                pickFragment(res);
                break;
            case R.id.menuSave:
                DialogFragment newResultDialog = new CNewResultDialog();
                newResultDialog.show(getFragmentManager(),"tag1");
                //zrobic wpisywanie highest speed z nazwa uzytkowniaka
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        if (location != null){
            //location.setUserMetricUnits(this.useMetricUnits());
            location.setUserMetricUnits(units);
            nCurrentSpeed = location.getSpeed();
            UpdatingProgress(nCurrentSpeed);
            maxSpeedProgress.setProgress((int) nCurrentSpeed);
            maxSpeedProgress.setMax((int) highestSpeed);
            maxSpeed.setText("record: "+(int) highestSpeed);
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
        //displaySpeed.setText(strCurrentSpeed + " " + strUnits + " max speed: "+highestSpeed);
        displaySpeed.setText(strCurrentSpeed+" KM/H");
    }
    //int k =0;
   // int inc=0;
    //public void testButton(View view){
       // maxSpeedProgress.setProgress(k);
       // k+=inc;
       // maxSpeed.setText("currentPro:"+k+" / "+maxSpeedProgress.getMax());
    //}
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
    private void ShowHideFragment(boolean sh){//metoda do ukrywania kontenera z fragmentami
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
    String movementType = "byCar";
    @Override
    public void moveByCar(View view) {
        movementType = "byCar";
        maxSpeedProgress.setProgress(0);
        maxSpeedProgress.setMax(50);
        //inc=5;
       // k=0;
       // maxSpeed.setText("currentPro:" + maxSpeedProgress.getProgress() + " / " + maxSpeedProgress.getMax());
        preferences = MainActivity.this.getSharedPreferences("byCar", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }

    @Override
    public void moveByBike(View view) {
        movementType = "byBike";
        maxSpeedProgress.setProgress(0);
        maxSpeedProgress.setMax(10);
       //inc=3;
        //k=0;
        //maxSpeed.setText("currentPro:" + maxSpeedProgress.getProgress() + " / " + maxSpeedProgress.getMax());
        preferences = MainActivity.this.getSharedPreferences("byBike", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }

    @Override
    public void moveOnFoot(View view) {
        movementType = "onFoot";
        maxSpeedProgress.setProgress(0);
        maxSpeedProgress.setMax(2);
      //  inc=1;
       // k=0;
      //  maxSpeed.setText("currentPro:" + maxSpeedProgress.getProgress() + " / " + maxSpeedProgress.getMax());
        preferences = MainActivity.this.getSharedPreferences("onFoot", Context.MODE_PRIVATE);
        ShowHideFragment(false);
    }

    @Override
    public void ExitResults(View view) {
        ShowHideFragment(false);
    }

    @Override
    public void ClearResulst(View view) {
       // preferences = MainActivity.this.getSharedPreferences(movementType, Context.MODE_PRIVATE);
        SharedPreferences.Editor cleaner = preferences.edit();
        cleaner.clear();
        cleaner.commit();
        ShowHideFragment(false);
        Toast.makeText(this,"list cleared",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void NewResultSave(String userName) {
        SaveResult(userName,highestSpeed);
        Log.d("dodam", " " + userName + " his speed: " + highestSpeed);
    }

    public void UpdatingProgress(float speed){
        arcProgress.setProgress((int) speed);
    }
}
