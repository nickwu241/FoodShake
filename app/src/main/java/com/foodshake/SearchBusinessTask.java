package com.foodshake;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.internal.Util;

public class SearchBusinessTask extends AsyncTask<Void, Void, Void> {
    private Map<String, String> mPreferences;
    private Location mLocaction;
    private Handler mHandler;

    public SearchBusinessTask(Map<String, String> preferences, Location loc, Handler handler) {
        mPreferences = preferences;
        mLocaction = loc;
        mHandler = handler;
    }

    @Override
    protected Void doInBackground(Void... v) {
        try {
            AppDB.restaurants = new YelpParser().businessSearch(mPreferences, mLocaction);
        }
        catch (IOException e) {
            Log.e(Constants.TAG_EXCEPTION, e.getMessage());
            AppDB.restaurants = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mHandler.obtainMessage(Constants.MESSAGE_SEARCH_BUSSINESS_SUCCESS).sendToTarget();
    }
}