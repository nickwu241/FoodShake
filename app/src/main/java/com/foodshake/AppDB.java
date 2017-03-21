package com.foodshake;

import android.location.Location;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioGroup;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class AppDB {
    public static ArrayList<Restaurant> restaurants;
    public static Restaurant selectedRestaurant;
    public static Location currentLocation;

    public static ArrayList<Review> reviewsForSelected;

    public static EditText prefRadius;
    public static GridLayout prefGridCatagories;
    public static RadioGroup prefPriceGroup;
    public static CheckBox prefAllBox;

    public static int radius = 25000;
    public static int price = 4;
}
