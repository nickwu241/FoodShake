package com.foodshake;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;
import retrofit2.Call;

/**
 * Created by chaneric on 2017-03-18.
 */

public class ParseYelp {
    private Map<String, String> params = new HashMap<>();
    YelpFusionApi yelp = YelpObject.getInstance();

    public void getRest(JSONObject pref) throws JSONException, SecurityException{
        params = setPref(pref);

    }


    //params.put("term", "food");
    //params.put("limit", "3");
    //return:
    private Map<String, String> setPref(JSONObject inPref) throws JSONException, SecurityException{
        // set order pref; 0 = best matched;
        params.put("sort", "0");

        // set terms : "food" "bars", NOT "food,bars"
        String foodType = inPref.getString("type");
        if (foodType == "all") {
            params.put("term", "food");
        } else {
            String temp;
            temp = foodType + " " + "food";
            params.put("term", temp);
        }

        // set radius
        String prefRadius = inPref.getString("radius");
        params.put("radius_filter", prefRadius);
        return params;


        // set location
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lat, lon;
        if(location == null){
             lon = -123.120738;  // if no location then set the default location to Vancouver
             lat = 49.282729;
        }
        else {
             lon = location.getLongitude();
             lat = location.getLatitude();
        }
        params.put("latitude", Double.toString(lat));
        params.put("longitude", Double.toString(lon));
    }


    private ArrayList<Business> getResult(Map params) throws IOException {
        Call<SearchResponse> call = yelp.getBusinessSearch(params);
        SearchResponse searchResponse = call.execute().body();
        ArrayList<Business> businesses = searchResponse.getBusinesses();

        return businesses;
    }


}
