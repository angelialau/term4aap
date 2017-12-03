package com.example.angelia.term4androidappproject.Models;

import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by arroyo on 29/11/17.
 */

public class ItineraryHolder {

    private String date;
    private String locations;
    private String methods;
    private String itemKey;

    public ItineraryHolder(String date, LinkedHashMap<String, String> itinerary) {
        this.date = date;
        this.locations = Arrays.toString(itinerary.keySet().toArray())
                .replace("[","")
                .replace("]","");
        this.methods = Arrays.toString(itinerary.values().toArray())
                .replace("[","")
                .replace("]","");
    }

    public ItineraryHolder() {

    }

    public String getDate() {
        return date;
    }

    public String getLocations() {
        return locations;
    }

    public String getMethods() {
        return methods;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
