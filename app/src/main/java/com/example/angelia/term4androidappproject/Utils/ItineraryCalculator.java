package com.example.angelia.term4androidappproject.Utils;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by arroyo on 28/11/17.
 */

public class ItineraryCalculator {

    private HashMap<String,LinkedTreeMap> footHashMap;
    private HashMap<String,LinkedTreeMap> publicTransportHashMap;
    private HashMap<String,LinkedTreeMap> taxiHashMap;

    private HashMap<String, String> bestItinerary;
    private double bestTime = Math.pow(2,31);

    private static final String PRICE_KEY = "price";
    private static final String TIME_KEY = "time";
    private static final String start = "Marina Bay Sands";
    private static final String TAG = "Itinerary Calculator";

    public ItineraryCalculator(HashMap<String, LinkedTreeMap> footHashMap,
                               HashMap<String, LinkedTreeMap> publicTransportHashMap,
                               HashMap<String, LinkedTreeMap> taxiHashMap) {

        this.footHashMap = footHashMap;
        this.publicTransportHashMap = publicTransportHashMap;
        this.taxiHashMap = taxiHashMap;
    }

    public void bruteForceCalculate(ArrayList<String> wantToVisit, LinkedHashMap<String,String> visited,
                                                      double cost, double time, int count, String current){

        LinkedTreeMap fromLocationByFoot = null;
        LinkedTreeMap fromLocationByPublic = null;
        LinkedTreeMap fromLocationByTaxi = null;

        if (wantToVisit.isEmpty()) {
            if (count >= 0) {
                wantToVisit.add(start);
                fromLocationByFoot = this.footHashMap.get(current);
                fromLocationByPublic = this.publicTransportHashMap.get(current);
                fromLocationByTaxi = this.taxiHashMap.get(current);

                count = -10;
            }
            else {
                if (cost > 20) {
                    return;
                }
                else if (this.bestTime > time) {
                    this.bestTime = time;
                    this.bestItinerary = visited;
                    //Log.i(TAG, "bruteForceCalculate: " + time);
                    //Log.i(TAG, "bruteForceCalculate: " + visited.toString());
                    return;
                }
            }
        }

        else if (count == 0) {
            wantToVisit.remove(start);

            fromLocationByFoot = this.footHashMap.get(start);
            fromLocationByPublic = this.publicTransportHashMap.get(start);
            fromLocationByTaxi = this.taxiHashMap.get(start);
        }

        else if (count > 0) {
            fromLocationByFoot = this.footHashMap.get(current);
            fromLocationByPublic = this.publicTransportHashMap.get(current);
            fromLocationByTaxi = this.taxiHashMap.get(current);
        }

//        Log.i(TAG, "bruteForceCalculate: " + current);
//        Log.i(TAG, "bruteForceCalculate: " + Arrays.toString(wantToVisit.toArray()));
//        Log.i(TAG, "bruteForceCalculate: " + visited.toString());

        bruteForceHelper(wantToVisit, visited, fromLocationByFoot, "foot", cost, time, count);
        bruteForceHelper(wantToVisit, visited, fromLocationByPublic, "public transport", cost, time, count);
        bruteForceHelper(wantToVisit, visited, fromLocationByTaxi, "taxi", cost, time, count);

    }

    public void bruteForceHelper(ArrayList<String> wantToVisit, LinkedHashMap<String,String> visited,
                                 LinkedTreeMap data, String type, double cost, double time, int count) {
        double temp_cost;
        double temp_time;

        LinkedHashMap<String,String> temp_visited;
        ArrayList<String> temp_wantToVisit;
        LinkedTreeMap price_time;

        for (String place : wantToVisit) {
            temp_visited = new LinkedHashMap<>(visited);
            temp_wantToVisit = new ArrayList<>(wantToVisit);

            price_time = (LinkedTreeMap) data.get(place);
//
//            Log.i(TAG, "bruteForceHelper: " + place);
//            Log.i(TAG, "bruteForceHelper: " + count);
            price_time.toString();


            if (price_time != null) {
                temp_visited.put(place, type);
                temp_wantToVisit.remove(place);

                temp_cost = cost + (Double) price_time.get(PRICE_KEY);
                temp_time = time + (Double) price_time.get(TIME_KEY);


                bruteForceCalculate(temp_wantToVisit, temp_visited, temp_cost, temp_time, count+1, place);
            }
        }
    }

    public HashMap<String, String> getBestItinerary() {
        return this.bestItinerary;
    }

    public double getBestTime() {
        return this.bestTime;
    }
}

