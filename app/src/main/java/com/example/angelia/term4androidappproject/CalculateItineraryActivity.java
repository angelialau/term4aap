package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.Adapters.CalculatedItineraryAdapter;
import com.example.angelia.term4androidappproject.Models.ItineraryHolder;
import com.example.angelia.term4androidappproject.Utils.ItineraryCalculator;
import com.example.angelia.term4androidappproject.Utils.JsonProcessing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.LinkedTreeMap;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CalculateItineraryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button buttonReturnToMain;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itineraryDatabaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String UID;
    private ItineraryHolder itineraryHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_itinerary);

        // Finding my views
        recyclerView = findViewById(R.id.calulatedRecycler);
        buttonReturnToMain = findViewById(R.id.buttonReturnToMain);

        // Calculating Itinerary
        final Intent intent = getIntent();
        ArrayList<String> locations = intent.getStringArrayListExtra(MainActivity.LOCATION_KEY);
        String date = intent.getStringExtra(MainActivity.DATE_KEY);

        HashMap<String, LinkedTreeMap> footmap = JsonProcessing.hashMapify(R.raw.foot, this);
        HashMap<String, LinkedTreeMap> publicmap = JsonProcessing.hashMapify(R.raw.public_transport, this);
        HashMap<String, LinkedTreeMap> taximap = JsonProcessing.hashMapify(R.raw.taxi,this);

        LinkedHashMap<String,String> visited = new LinkedHashMap<>();
        ItineraryCalculator calculator = new ItineraryCalculator(footmap,publicmap,taximap, 20);

        Long startTime = System.nanoTime();
        calculator.bruteForceCalculate(locations,visited,0,0,0,"Marina Bay Sands");
        Long endTime = System.nanoTime();
        Double duration = (endTime - startTime)/1000000.0;

        Log.i("Calculate Itinerary", "onCreate: running time of calculation in milliseconds = " + duration);
        Log.i("Calculate Itinerary", "onCreate: " + calculator.getBestTime());

        // Putting calculated itinerary on recyclerview
        recyclerView.setAdapter(new CalculatedItineraryAdapter(calculator.getBestItinerary()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting up Firebase to connect to
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        UID = firebaseAuth.getCurrentUser().getUid();
        itineraryDatabaseReference = firebaseDatabase.getReference().child(UID);

        // Sending data to Firebase
        itineraryHolder = new ItineraryHolder(date, calculator.getBestItinerary());
        itineraryDatabaseReference.push().setValue(itineraryHolder);

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
}
