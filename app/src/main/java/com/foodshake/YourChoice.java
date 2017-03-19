package com.foodshake;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yelp.fusion.client.models.Review;
import com.yelp.fusion.client.models.Reviews;

import java.util.ArrayList;

public class YourChoice extends AppCompatActivity {
    public static TextView textViewObj;
    public static TextView categoryObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Price: " + RestaurantDB.selectedRestaurant.price);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewObj = (TextView) findViewById(R.id.restaurant_name);
        textViewObj.setText(RestaurantDB.selectedRestaurant.name);

        categoryObj = (TextView) findViewById(R.id.category);
        categoryObj.setText(RestaurantDB.selectedRestaurant.categories.get(0).getTitle());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating((float) RestaurantDB.selectedRestaurant.rating);

        setReviews();
    }

    public void onDirectionsClick(View view) {
        String result = RestaurantDB.selectedRestaurant.id;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                .parse("http://maps.google.com/?q=" + result)));
    }

    public void onCallClick(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL).setData(
                Uri.parse("tel:" + RestaurantDB.selectedRestaurant.phoneNumber.trim())));
    }

    public void onInfoClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(RestaurantDB.selectedRestaurant.URL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            getApplication().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            getApplication().startActivity(intent);
        }
    }

    public void setReviews() {

        // first review
        TextView personReview = (TextView) findViewById(R.id.author);
        TextView body = (TextView) findViewById(R.id.body1);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        Review current = RestaurantDB.reviewsForSelected.get(0);
        personReview.setText(current.getUser().getName());
        body.setText(current.getText());
        ratingBar.setRating((float) current.getRating());

        // second review
        TextView personReview2 = (TextView) findViewById(R.id.author2);
        TextView body2 = (TextView) findViewById(R.id.body2);
        RatingBar ratingBar2 = (RatingBar) findViewById(R.id.ratingBar3);
        Review current2 = RestaurantDB.reviewsForSelected.get(1);
        personReview2.setText(current2.getUser().getName());
        body2.setText(current2.getText());
        ratingBar2.setRating((float) current2.getRating());

        // third review
        TextView personReview3 = (TextView) findViewById(R.id.author3);
        TextView body3 = (TextView) findViewById(R.id.body3);
        RatingBar ratingBar3 = (RatingBar) findViewById(R.id.ratingBar4);
        Review current3 = RestaurantDB.reviewsForSelected.get(2);
        personReview3.setText(current3.getUser().getName());
        body3.setText(current3.getText());
        ratingBar3.setRating((float) current3.getRating());




        
    }



}
