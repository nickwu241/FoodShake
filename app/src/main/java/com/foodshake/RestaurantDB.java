package com.foodshake;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.SyncStateContract;


import java.util.ArrayList;

public class RestaurantDB {
    public static ArrayList<Restaurant> restaurants;
    public static Restaurant selectedRestaurant;
    public static Location currentLocation;
}
