package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.Utils.ItineraryCalculator;
import com.example.angelia.term4androidappproject.Utils.JsonProcessing;
import com.example.angelia.term4androidappproject.Utils.ShortestPathItineraryCalculator;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CalculateItineraryActivity extends AppCompatActivity {

    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_itinerary);

        result = findViewById(R.id.showResult);

        Intent intent = getIntent();
        ArrayList<String> locations = intent.getStringArrayListExtra(MainActivity.LOCATION_KEY);
        double budget = 20.0;

        HashMap<String, LinkedTreeMap> footmap = JsonProcessing.hashMapify(R.raw.foot, this);
        HashMap<String, LinkedTreeMap> publicmap = JsonProcessing.hashMapify(R.raw.public_transport, this);
        HashMap<String, LinkedTreeMap> taximap = JsonProcessing.hashMapify(R.raw.taxi,this);

        LinkedHashMap<String,String> visited = new LinkedHashMap<>();
        ItineraryCalculator calculator = new ItineraryCalculator(footmap,publicmap,taximap);
        calculator.bruteForceCalculate(locations,visited,0,0,0,"Marina Bay Sands");
        String bruteforceResult = calculator.getBestItinerary().toString();
        String bruteforceTime = String.valueOf(calculator.getBestTime());

        ShortestPathItineraryCalculator spCalculator = new ShortestPathItineraryCalculator(footmap,publicmap,taximap);
        spCalculator.spItineraryCalculator(locations, budget);
        String nnResult = spCalculator.getSpBestItinerary().toString();
        String nnTime = String.valueOf(spCalculator.getTotalTimeNeeded());

        String itineraries = String.format("using brute force: %s (time = %s), using greedy approach: %s (time = %s)",
                bruteforceResult, bruteforceTime, nnResult, nnTime);
        result.setText(itineraries);
        Log.i("Angelia", "nnResult = " +nnResult);
        Log.i("Angelia", "nnTime = " +nnTime);
    }
}
