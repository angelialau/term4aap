package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class newItinerary extends AppCompatActivity {
    String[] places = new String[3];
    String placePref;
    Spinner locationSpinner;
    boolean clicked = false;
    String choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);

        Intent intent = getIntent();
        final String placePref = intent.getStringExtra("PlacePref");

        places[0] = getString(R.string.hindu_temple);
        places[1] = getString(R.string.mosque);
        places[2] = getString(R.string.church);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(this, R.layout.activity_itinerary,places,getString(R.string.no_preference),placePref,getString(R.string.mosque));
        locationSpinner.setAdapter(customSpinnerAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!clicked){
                    choice = places[position];
                }else{
                    choice = placePref;
                    clicked = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }
}
