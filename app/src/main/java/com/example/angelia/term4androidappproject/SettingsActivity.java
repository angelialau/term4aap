package com.example.angelia.term4androidappproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        } else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

