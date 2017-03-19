package com.foodshake;

import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import java.io.IOException;

public class YelpObject {
    private static YelpObject us = new YelpObject();
    private static YelpFusionApi instance;

    private YelpObject() {
        try {
            instance = new YelpFusionApiFactory().createAPI(BuildConfig.app_id, BuildConfig.app_secret);
        } catch (IOException e) {
            Log.e("EXCEPTION", e.getMessage());
        }
    }

    public static YelpFusionApi getYelpAPI() {
        return us.instance;
    }

}
