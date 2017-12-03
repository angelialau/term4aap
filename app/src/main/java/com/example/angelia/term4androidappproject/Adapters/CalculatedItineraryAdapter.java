package com.example.angelia.term4androidappproject.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.CalculateItineraryActivity;
import com.example.angelia.term4androidappproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by arroyo on 29/11/17.
 */

public class CalculatedItineraryAdapter extends RecyclerView.Adapter<CalculatedItineraryAdapter.CalculatedViewHolder> {

    private List<String> placesToGo;
    private List<String> methodsOfTravel;

    public CalculatedItineraryAdapter(LinkedHashMap<String,String> itinerary) {
        this.methodsOfTravel = new ArrayList<>(itinerary.values());
        this.placesToGo = new ArrayList<>(itinerary.keySet());
    }

    public CalculatedItineraryAdapter(String[] placesToGo, String[] methodsOfTravel) {
        this.placesToGo = Arrays.asList(placesToGo);
        this.methodsOfTravel = Arrays.asList(methodsOfTravel);
    }

    public static class CalculatedViewHolder extends RecyclerView.ViewHolder {
        public TextView location;
        public TextView method;

        public CalculatedViewHolder(View view) {
            super(view);
            location = view.findViewById(R.id.textCalculatedLocation);
            method = view.findViewById(R.id.textTravelMethod);
        }
    }

    @Override
    public CalculatedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calculate_itinerary, parent, false);

        return new CalculatedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CalculatedViewHolder holder, int position) {
        String temp_location = this.placesToGo.get(position);
        if (temp_location.length() > 12) {
            temp_location = temp_location.substring(0,12) + "...";
        }

        holder.location.setText(temp_location);
        holder.method.setText(this.methodsOfTravel.get(position));
    }

    @Override
    public int getItemCount() {
        return this.placesToGo.size();
    }
}
