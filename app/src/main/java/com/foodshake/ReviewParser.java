package com.foodshake;

import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class ReviewParser {
    private static final String LOCALE = "en_CA";

    public static ArrayList<Review> getReviews(String businessID) throws IOException {
        Call<Reviews> call = YelpObject.getYelpAPI().getBusinessReviews(businessID, LOCALE);
        return call.execute().body().getReviews();
    }
}
