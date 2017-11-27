package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.angelia.term4androidappproject.Adapters.CustomSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class NewItineraryActivity extends AppCompatActivity {

    ArrayList<String> locations = new ArrayList<>();
    String[] hotels = new String[3];

    EditText editTextItineraryDate;
    Spinner spinnerItineraryHotel;
    SearchView autoCompleteItinerarySearch;
    ListView listSearchResult;

    Button buttonContinue;
    Button buttonAddLocation;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);

        hotels[0] = "Marina Bay Sands";
        hotels[1] = "One Fullerton";
        hotels[2] = "Raffles Hotel";

        locations.add("Rayson");
        locations.add("Kim");
        locations.add("Angelia");

        // Linking up all my views
        editTextItineraryDate = findViewById(R.id.editTextItineraryDate);
        spinnerItineraryHotel = findViewById(R.id.spinnerItineraryHotel);
        autoCompleteItinerarySearch = findViewById(R.id.autoCompleteItinerarySearch);
        listSearchResult = findViewById(R.id.listSearchResult);

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonAddLocation = findViewById(R.id.buttonAddLocation);

        // Adding the Spinner Adapter to our hotel spinner
        CustomSpinnerAdapter hotelSpinnerAdapter = new CustomSpinnerAdapter(this,
                                                                        R.layout.activity_new_itinerary,
                                                                        hotels,
                                                                        "Choose a hotel",
                                                                        hotels[0],
                                                                        hotels[0]);

        spinnerItineraryHotel.setAdapter(hotelSpinnerAdapter);

        // Getting that autocorrect
        autoCompleteItinerarySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<ExtractedResult> results = FuzzySearch.extractSorted(newText, locations, 5);
                ArrayList<String> processedResults = new ArrayList<>();
                for(ExtractedResult r : results) {
                    processedResults.add(r.getString());
                }
                if (arrayAdapter == null) {
                    arrayAdapter = new ArrayAdapter<>(NewItineraryActivity.this,
                                                        android.R.layout.simple_list_item_1,
                                                        processedResults);
                } else {
                    arrayAdapter.clear();
                    arrayAdapter.addAll(processedResults);
                }
                listSearchResult.setAdapter(arrayAdapter);

                return true;
            }
        });

        listSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteItinerarySearch.setQuery(locations.get(position),false);
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EditItineraryActivity.class);
                startActivity(intent);
            }
        });

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                autoCompleteItinerarySearch.setQuery("",false);
                Toast.makeText(v.getContext(),"Location Added",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
