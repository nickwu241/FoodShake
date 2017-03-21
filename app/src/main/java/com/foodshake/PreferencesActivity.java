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
        AppDB.prefRadius = (EditText) findViewById(R.id.radius);
        AppDB.prefGridCatagories = (GridLayout) findViewById(R.id.grid_categories);
        AppDB.prefPriceGroup = (RadioGroup) findViewById(R.id.price_group);
        AppDB.prefAllBox = (CheckBox) findViewById(R.id.all);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.reset_default:
                // reset all to default
                AppDB.prefRadius.getText().clear();
                ((RadioButton) findViewById(R.id.price_4)).setChecked(true);
                AppDB.prefAllBox.setChecked(true);
                // now uncheck all other buttons
            case R.id.all:
                for (int i = 0; i < AppDB.prefGridCatagories.getChildCount(); i++) {
                    ((CheckBox) AppDB.prefGridCatagories.getChildAt(i)).setChecked(false);
                }
                break;
            default:
                // any other catagory types, uncheck 'all'
                ((CheckBox) findViewById(R.id.all)).setChecked(false);
                break;
        }
    }
    // TODO: Show previous state
    @Override
    protected void onResume() {
        super.onResume();
//        AppDB.prefRadius.getText().clear();
//        ((RadioButton) findViewById(R.id.price_4)).setChecked(true);
//        AppDB.prefAllBox.setChecked(true);
//        for (int i = 0; i < AppDB.prefGridCatagories.getChildCount(); i++) {
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AppDB.radius = Integer.parseInt(AppDB.prefRadius.getText().toString());
        }
        catch (NumberFormatException e) {
            // default 25000
            AppDB.radius = 25000;
        }

        RadioButton selectedPrice = ((RadioButton) findViewById(AppDB.prefPriceGroup.getCheckedRadioButtonId()));
        if (selectedPrice != null) {
            AppDB.price = selectedPrice.getText().length();
        }
        else {
            AppDB.price = 4;
        }
    }

}
