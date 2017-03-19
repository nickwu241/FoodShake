package com.foodshake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // max radius is capped at 400000
        RestaurantDB.prefRadius = (EditText) findViewById(R.id.radius);
        RestaurantDB.prefGridCatagories = (GridLayout) findViewById(R.id.grid_categories);
        RestaurantDB.prefPriceGroup = (RadioGroup) findViewById(R.id.price_group);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.reset_default:
                // reset all to default
                RestaurantDB.prefRadius.getText().clear();
                ((RadioButton) findViewById(R.id.price_4)).setChecked(true);
                ((CheckBox) findViewById(R.id.all)).setChecked(true);
                // now uncheck all other buttons
            case R.id.all:
                for (int i = 0; i < RestaurantDB.prefGridCatagories.getChildCount(); i++) {
                    ((CheckBox) RestaurantDB.prefGridCatagories.getChildAt(i)).setChecked(false);
                }
                break;
            default:
                // any other catagory types, uncheck all
                ((CheckBox) findViewById(R.id.all)).setChecked(false);
                break;
        }
    }
}
