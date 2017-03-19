package com.foodshake;

import android.os.AsyncTask;
import android.os.Handler;

import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.util.ArrayList;

/**
 * Created by chaneric on 2017-03-19.
 */

public class SearchReviews extends AsyncTask<Void,Void,Void> {
    private Handler mHandler;


    public SearchReviews(Handler handler) {
        mHandler = handler;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        RestaurantDB.reviewsForSelected = RestaurantDB.getReviews(RestaurantDB.selectedRestaurant.businessObj);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mHandler.obtainMessage(MainActivity.MESSAGE_SEARCH_REVIEWS_SUCCESS).sendToTarget();
    }
}
