package com.nwu.foodshake.page;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwu.foodshake.R;
import com.nwu.foodshake.screen.ResultScreen;
import com.nwu.foodshake.screen.Screen;
import com.nwu.yelpapi.pojo.Business;
import com.nwu.yelpapi.pojo.Category;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ResultPage implements Page {
    //----------------------------------------------------------------------------------------------
    private Activity mActivity;
    private ResultScreen mScreen;

    private TextView restaurantName;
    private TextView restaurantCategories;
    private TextView restaurantPrice;
    private TextView restaurantAddress;
    private TextView restaurantPhone;
    private TextView restaurantNumReviews;
    private ImageView restaurantImage;
    private ImageView restaurantStars;

    //----------------------------------------------------------------------------------------------
    public ResultPage(Activity activity, ResultScreen screen) {
        mActivity = activity;
        mScreen = screen;
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
        mActivity.setContentView(R.layout.display_restaurant);
        restaurantName = (TextView) mActivity.findViewById(R.id.restaurant_name);
        restaurantCategories = (TextView) mActivity.findViewById(R.id.restaurant_categories);
        restaurantPrice = (TextView) mActivity.findViewById(R.id.restaurant_price);
        restaurantAddress = (TextView) mActivity.findViewById(R.id.restaurant_address);
        restaurantPhone = (TextView) mActivity.findViewById(R.id.restaurant_phone);
        restaurantNumReviews = (TextView) mActivity.findViewById(R.id.restaurant_num_reviews);
        restaurantImage = (ImageView) mActivity.findViewById(R.id.restaurant_picture);
        restaurantStars = (ImageView) mActivity.findViewById(R.id.restaurant_stars);
        populateView(mScreen);
    }

    //----------------------------------------------------------------------------------------------
    private void populateView(ResultScreen screen) {
        Business business = screen.getBusiness();

        new GetImageTask().execute(business.image_url);

        restaurantName.setText(business.name);

        // TODO: repeated join logic here
        String displayCat = "";
        for (Category c : business.categories) {
            displayCat += c.title + ", ";
        }
        displayCat = displayCat.length() > 0 ?
                displayCat.substring(0, displayCat.length() - 2) : "";
        restaurantCategories.setText(displayCat);

        restaurantPrice.setText(business.price);
        restaurantAddress.setText(business.location.address1);
        restaurantPhone.setText(business.display_phone);
        restaurantNumReviews.setText("based on " + business.review_count +
                (business.review_count == 1 ? " review" : " reviews"));
        restaurantStars.setImageResource(getResourceFromRating(business.rating));
    }

    //----------------------------------------------------------------------------------------------
    private int getResourceFromRating(double rating) {
        final int flooredRating = (int) rating;
        final boolean whole = flooredRating == rating;
        switch (flooredRating) {
            case 0: return R.drawable.stars_small_0;
            case 1: return whole ? R.drawable.stars_small_1 : R.drawable.stars_small_1_half;
            case 2: return whole ? R.drawable.stars_small_2 : R.drawable.stars_small_2_half;
            case 3: return whole ? R.drawable.stars_small_3 : R.drawable.stars_small_3_half;
            case 4: return whole ? R.drawable.stars_small_4 : R.drawable.stars_small_4_half;
            case 5: return R.drawable.stars_small_5;
            default: throw new IllegalArgumentException("Invalid Rating: " + rating);
        }
    }

    //----------------------------------------------------------------------------------------------
    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                return BitmapFactory.decodeStream(new URL(url).openStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            restaurantImage.setImageBitmap(result);
        }
    }

    //----------------------------------------------------------------------------------------------
}
