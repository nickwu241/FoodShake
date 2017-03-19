package com.foodshake;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import java.io.IOException;

/**
 * Created by chaneric on 2017-03-18.
 */

public class YelpObject {
    private static YelpObject us = new YelpObject();
    private static YelpFusionApi instance;

    private YelpObject() {
        try {
            instance = new YelpFusionApiFactory().createAPI(BuildConfig.consumer_key, BuildConfig.consumer_secret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static YelpFusionApi getInstance() {
        return us.instance;
    }

}
