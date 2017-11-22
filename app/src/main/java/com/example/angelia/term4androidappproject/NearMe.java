package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NearMe extends AppCompatActivity {
    String[] places = new String[4];
    Spinner placeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        places[0]=getString(R.string.hindu_temple);
        places[1] = getString(R.string.chinese_temple);
        places[2] = getString(R.string.mosque);
        places[3] = getString(R.string.church);

        Intent intent = getIntent();
        String placePref = intent.getStringExtra("PlacePref");
        placeSpinner = (Spinner) findViewById(R.id.placeSpinner);

        Log.i("kim",placePref);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.activity_near_me,places,getString(R.string.no_preference),placePref,getString(R.string.mosque));
        placeSpinner.setAdapter(customSpinnerAdapter);

        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.i("kim", places[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


}
