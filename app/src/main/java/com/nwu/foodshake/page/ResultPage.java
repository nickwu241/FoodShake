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
        restaurantName.setText(screen.name);
        restaurantCategories.setText(screen.categories);
        restaurantPrice.setText(screen.price);
        restaurantAddress.setText(screen.address);
        restaurantPhone.setText(screen.phone);
        restaurantNumReviews.setText(screen.numReviews);
        restaurantImage.setImageBitmap(screen.image);
        restaurantStars.setImageResource(screen.stars);
    }

    //----------------------------------------------------------------------------------------------
}
