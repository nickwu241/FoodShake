package com.foodshake;

import com.yelp.clientlib.entities.SearchResponse;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by chaneric on 2017-03-18.
 */

public class ParseYelp {
    private HashMap params;

    public void getRest(JSONObject pref){
        this.params = setPref(pref);

    }


    //params.put("term", "food");
    //params.put("limit", "3");
    //return:
    private HashMap setPref(JSONObject inPref){


        return null;
    }


    private Call<SearchResponse> getResult(HashMap params) {
        return null;
    }

}
