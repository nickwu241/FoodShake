package com.nwu.foodshake;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nwu.foodshake.screen.ResultScreen;
import com.nwu.foodshake.screen.Screen;
import com.nwu.yelpapi.pojo.Business;
import com.nwu.yelpapi.pojo.SearchResponse;

import java.lang.ref.WeakReference;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Screen {
    //----------------------------------------------------------------------------------------------
    private static final String TAG = "MainActivity";

    private App mApp;
    private YelpRequestHandler mYelp;

    private SensorManager mSensorManager;
    private ShakeDetector mShakeDetector;
    private Sensor mAccelerometer;
    private Vibrator mVibrator;

    private LocationManager mLocationManager;
    private Location mLastLocation;

    private ResultScreen mResultScreen;

    //----------------------------------------------------------------------------------------------
    @Override
    public void showPage(Activity activity) {
        setContentView(R.layout.activity_main);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Debug.ON) Log.i(TAG, "onCreate");

        mApp = App.instance(getApplicationContext());
        mYelp = mApp.getYelpRequestHandler();
        mYelp.setHandler(new ResponseHandler(this));

        // detecting shake
        mSensorManager = ((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                sendRequest(null);
            }
        });

        // vibration feedback while sending yelp requests
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        // check for location permissions
        final int fineLocPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        final int coarseLocPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        final boolean fineLocGranted = fineLocPerm == PackageManager.PERMISSION_GRANTED;
        final boolean coarseLocGranted = coarseLocPerm == PackageManager.PERMISSION_GRANTED;

        if (!(fineLocGranted || coarseLocGranted)) {
            if (Debug.ON) Log.e(TAG, "No Location Permissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSION_FINE_LOCATION);
        }

        // try to get location asap
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    if (Debug.ON) Log.i(TAG, "Location Changed!");
                    mLastLocation = location;
                    mLocationManager.removeUpdates(this);
                }
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

        mApp.getNavigator().setUp(this);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        if (Debug.ON) Log.i(TAG, "onResume");
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mApp.getNavigator().show();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onPause() {
        if (Debug.ON) Log.i(TAG, "onPause");
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
        mVibrator.cancel();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        mApp.getNavigator().back();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSION_FINE_LOCATION:
                if (Debug.ON) {
                    // if request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG, "GRANTED: Location Permissions");
                        return;
                    }
                }
                if (Debug.ON) Log.e(TAG, "DENIED: Location Permissions");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSION_FINE_LOCATION);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    public void goToSettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    //----------------------------------------------------------------------------------------------
    // TODO: remove View parameter once done testing
    public void sendRequest(View view) {
        // only send Yelp API requests one at a time
        if (mYelp.getStatus() != YelpRequestHandler.Status.STANDBY) {
            // try again if we failed to authenticate the first time around
            if (mYelp.getStatus() == YelpRequestHandler.Status.NEEDS_AUTHENTICATION) {
                mYelp.authenticate();
            }
            // if we reach here, we're already sending a request
            return;
        }

        // check if we have a fixed location
        if (mLastLocation == null) {
            // if not, try to get the last one
            try {
                mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            catch (SecurityException e){
                // should never get here
                if (Debug.ON) Log.e(TAG, "Security Exception finding location", e);
                return;
            }

            // still no location :(
            if (mLastLocation == null) {
                handleError(Constants.ERROR_LOCATION);
                return;
            }
        }

        // setup our parameters for yelp search api
        Map<String, String> params = mApp.getSearchParams();
        params.put("latitude", String.valueOf(mLastLocation.getLatitude()));
        params.put("longitude", String.valueOf(mLastLocation.getLongitude()));

        if (Debug.ON) {
            Log.i(TAG, "Sending Search Request...");
            String paramString = "";
            for (Map.Entry<String, String> e : params.entrySet()) {
                paramString += e.getKey() + ':' + e.getValue() + '\n';
            }
            Log.i(TAG, paramString);
        }

        mVibrator.vibrate(Constants.VIBRATION_PATTERN_USER_FEEDBACK, 0);
        // hope for the best
        mYelp.search(params);
    }

    //----------------------------------------------------------------------------------------------
    private void handleSearchResponse(SearchResponse searchResponse) {
        if (Debug.ON) Log.i(TAG, mApp.gson.toJson(searchResponse));

        Business randomBusiness = Util.getRandom(searchResponse.businesses);
        if (randomBusiness == null) {
            handleError(Constants.ERROR_NO_RESTAURANT);
            return;
        }
        mYelp.business(randomBusiness.id);
    }

    //----------------------------------------------------------------------------------------------
    private void handleBusinessResponse(Business business) {
        if (Debug.ON) Log.i(TAG, mApp.gson.toJson(business));

        // theoretically, shouldn't fail since we should have given a valid business id
        if (business == null) {
            if (Debug.ON) Log.e(TAG, "failed to retrieve a business... <SHOULDN'T GET HERE>");
            return;
        }

        if (mResultScreen == null) {
            mResultScreen = new ResultScreen();
        }
        mResultScreen.setBusiness(business);
        mVibrator.cancel();
        mApp.getNavigator().goTo(mResultScreen);
    }

    //----------------------------------------------------------------------------------------------
    private void handleError(int errorCode) {
        String errorString;
        switch (errorCode) {
            case Constants.ERROR_NETWORK:
                errorString = Constants.ES_NETWORK;
                break;
            case Constants.ERROR_LOCATION:
                errorString = Constants.ES_LOCATION;
                break;
            case Constants.ERROR_NO_RESTAURANT:
                errorString = Constants.ES_NO_RESTAURANT;
                break;
            default:
                if (Debug.ON) Log.e(TAG, "Unimplemented error code: " + errorCode);
                return;
        }
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        if (Debug.ON) Log.e(TAG, errorString);
        // cancel vibration
        mVibrator.cancel();
    }

    //----------------------------------------------------------------------------------------------
    private static class ResponseHandler extends Handler {
        private final WeakReference<MainActivity> mMainActivity;

        private ResponseHandler(MainActivity mainActivity) {
            mMainActivity = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity main = mMainActivity.get();
            switch (msg.what) {
                case Constants.RESPONSE_SEARCH:
                    main.handleSearchResponse((SearchResponse) msg.obj);
                    break;

                case Constants.RESPONSE_BUSINESS:
                    main.handleBusinessResponse((Business) msg.obj);
                    break;

                case Constants.RESPONSE_FAILED:
                case Constants.RESPONSE_ERROR:
                    main.handleError(Constants.ERROR_NETWORK);
                    break;

                case Constants.RESPONSE_AUTHENTICATION:
                    if (Debug.ON) Log.i(TAG, "Authenticated Yelp API");
                    break;

                default:
                    if (Debug.ON) Log.e(TAG, "Message handling not implemented: " + msg.what);
                    break;
            }
        }
    } // <END CLASS> ResponseHandler

    //----------------------------------------------------------------------------------------------
}
