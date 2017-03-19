package com.foodshake;

import android.location.Location;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class RestaurantDB {
    public static ArrayList<Restaurant> restaurants;
    public static Restaurant selectedRestaurant;
    public static Location currentLocation;

    public static EditText prefRadius;
    public static GridLayout prefGridCatagories;
    public static RadioGroup prefPriceGroup;
}
