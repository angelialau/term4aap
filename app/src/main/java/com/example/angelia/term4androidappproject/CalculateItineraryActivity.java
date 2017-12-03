package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.angelia.term4androidappproject.Adapters.CalculatedItineraryAdapter;
import com.example.angelia.term4androidappproject.Models.ItineraryHolder;
import com.example.angelia.term4androidappproject.Utils.ItineraryCalculator;
import com.example.angelia.term4androidappproject.Utils.JsonProcessing;
import com.example.angelia.term4androidappproject.Utils.ShortestPathItineraryCalculator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.LinkedTreeMap;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CalculateItineraryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button buttonReturnToMain;
    ProgressBar loading;

    //For calculation
    ItineraryCalculator itineraryCalculator;
    ShortestPathItineraryCalculator shortestPathItineraryCalculator;
    String typeOfCalculation, date;
    HashMap<String, LinkedTreeMap> footmap,taximap,publicmap;
    ArrayList<String> locations = new ArrayList<String>();
    LinkedHashMap<String,String> visited;
    double budget = 20.0;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itineraryDatabaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String UID;
    private ItineraryHolder itineraryHolder;

    private String TAG = "CalculateItineraryActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_itinerary);

        // Finding my views
        recyclerView = findViewById(R.id.calulatedRecycler);
        buttonReturnToMain = findViewById(R.id.buttonReturnToMain);
        loading = findViewById(R.id.loadingPanel);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting data required for calculating Itinerary
        final Intent intent = getIntent();
        locations = intent.getStringArrayListExtra(MainActivity.LOCATION_KEY);
        typeOfCalculation = intent.getStringExtra("Type");
        Log.i(TAG,"message"  + typeOfCalculation);
        Log.i(TAG, locations.toString());

        date = intent.getStringExtra(MainActivity.DATE_KEY);

        footmap = JsonProcessing.hashMapify(R.raw.foot, this);
        publicmap = JsonProcessing.hashMapify(R.raw.public_transport, this);
        taximap = JsonProcessing.hashMapify(R.raw.taxi,this);

        visited = new LinkedHashMap<>();

        // Setting up Firebase to connect to
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        UID = firebaseAuth.getCurrentUser().getUid();
        itineraryDatabaseReference = firebaseDatabase.getReference().child(UID);

        // Setting buttons
        buttonReturnToMain.setEnabled(false);
        Calculation calculation = new Calculation();
        calculation.execute();
        // Set button to return to MainActivity
        buttonReturnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(),MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            }
        });
    }

    public class Calculation extends AsyncTask<String,Integer, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            Log.i(TAG, "calculation");
            if(typeOfCalculation.equals(EditItineraryActivity.APPROX)) {
                shortestPathItineraryCalculator = new ShortestPathItineraryCalculator(footmap, publicmap, taximap);
                long startTime = System.nanoTime();
                shortestPathItineraryCalculator.spItineraryCalculator(locations, budget);
                long endTime = System.nanoTime();

                long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                Log.i("results", "result: " + duration);
            } else {
                itineraryCalculator = new ItineraryCalculator(footmap,publicmap,taximap, 20);

                Long startTime = System.nanoTime();
                itineraryCalculator.bruteForceCalculate(locations,visited,0,0,0,"Marina Bay Sands");
                Long endTime = System.nanoTime();
                Double duration = (endTime - startTime)/1000000.0;

                Log.i("Calculate Itinerary", "onCreate: running time of calculation in milliseconds = " + duration);
                Log.i("Calculate Itinerary", "onCreate: " + itineraryCalculator.getBestTime());

            }
            return null;
        }


        protected void onPostExecute(Integer integer) {
            Log.i(TAG,"done");
            if(typeOfCalculation.equals(EditItineraryActivity.BRUTE)){
                // Putting calculated itinerary on recyclerview
                recyclerView.setAdapter(new CalculatedItineraryAdapter(itineraryCalculator.getBestItinerary())); //rayson calculator

                // Sending data to Firebase
                itineraryHolder = new ItineraryHolder(date, itineraryCalculator.getBestItinerary()); //raysons calculator
            }
            else{
                // Putting calculated itinerary on recyclerview
                recyclerView.setAdapter(new CalculatedItineraryAdapter(shortestPathItineraryCalculator.getSpBestItinerary()));

                // Sending data to Firebase
                itineraryHolder = new ItineraryHolder(date, shortestPathItineraryCalculator.getSpBestItinerary());

            }
            itineraryDatabaseReference.push().setValue(itineraryHolder);
            buttonReturnToMain.setEnabled(true);
            loading.setVisibility(View.GONE);
        }
    }

}
