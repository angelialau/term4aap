package com.example.angelia.term4androidappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NearMe extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] places = {getString(R.string.hindu_temple),getString(R.string.chinese_temple),getString(R.string.mosque),getString(R.string.church)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        Intent intent = getIntent();
        String placePref = intent.getStringExtra("PlacePref");
        Spinner placeSpinner = (Spinner) findViewById(R.id.placeSpinner);

        placeSpinner.setAdapter(new CustomSpinnerAdapter(this, R.layout.activity_near_me,places,placePref,getString(R.string.no_preference)));

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),places[i], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
