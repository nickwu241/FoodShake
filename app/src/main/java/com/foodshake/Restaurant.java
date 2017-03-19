package com.foodshake;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Coordinates;
import com.yelp.fusion.client.models.Hour;
import com.yelp.fusion.client.models.Location;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chaneric on 2017-03-18.
 */

public class Restaurant {

    private Restaurant(Business businessObject) {
        parseData(businessObject);
    }
    private String id;   // the unique ID of the restaurant
    private String name; // the name of the restaurant
    private String imageURL; // the URL for the image associated with the restaurant
    private String URL;  // the Yelp URL
    private String price; // How much it cost: "$" means cheap "$$$$" means expensive
    private double rating;  // how high it is rated, decimals are possible.
    private int reviewCount; // review count
    private String phoneNumber; // the phone number for the business
    private ArrayList<String> arrayOfPhotos; // An array of links to the photos
    private ArrayList<Hour> hours; // the hours associated with the restaurant.  It is in a JSON Array.
    private Coordinates coordinates; // gets the Lat/Lon for the restaurant
    private Location location; // the location of the restaurant.  includes everything relevant to the location


    private void parseData(Business businessObject) {
        final String id = businessObject.getId();
        this.name = businessObject.getName();
        this.imageURL = businessObject.getImageUrl();
        this.URL = businessObject.getUrl();
        this.price = businessObject.getPrice();
        this.rating = businessObject.getRating();
        this.reviewCount = businessObject.getReviewCount();
        this.phoneNumber = businessObject.getPhone();
        this.arrayOfPhotos = businessObject.getPhotos();
        this.hours = businessObject.getHours();
        this.coordinates = businessObject.getCoordinates();
        this.location = businessObject.getLocation();
    }




}
