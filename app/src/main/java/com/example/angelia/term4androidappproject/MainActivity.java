package com.example.angelia.term4androidappproject;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPref;

    private int RC_SIGN_IN = 1010;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String userEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);

        boolean inputTranslatePref = sharedPref.getBoolean(getString(R.string.translate_pref), false);
        Log.i("Angelia", "oncreate translate preference: " + inputTranslatePref);

        String inputRoutePref = sharedPref.getString(getString(R.string.route_pref),"default"); //default " ", else, best_route, less_walking, fewer_transfers
        Log.i("Angelia", "oncreate route preference: " + inputRoutePref);

        String inputPlacePref = sharedPref.getString(getString(R.string.place_pref), "no preference");
        Log.i("Angelia", "oncreate place preference: " + inputPlacePref);
    }

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
        //TODO Set email and download data
        userEmail = email;
    }

    public void onSignedOutCleanUp() {
        //TODO Unset email and clear all data
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

        if(id == R.id.settings){
            Context context = this;
            Intent intent = new Intent(context,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.translate_pref))){
            //same code as above
            boolean inputTranslatePref = sharedPreferences.getBoolean(key,false);
            Log.i("Angelia", "in app translate preference: " + inputTranslatePref);
            //REMINDER - write code for what u want to do when this pref is changed
            //TODO translate strings.xml to chinese
        }

        if (key.equals(getString(R.string.route_pref))){
            String inputRoutePref = sharedPreferences.getString(key, "default");
            Log.i("Angelia", "inapp route preference: " + inputRoutePref);
            //TODO use this in gmaps query
        }

        if (key.equals(getString(R.string.place_pref))){
            String inputPlacePref = sharedPreferences.getString(key, "no preference");
            Log.i("Angelia", "inapp route preference: " + inputPlacePref);
            //TODO put into gmaps query
        }
    }
}
