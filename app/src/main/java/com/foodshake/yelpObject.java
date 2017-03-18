package com.foodshake;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;

/**
 * Created by chaneric on 2017-03-18.
 */

public class yelpObject {
    private static YelpAPI instance;




    private yelpObject() {
        YelpAPIFactory apiFactory = new YelpAPIFactory();
    }


    public static YelpAPI getInstance() {
        return instance;
    }



}
