package com.example.angelia.term4androidappproject;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by arroyo on 21/11/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ItineraryItem> itineraryItemList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, place;
        public ImageView type;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.dateTextView);
            place = view.findViewById(R.id.placeTextView);
            type = view.findViewById(R.id.typeImageView);
        }
    }

    public RecyclerViewAdapter(List<ItineraryItem> itineraryItemList) {
        this.itineraryItemList = itineraryItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itinerary, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItineraryItem item = itineraryItemList.get(position);
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        holder.date.setText(df.format("dd MMM yyyy", item.getDate()));
        holder.place.setText(item.getPlaces().get(0));

        switch (item.getType().toLowerCase()){
            case "temple":
                holder.type.setImageResource(R.drawable.temple);
                break;
            case "church":
                holder.type.setImageResource(R.drawable.church);
                break;
            case "mosque":
                holder.type.setImageResource(R.drawable.mosque);
                holder.type.setPadding(0,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return itineraryItemList.size();
    }
}
