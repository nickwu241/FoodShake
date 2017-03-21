package com.foodshake;

import android.location.Location;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class YelpParser {
    private static final String LIMIT = "50";
    private Map<String, String> params = new HashMap<>();

    public ArrayList<Restaurant> businessSearch(Map<String, String> userPreferences, Location loc) throws IOException {
        setUpParams(userPreferences, loc);

        Call<SearchResponse> call = YelpApiProvider.getYelpApi().getBusinessSearch(params);
        SearchResponse searchResponse = call.execute().body();

        ArrayList<Restaurant> restaurants = new ArrayList<>();
        String types = userPreferences.get("type");

        for (Business b : searchResponse.getBusinesses()) {
            if (types != null) {
                for (Category c : b.getCategories()) {
                    if (types.contains(c.getAlias())) {
                        restaurants.add(new Restaurant(b));
                    }
                }
            }
            else {
                restaurants.add(new Restaurant(b));
            }
        }
        return restaurants;
    }

    private void setUpParams(Map<String, String> userPreferences, Location location) {
        // set order pref; 0 = best matched;
        params.put("sort", "0");

        // limit to 20 results
        params.put("limit", LIMIT);

        // set terms : "food" "bars", NOT "food,bars"
        params.put("term", "food");

        String foodType = userPreferences.get("type");
        if (foodType != null) {
            params.put("type", foodType);
        }

        // set radius, default = 250000 meters, max = 400000 meters
        String prefRadius = userPreferences.get("radius_filter");
        params.put("radius_filter", prefRadius);

        // set location
        double lat, lon;
        if (location == null) {
            // if no location then set the default location to Vancouver
            lon = -123.120738;
            lat = 49.282729;
        }
        else {
             lon = location.getLongitude();
             lat = location.getLatitude();
        }
        params.put("latitude", Double.toString(lat));
        params.put("longitude", Double.toString(lon));
    }
}
