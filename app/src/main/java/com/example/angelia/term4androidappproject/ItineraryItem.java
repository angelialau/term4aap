package com.example.angelia.term4androidappproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arroyo on 21/11/17.
 */

public class ItineraryItem {
    private Date date;
    private List<String> places;   // List of all the places to be visited on that date
    private String type;                // Type of place of worship

    public ItineraryItem(Date date, List<String> places, String type) {
        this.date = date;
        this.places = places;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getPlaces() {
        return places;
    }

    public String getType() {
        return type;
    }
}
