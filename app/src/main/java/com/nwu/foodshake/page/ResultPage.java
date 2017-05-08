package com.nwu.foodshake.page;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwu.foodshake.R;
import com.nwu.foodshake.screen.ResultScreen;
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
    private TextView restaurantRating;
    private TextView restaurantAddress;
    private TextView restaurantPhone;
    private ImageView restaurantImage;

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
        restaurantRating = (TextView) mActivity.findViewById(R.id.restaurant_rating);
        restaurantAddress = (TextView) mActivity.findViewById(R.id.restaurant_address);
        restaurantPhone = (TextView) mActivity.findViewById(R.id.restaurant_phone);
        restaurantImage = (ImageView) mActivity.findViewById(R.id.restaurant_picture);
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
        restaurantRating.setText(String.valueOf(business.rating));
        restaurantAddress.setText(business.location.address1);
        restaurantPhone.setText(business.display_phone);
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
