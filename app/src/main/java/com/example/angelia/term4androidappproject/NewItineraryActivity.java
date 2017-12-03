package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class NewItineraryActivity extends AppCompatActivity {

    ArrayList<String> locations = new ArrayList<>();
    ArrayList<String> processedResults;
    String[] hotels = new String[3];

    EditText editTextItineraryDate;
    Spinner spinnerItineraryHotel;
    SearchView autoCompleteItinerarySearch;
    ListView listSearchResult;

    Button buttonContinue;
    Button buttonAddLocation;

    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> itinerary;
    String datekey;
    String hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itinerary);

        hotels[0] = "Marina Bay Sands";

        hotel = hotels[0];

        locations.add("Marina Bay Sands");
        locations.add("Singapore Flyer");
        locations.add("Vivo City");
        locations.add("Resorts World Sentosa");
        locations.add("Buddha Tooth Relic Temple");
        locations.add("Zoo");

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
                                                                        hotels[0],
                                                                        hotels[0],
                                                                        hotels[0]);

        spinnerItineraryHotel.setAdapter(hotelSpinnerAdapter);
        spinnerItineraryHotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                hotel = hotels[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        // Getting that autocorrect
        autoCompleteItinerarySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<ExtractedResult> results = FuzzySearch.extractSorted(newText, locations, 5);
                processedResults = new ArrayList<>();
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

        // Getting intent
        Intent intent = getIntent();
        itinerary = intent.getStringArrayListExtra(MainActivity.LOCATION_KEY);
        datekey = intent.getStringExtra(MainActivity.DATE_KEY);
        if (itinerary == null || datekey == null) {
            itinerary = new ArrayList<>();
        } else {
            editTextItineraryDate.setText(datekey);
        }

        // Setting on click listeners
        listSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteItinerarySearch.setQuery(processedResults.get(position),false);
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextItineraryDate.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(),"Date cannot be empty",Toast.LENGTH_SHORT).show();
                } else if (hotel.isEmpty()){
                    Toast.makeText(v.getContext(), "Hotel cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    datekey = editTextItineraryDate.getText().toString();
                    if (!itinerary.contains(hotel)){
                        itinerary.add(hotel);
                    }

                    Intent intent = new Intent(v.getContext(),EditItineraryActivity.class);
                    intent.putExtra(MainActivity.LOCATION_KEY, itinerary);
                    intent.putExtra(MainActivity.DATE_KEY, datekey);
                    startActivity(intent);
                }
            }
        });

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toadd = autoCompleteItinerarySearch.getQuery().toString();
                if(!itinerary.contains(toadd)){
                    itinerary.add(toadd);
                }
                Toast.makeText(v.getContext(),"Location Added",Toast.LENGTH_SHORT).show();
                autoCompleteItinerarySearch.setQuery("",false);
            }
        });
    }
}
