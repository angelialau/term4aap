package com.example.angelia.term4androidappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerViewAdapter recyclerViewAdapter;
    public LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        recyclerView = findViewById(R.id.recycler_view);

        // fake data
        List<String> list = Arrays.asList("somewhere");
        Date date = new Date();

        ItineraryItem a = new ItineraryItem(date, list,"temple");
        ItineraryItem b = new ItineraryItem(date, list, "church");
        ItineraryItem c = new ItineraryItem(date, list, "mosque");
        ItineraryItem d = new ItineraryItem(date, list, "temple");

        List<ItineraryItem> itineraryItems = Arrays.asList(a,b,c,d);

        recyclerViewAdapter = new RecyclerViewAdapter(itineraryItems);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
