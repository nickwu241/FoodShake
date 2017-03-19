package com.foodshake;

import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by chaneric on 2017-03-18.
 */

public class ParseYelp {
    private Map params;
    YelpAPI yelp = YelpObject.getInstance();

    public void getRest(JSONObject pref) throws JSONException {
        this.params = setPref(pref);

    }


    //params.put("term", "food");
    //params.put("limit", "3");
    //return:
    private Map<String, String> setPref(JSONObject inPref) throws JSONException {
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
    }


    private ArrayList<Business> getResult(Map params) throws IOException {
        Call<SearchResponse> call = yelp.search("",params);
        SearchResponse searchResponse = call.execute().body();
        ArrayList<Business> businesses = searchResponse.businesses();

        return businesses;
    }


}
