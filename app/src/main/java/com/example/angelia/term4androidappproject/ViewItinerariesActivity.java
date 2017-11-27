package com.example.angelia.term4androidappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.angelia.term4androidappproject.Adapters.ViewItineraryAdapter;
import com.example.angelia.term4androidappproject.Models.ViewItineraryItem;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ViewItinerariesActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ViewItineraryAdapter viewItineraryAdapter;
    public LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        recyclerView = findViewById(R.id.recycler_view);

        // fake data
        List<String> list = Arrays.asList("somewhere");
        Date date = new Date();

        ViewItineraryItem a = new ViewItineraryItem(date, list,"temple");
        ViewItineraryItem b = new ViewItineraryItem(date, list, "church");
        ViewItineraryItem c = new ViewItineraryItem(date, list, "mosque");
        ViewItineraryItem d = new ViewItineraryItem(date, list, "temple");

        List<ViewItineraryItem> viewItineraryItems = Arrays.asList(a,b,c,d);

        viewItineraryAdapter = new ViewItineraryAdapter(viewItineraryItems);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter
        recyclerView.setAdapter(viewItineraryAdapter);
    }
}
