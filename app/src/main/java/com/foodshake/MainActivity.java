package com.foodshake;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {

    public static final int MESSAGE_SEARCH_BUSSINESS_SUCCESS = 1;
    public static final int MESSAGE_SEARCH_REVIEWS_SUCCESS = 2;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private Toast mToast;
    private Vibrator mVibrator;

    private SearchBusinessTask mSearchBusinessTask;
    private SearchReviews mSearchReviewsTask;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SEARCH_BUSSINESS_SUCCESS:
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, RestaurantDB.restaurants.size());
                    boolean doneSelection = false;
                    Restaurant r = null;
                    while (!doneSelection) {
                        doneSelection = true;
                        r = RestaurantDB.restaurants.get(randomIndex);
                        if (r.price.length() > RestaurantDB.price) {
                            RestaurantDB.restaurants.remove(randomIndex);
                            doneSelection = false;
                        }
                    }

                    RestaurantDB.selectedRestaurant = r;
                    Log.i("RESTAURANT SELECTED", r.name);
                    if (mSearchReviewsTask == null || mSearchReviewsTask.getStatus() == AsyncTask.Status.RUNNING) {
                        mSearchReviewsTask = new SearchReviews(mHandler);
                        mSearchReviewsTask.execute();
                    }
                    break;

                case MESSAGE_SEARCH_REVIEWS_SUCCESS:
                    if (mVibrator != null) {
                        mVibrator.cancel();
                    }
                    Intent i = new Intent(getApplicationContext(), YourChoice.class);
                    startActivity(i);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            // shake detected
            if (mAccel > 12) {
                shakeAction();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void shakeAction() {
        if (mSearchBusinessTask == null || mSearchBusinessTask.getStatus() != AsyncTask.Status.RUNNING) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            try {
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                RestaurantDB.currentLocation = location;
            }
            catch (SecurityException e) {
                Log.e("EXCEPTION", e.getMessage());
            }

            HashMap pref = new HashMap<>();
            String userCategories = extractCategories();
            if (userCategories != null) {
                pref.put("type", userCategories);
            }
            pref.put("radius_filter", String.valueOf(RestaurantDB.radius));

            mVibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 1000 milliseconds
            long pattern[] = {50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
            mVibrator.vibrate(pattern, 0);

            mSearchBusinessTask = new SearchBusinessTask(pref, location, mHandler);
            mSearchBusinessTask.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private String extractCategories() {
        if (RestaurantDB.prefAllBox == null || RestaurantDB.prefAllBox.isChecked()) {
            return null;
        }

        String categories = "";
        for (int i = 0; i < RestaurantDB.prefGridCatagories.getChildCount(); i++) {
            if (((CheckBox)RestaurantDB.prefGridCatagories.getChildAt(i)).isChecked()) {
                categories += RestaurantDB.prefGridCatagories.getChildAt(i).getTag() + ",";
            }
        }
        return categories.substring(0, categories.length() - 1);
    }
}
