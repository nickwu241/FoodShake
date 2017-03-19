package com.foodshake;

import android.Manifest;
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

public class YourChoice extends AppCompatActivity {
    public static TextView textViewObj;
    public static TextView categoryObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}
