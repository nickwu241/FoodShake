package com.foodshake;

import android.location.Location;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class YelpParser {
    private Map<String, String> params = new HashMap<>();
    private YelpFusionApi yelpAPI = YelpObject.getYelpAPI();

    public ArrayList<Business> businessSearch(Map<String, String> userPreferences, Location loc) throws IOException {
        setUpParams(userPreferences, loc);
        Call<SearchResponse> call = yelpAPI.getBusinessSearch(params);
        SearchResponse searchResponse = call.execute().body();
        return searchResponse.getBusinesses();
    }

    private void setUpParams(Map<String, String> userPreferences, Location location) {
        // set order pref; 0 = best matched;
        params.put("sort", "0");

        // limit to 20 results
        params.put("limit", "20");

        // set terms : "food" "bars", NOT "food,bars"
        String foodType = userPreferences.get("type");
        if (foodType == "all") {
            params.put("term", "food");
        } else {
            params.put("term", foodType + " " + "food");
        }

        // set radius, default = 250000 meters, max = 400000 meters
        String prefRadius = userPreferences.get("radius");
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
