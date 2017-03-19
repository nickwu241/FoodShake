package com.foodshake;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class SearchBusinessTask extends AsyncTask<Void, Void, ArrayList<Restaurant>> {
    private Map<String, String> mPreferences;
    private Location mLocaction;

    public SearchBusinessTask(Map<String, String> preferences, Location loc) {
        mPreferences = preferences;
        mLocaction = loc;
    }

    @Override
    protected ArrayList<Restaurant> doInBackground(Void... v) {
        try {
            return new YelpParser().businessSearch(mPreferences, mLocaction);
        } catch (IOException e) {
            Log.e("EXCEPTION", e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Restaurant> restaurants) {
        RestaurantDB.restaurants = restaurants;
    }
}