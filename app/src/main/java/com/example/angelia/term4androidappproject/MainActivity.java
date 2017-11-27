package com.example.angelia.term4androidappproject;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.angelia.term4androidappproject.Models.PreferenceItem;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    PreferenceItem preferences = new PreferenceItem();
    SharedPreferences sharedPref;

    private int RC_SIGN_IN = 1010;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    public static String userEmail = null;
    public static String LOCATION_KEY = "locations_list";
    public static String DATE_KEY = "date_key";

    public Button buttonMyItinerary;
    public Button buttonFindNearMe;
    public Button buttonStartNewItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setting up Shared Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        boolean useDarkTheme = sharedPref.getBoolean(getString(R.string.translate_pref), false); //translate_pref actually pref to change to dark theme
        Log.i("Angelia", "oncreate dark theme preference: " + useDarkTheme);

        String inputRoutePref = sharedPref.getString(getString(R.string.route_pref),"default"); //default " ", else, best_route, less_walking, fewer_transfers
        Log.i("Angelia", "oncreate route preference: " + inputRoutePref);

        final String inputPlacePref = sharedPref.getString(getString(R.string.place_pref), "no preference");
        Log.i("Angelia", "oncreate place preference: " + inputPlacePref);
        preferences.setPlacePref(inputPlacePref);

        if(useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connecting all the buttons to views
        buttonFindNearMe = findViewById(R.id.find_near_me_button);
        buttonMyItinerary = findViewById(R.id.my_itinerary_button);
        buttonStartNewItinerary = findViewById(R.id.start_itinerary_button);

        // Setup OnClick listeners
        buttonStartNewItinerary.setOnClickListener(buttonStartNewItineraryOnClickListener);

        buttonMyItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ViewItinerariesActivity.class);
                startActivity(intent);
            }
        });

        buttonFindNearMe.setOnClickListener(buttonFindNearMeOnClickListener);

        // Setting up objects related to Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // signed in
                    onSignedInIntialized(user.getEmail());
                }
                else {
                    // signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        HashMap<String, Object> map = hashMapify(R.raw.foot);
        LinkedTreeMap foot = (LinkedTreeMap) map.get("foot");
        LinkedTreeMap marina = (LinkedTreeMap) foot.get("Marina Bay Sands");
        LinkedTreeMap flyer = (LinkedTreeMap) marina.get("Singapore Flyer");
        String price = flyer.toString();

        Log.i("angelia", price);
        Log.i("TAG", "onCreate: something");


    }

    View.OnClickListener buttonFindNearMeOnClickListener =  new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),NearMeActivity.class);
            intent.putExtra("PlacePref",preferences.getPlacePref());
            startActivity(intent);
        }};

    View.OnClickListener buttonStartNewItineraryOnClickListener =  new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),NewItineraryActivity.class);
            intent.putExtra("PlacePref",preferences.getPlacePref());
            startActivity(intent);
        }};

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        //TODO Clear data and stop listening to database
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO change string to string res
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onSignedInIntialized(String email) {
        userEmail = email;
    }

    public void onSignedOutCleanUp() {
        userEmail = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.settings:
                Context context = this;
                Intent intent = new Intent(context,SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                return true;
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.translate_pref))){
            //same code as above
            boolean useDarkTheme = sharedPref.getBoolean(getString(R.string.translate_pref), false); //translate_pref actually pref to change to dark theme
            Log.i("Angelia", "inapp dark theme preference: " + useDarkTheme);
            //REMINDER - write code for what u want to do when this pref is changed
        }

        if (key.equals(getString(R.string.route_pref))){
            String inputRoutePref = sharedPreferences.getString(key, "default");
            Log.i("Angelia", "inapp route preference: " + inputRoutePref);
            //TODO use this in gmaps query
        }

        if (key.equals(getString(R.string.place_pref))){
            String inputPlacePref = sharedPreferences.getString(key, "no preference");
            Log.i("Angelia", "inapp route preference: " + inputPlacePref);
            preferences.setPlacePref(inputPlacePref);
            //TODO put into gmaps query
        }
    }

    public class JsonData{
        String origin;
        String destination;
        String price;
        String time;

    }

    public HashMap<String, Object> hashMapify(int resource){
        String line;
        String output="";

        InputStream inputStream = getResources().openRawResource(resource);
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line=reader.readLine())!=null){
                output = output+line;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        Gson gson = new Gson();
        HashMap<String, Object> map = gson.fromJson(output, new TypeToken<HashMap<String, Object>>(){}.getType());

        return map;


    }


}
