package com.foodshake;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;

/**
 * Created by chaneric on 2017-03-18.
 */

public class YelpObject {
    private static YelpObject us = new YelpObject();
    private static YelpAPI instance;

    private YelpObject() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(BuildConfig.consumer_key,
                BuildConfig.consumer_secret,
                BuildConfig.token,
                BuildConfig.token_secret);
    }


    public static YelpAPI getInstance() {
        return us.instance;
    }

}
