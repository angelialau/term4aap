package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.Utils.ItineraryCalculator;
import com.example.angelia.term4androidappproject.Utils.JsonProcessing;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

public class CalculateItineraryActivity extends AppCompatActivity {

    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_itinerary);

        result = findViewById(R.id.showResult);

        Intent intent = getIntent();
        ArrayList<String> locations = intent.getStringArrayListExtra(MainActivity.LOCATION_KEY);

        HashMap<String, LinkedTreeMap> footmap = JsonProcessing.hashMapify(R.raw.foot, this);
        HashMap<String, LinkedTreeMap> publicmap = JsonProcessing.hashMapify(R.raw.public_transport, this);
        HashMap<String, LinkedTreeMap> taximap = JsonProcessing.hashMapify(R.raw.taxi,this);

        HashMap<String,String> visited = new HashMap<>();
        ItineraryCalculator calculator = new ItineraryCalculator(footmap,publicmap,taximap);
        calculator.bruteForceCalculate(locations,visited,0,0,0,"Marina Bay Sands");

        Log.i("Calculate Itinerary", "onCreate: " + calculator.getBestTime());
        result.setText(calculator.getBestItinerary().toString());
    }
}
