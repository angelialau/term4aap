package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.angelia.term4androidappproject.Adapters.EditItineraryAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class EditItineraryActivity extends AppCompatActivity {

    Button buttonDoneItinerary;
    Button buttonAddMoreLocation;

    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerShowLocations;
    EditItineraryAdapter editItineraryAdapter;

    String dateFromIntent;
    ArrayList<String> locationsFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);

        // Getting data from extras
        Intent incoming = getIntent();
        dateFromIntent = incoming.getStringExtra(MainActivity.DATE_KEY);
        locationsFromIntent = incoming.getStringArrayListExtra(MainActivity.LOCATION_KEY);

        // Wiring up all my views
        buttonDoneItinerary = findViewById(R.id.buttonDoneWithItinerary);
        buttonAddMoreLocation = findViewById(R.id.buttonAddMoreLocation);
        recyclerShowLocations = findViewById(R.id.recyclerShowLocations);

        // Setting up recycler view
        if (!dateFromIntent.isEmpty() || !locationsFromIntent.isEmpty()){
            // use a linear layout manager
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerShowLocations.setLayoutManager(linearLayoutManager);

            editItineraryAdapter = new EditItineraryAdapter(locationsFromIntent);
            recyclerShowLocations.setAdapter(editItineraryAdapter);
        }

        // Setting on click listeners
        buttonDoneItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonAddMoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),NewItineraryActivity.class);
                startActivity(intent);
            }
        });
    }
}
