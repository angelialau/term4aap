package com.example.angelia.term4androidappproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.angelia.term4androidappproject.Adapters.ViewItineraryAdapter;
import com.example.angelia.term4androidappproject.Models.ItineraryHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewItinerariesActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public ViewItineraryAdapter viewItineraryAdapter;
    public LinearLayoutManager linearLayoutManager;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference itineraryDatabaseReference;
    private ChildEventListener childEventListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String UID;
    
    private String TAG = "ViewItinerariesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.useDarkTheme){
            setTheme(R.style.AppTheme_Dark);
        } else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        recyclerView = findViewById(R.id.recycler_view);

        // Setting up Firebase to connect to
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        UID = firebaseAuth.getCurrentUser().getUid();
        itineraryDatabaseReference = firebaseDatabase.getReference().child(UID);

        // Put data into recyclerView adapter
        List<ItineraryHolder> itineraryHolders = new ArrayList<>();
        viewItineraryAdapter = new ViewItineraryAdapter(itineraryHolders);
        recyclerView.setAdapter(viewItineraryAdapter);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    attachDatabaseReadListener();
                } else {
                    detachDatabaseReadListener();
                    Log.i(TAG, "onAuthStateChanged: Database listener detached");
                    viewItineraryAdapter.clear();
                }
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void attachDatabaseReadListener(){
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ItineraryHolder itineraryHolder = dataSnapshot.getValue(ItineraryHolder.class);
                    itineraryHolder.setItemKey(dataSnapshot.getKey());
                    viewItineraryAdapter.add(itineraryHolder);
                    Log.i(TAG, "onChildAdded: called");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.i(TAG, "onChildChanged: called");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "onChildRemoved: called");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.i(TAG, "onChildChanged: called");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG, "onChildChanged: called");
                }
            };
            itineraryDatabaseReference.addChildEventListener(childEventListener);
        }
    }

    public void detachDatabaseReadListener(){
        if (childEventListener != null) {
            itineraryDatabaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
}
