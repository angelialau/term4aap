package com.example.angelia.term4androidappproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angelia.term4androidappproject.CalculateItineraryActivity;
import com.example.angelia.term4androidappproject.Models.ItineraryHolder;
import com.example.angelia.term4androidappproject.R;
import com.example.angelia.term4androidappproject.ViewSingleItineraryActivity;

import java.util.List;

/**
 * Created by arroyo on 21/11/17.
 */

public class ViewItineraryAdapter extends RecyclerView.Adapter<ViewItineraryAdapter.ViewHolder> {

    private static List<ItineraryHolder> itineraryHolderList;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, place;
        private ImageView type;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.dateTextView);
            place = view.findViewById(R.id.placeTextView);
            type = view.findViewById(R.id.typeImageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ItineraryHolder item = itineraryHolderList.get(position);

            Intent intent = new Intent(v.getContext(), ViewSingleItineraryActivity.class);
            intent.putExtra(ViewSingleItineraryActivity.ITEM_LOCATION_KEY, item.getLocations());
            intent.putExtra(ViewSingleItineraryActivity.ITEM_METHODS_KEY, item.getMethods());
            intent.putExtra(ViewSingleItineraryActivity.ITEM_KEY_KEY, item.getItemKey());

            v.getContext().startActivity(intent);
        }
    }

    public ViewItineraryAdapter(List<ItineraryHolder> itineraryHolderList) {
        this.itineraryHolderList = itineraryHolderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_itinerary, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItineraryHolder item = this.itineraryHolderList.get(position);

        holder.date.setText(item.getDate());
        holder.place.setText(item.getLocations());

    }

    @Override
    public int getItemCount() {
        return itineraryHolderList.size();
    }

    public void add(ItineraryHolder itineraryHolder) {
        this.itineraryHolderList.add(itineraryHolder);
        this.notifyDataSetChanged();
        this.notifyItemInserted(this.getItemCount()-1);
        this.notifyItemRangeChanged(0,this.getItemCount());
    }

    public void clear() {
        this.itineraryHolderList.clear();
        this.notifyDataSetChanged();
    }
}
