package com.foodshake;

import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class ReviewsParser {
    public static ArrayList<Review> getReviews(String businessID) throws IOException {
        Call<Reviews> call = YelpApiProvider.getYelpApi().getBusinessReviews(businessID, Constants.LOCALE);
        return call.execute().body().getReviews();
    }
}
