package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewItineraryActivity extends AppCompatActivity {

    String[] hotels = new String[3];

    EditText editTextItineraryDate;
    Spinner spinnerItineraryHotel;
    AutoCompleteTextView autoCompleteItinerarySearch;

    Button buttonContinue;
    Button buttonAddLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);

        hotels[0] = "Marina Bay Sands";
        hotels[1] = "One Fullerton";
        hotels[2] = "Raffles Hotel";

        editTextItineraryDate = findViewById(R.id.editTextItineraryDate);
        spinnerItineraryHotel = findViewById(R.id.spinnerItineraryHotel);
        autoCompleteItinerarySearch = findViewById(R.id.autoCompleteItinerarySearch);

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonAddLocation = findViewById(R.id.buttonAddLocation);

        CustomSpinnerAdapter hotelSpinnerAdapter = new CustomSpinnerAdapter(this,
                                                                        R.layout.activity_new_itinerary,
                                                                        hotels,
                                                                        "Choose a hotel",
                                                                        hotels[0],
                                                                        hotels[0]);

        spinnerItineraryHotel.setAdapter(hotelSpinnerAdapter);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EditItineraryActivity.class);
                startActivity(intent);
            }
        });
    }
}
