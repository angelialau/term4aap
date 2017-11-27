package com.example.angelia.term4androidappproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.EditItineraryActivity;
import com.example.angelia.term4androidappproject.R;

import java.util.List;

/**
 * Created by arroyo on 27/11/17.
 */

public class EditItineraryAdapter extends RecyclerView.Adapter<EditItineraryAdapter.ViewHolder> {

    private List<String> locationList;
    private boolean listChanged;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView location;
        public Button remove;

        public ViewHolder(View view) {
            super(view);
            location = view.findViewById(R.id.textLocation);
            remove = view.findViewById(R.id.buttonRemoveLocation);
        }

        public String getLocation() {
            if (location != null) {
                return location.getText().toString();
            } else {
                return null;
            }
        }
    }

    public EditItineraryAdapter(List<String> locationList) {
        this.locationList = locationList;
    }

    @Override
    public EditItineraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_edit_itinerary, parent, false);

        return new EditItineraryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EditItineraryAdapter.ViewHolder holder, final int position) {
        String item = locationList.get(position);

        holder.location.setText(item);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationList.remove(holder.getLocation());
                removeData(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void removeData(int position){
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position,locationList.size());
        this.notifyDataSetChanged();
    }
}
