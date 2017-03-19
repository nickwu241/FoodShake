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

    public String id;   // the unique ID of the restaurant
    public String name; // the name of the restaurant
    public String imageURL; // the URL for the image associated with the restaurant
    public String URL;  // the Yelp URL
    public String price; // How much it cost: "$" means cheap "$$$$" means expensive
    public double rating;  // how high it is rated, decimals are possible.
    public int reviewCount; // review count
    public String phoneNumber; // the phone number for the business
    public ArrayList<String> arrayOfPhotos; // An array of links to the photos
    public ArrayList<Hour> hours; // the hours associated with the restaurant.  It is in a JSON Array.
    public Coordinates coordinates; // gets the Lat/Lon for the restaurant
    public Location location; // the location of the restaurant.  includes everything relevant to the location

    public Restaurant(Business businessObject) {
        parseData(businessObject);
    }

    private void parseData(Business businessObject) {
        this.id = businessObject.getId();
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
