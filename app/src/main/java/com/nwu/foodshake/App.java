package com.nwu.foodshake;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.nwu.yelpapi.*;
import com.nwu.yelpapi.pojo.Business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    //----------------------------------------------------------------------------------------------
    private static App instance;

    public final Gson gson = Debug.ON ? new Gson() : null;

    private final Context mContext;
    private final YelpRequestHandler mYelpRequestHandler;
    private final Navigator mNavigator;

    private Map<String, String> mDefaultSearchParams;
    private List<Business> mCachedBusinesses;

    //----------------------------------------------------------------------------------------------
    private App(Context context) {
        mContext = context;
        mYelpRequestHandler = new YelpRequestHandler(BuildConfig.client_id, BuildConfig.client_secret);
        mNavigator = new Navigator();

        // clear shared preferences on app initialization
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().clear().commit();
        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, true);
    }

    //----------------------------------------------------------------------------------------------
    public static App instance(Context context) {
        if (instance == null) {
            instance = new App(context);
        }
        return instance;
    }

    //----------------------------------------------------------------------------------------------
    public Map<String, String> getSearchParams() {
        if (mDefaultSearchParams == null) {
            mDefaultSearchParams = new HashMap<>();
            mDefaultSearchParams.put("term", "food");
            mDefaultSearchParams.put("limit", "50");
        }
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        Map<String, String> searchParams = new HashMap<>();
        searchParams.putAll(mDefaultSearchParams);
        searchParams.put(Constants.KEY_RADIUS, sp.getString("radius", "10000"));
        searchParams.put(Constants.KEY_PRICE, Util.join(sp.getStringSet("price", null), ","));

        final String cuisineType = Util.join(sp.getStringSet(Constants.KEY_CUISINE_TYPE, null), ",");
        if (!(cuisineType.equals("all") || cuisineType.equals(""))) {
            searchParams.put("categories", cuisineType);
        }

        searchParams.put("open_now", String.valueOf(sp.getBoolean(Constants.KEY_OPEN_NOW, true)));
        return searchParams;
    }

    //----------------------------------------------------------------------------------------------
    public YelpRequestHandler getYelpRequestHandler() {
        return  mYelpRequestHandler;
    }

    //----------------------------------------------------------------------------------------------
    public Navigator getNavigator() {
        return mNavigator;
    }

    //----------------------------------------------------------------------------------------------
    public void cacheBusinesses(List<Business> businesses) {
        mCachedBusinesses = businesses;
    }

    //----------------------------------------------------------------------------------------------
    public List<Business> getCachedBusinesses() {
        return mCachedBusinesses;
    }

    //----------------------------------------------------------------------------------------------
}
