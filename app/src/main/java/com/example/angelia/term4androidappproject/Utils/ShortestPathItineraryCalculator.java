package com.example.angelia.term4androidappproject.Utils;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Angelia on 29/11/17.
 */

public class ShortestPathItineraryCalculator {
    private HashMap<String, LinkedTreeMap> footHashMap;
    private HashMap<String, LinkedTreeMap>  publicTransportHashMap;
    private HashMap<String, LinkedTreeMap>  taxiHashMap;

    private HashMap<String, String> spBestItinerary; //order and mode of transport
    private Double costOfItinerary;
    private Double totalTimeNeeded;

    private static final String PRICE_KEY = "price";
    private static final String TIME_KEY = "time";
    private static final String FOOT_KEY = "foot";
    private static final String PUBLICTRANSPORT_KEY = "public transport";
    private static final String TAXI_KEY = "taxi";
    private static final String start = "Marina Bay Sands";
    private static final String TAG = "GreedyApproach";

    public ShortestPathItineraryCalculator(HashMap<String, LinkedTreeMap> footHashMap,
                                           HashMap<String, LinkedTreeMap> publicTransportHashMap,
                                           HashMap<String, LinkedTreeMap>  taxiHashMap) {

        this.footHashMap = footHashMap;
        this.publicTransportHashMap = publicTransportHashMap;
        this.taxiHashMap = taxiHashMap;
        this.costOfItinerary = 0.0;
        this.totalTimeNeeded = 0.0;
        this.spBestItinerary = new HashMap<>();
    }

    public HashMap<String, String> getSpBestItinerary() {
        return spBestItinerary;
    }

    public Double getCostOfItinerary() {
        return costOfItinerary;
    }

    public Double getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    /**
     * Strategy: First find shortest (in terms of time) path using
     * nearest neighbours from marina -> location1 -> ... -> marina with the
     * mode being by Foot. This is so that we can disregard cost as a factor, considering
     * that travelling by foot is free, therefore simplifying the travelling salesman problem.
     * After forming the estimate shortest path, we relax each edge first via public transport
     * then using taxi, while keeping to the budget constraints. The order is as such because
     * we postulate that public transport would be the most efficient option (if efficiency = time/cost)
     * amongst the 3 modes of travel.
     *
     * @param placesToVisit: itinerary to plan
     * @param budget: budget to keep within, should default to 20
     */
    public void spItineraryCalculator(ArrayList<String> placesToVisit, double budget){ //assume exclude hotel
        //format: spBestItinerary.put(nextLocation, modeOfTransport)

        ArrayList<String> itineraryForDebugging = new ArrayList<>();
        ArrayList<Double> timePath = new ArrayList<>();

        spBestItinerary.put(start, "NA"); //start from marina
        itineraryForDebugging.add(start);
        timePath.add(0.0);

        double shortestTimeNeeded;
        totalTimeNeeded = 0.0;

        //checking nearest neighbours by time
        while(!placesToVisit.isEmpty()){
            String currentLocation = itineraryForDebugging.get(itineraryForDebugging.size()-1); //get the last element which is the current location
            String nextLocation = null;
            Iterator iterator = placesToVisit.iterator();
            shortestTimeNeeded = Math.pow(2,31); //reinitialise large number so that you can find nearest neighbour in terms of time
            while(iterator.hasNext()){
                //check every other place left to visit in the itinerary
                if(iterator.next()!=currentLocation){ //cos we don't want the location to check itself
                    LinkedTreeMap dataOfCurrentLocation = (LinkedTreeMap) this.footHashMap.get(currentLocation).get(iterator.next());
                    Double timeNeeded = (Double) dataOfCurrentLocation.get(TIME_KEY);
                    if (timeNeeded < shortestTimeNeeded){
                        shortestTimeNeeded = timeNeeded;
                        nextLocation = (String) iterator.next();
                    }
                }
            }
            spBestItinerary.put(nextLocation, FOOT_KEY);
            itineraryForDebugging.add(nextLocation);
            placesToVisit.remove(nextLocation);
            totalTimeNeeded+=shortestTimeNeeded;
            timePath.add(shortestTimeNeeded);
        }

        //accounting for travelling back to marina
        spBestItinerary.put(start, FOOT_KEY);
        itineraryForDebugging.add(start);

        Log.i(TAG, "entered calculator");
        // remember that travel by foot is free, so costOfItinerary = 0.0;

        //format: relax(itinerary, budget, currentTotalTimeNeeded, modeOfTransport)
        relax(itineraryForDebugging, timePath, budget, this.publicTransportHashMap, PUBLICTRANSPORT_KEY);
        relax(itineraryForDebugging, timePath, budget, this.taxiHashMap, TAXI_KEY);


    }

    /**
     * Strategy: find the burden edge ie the edge within the itinerary with the longest path, then
     * reorder the itinerary with the burden edge as index 0. Then relax the edges in this new order
     * to see if relaxation shortens the total time needed while still keeping within budget. That means its
     * okay if costOfTravel increases, so long as it remains within budget.
     *
     * @param currItinerary: current best guess of shortest path itinerary
     * @param timePath: list of times used to find the "burden", ie the edge with the longest travelling time
     * @param budget
     * @param modeOfTransport: either public transport or taxi
     * @param mode: name of modeOfTransport, to check the price of travel via public transport so that we can
     *            find out whether travelling same edge via taxi is more efficient
     *
     */
    public void relax(ArrayList<String> currItinerary, ArrayList<Double> timePath, double budget, HashMap<String, LinkedTreeMap> modeOfTransport, String mode){
        Log.i(TAG, "entered relax");
        int indexOfBurden = timePath.indexOf(Collections.max(timePath));

        //making a new list starting from edge with longest travelling time between 2 locations
        ArrayList<String> reorderedItinerary = new ArrayList<>();
        ArrayList<Double> reorderedTimePath = new ArrayList<>();
        for (int i = indexOfBurden; i < currItinerary.size()-1; i++) { //size-1 so that i wont double count marina!
            reorderedItinerary.add(currItinerary.get(i));
            reorderedTimePath.add(timePath.get(i));
        }
        for (int i = 0; i < indexOfBurden; i++) {
            reorderedItinerary.add(currItinerary.get(i));
            reorderedTimePath.add(timePath.get(i));
        }

        //inorder traversal of edges, see if totalTimeNeeded shortens with relaxation
        for (int i = 0; i < reorderedItinerary.size()-1; i++) {
            String nextLocation = reorderedItinerary.get(i+1);
            LinkedTreeMap dataOfCurrentLocation = (LinkedTreeMap) modeOfTransport.get(reorderedItinerary.get(i)).get(reorderedItinerary.get(i+1));
            Double timeNeeded = (Double) dataOfCurrentLocation.get(TIME_KEY);
            Double cost = (Double) dataOfCurrentLocation.get(TIME_KEY);

            Double tempTimeNeeded = totalTimeNeeded - reorderedTimePath.get(i) + timeNeeded;

            //if i already relaxed via public transport and am relaxing via taxi,
            //i need to subtract the cost of the edge via public transport from my estimated cost
            Double previousCost = 0.0;

            if(mode.equals(TAXI_KEY)){
                int indexOfPreviousLocation = 0;
                if(i==0) indexOfPreviousLocation = reorderedItinerary.size()-1;
                else indexOfPreviousLocation = i-1;

                LinkedTreeMap dataOfEdge = (LinkedTreeMap) publicTransportHashMap.get(reorderedItinerary.get(indexOfPreviousLocation)).get(reorderedItinerary.get(i));
                previousCost = (Double) dataOfEdge.get(PRICE_KEY);
            }

            Double tempCostNeeded = costOfItinerary - previousCost + cost;

            if(tempCostNeeded<=budget && tempTimeNeeded<totalTimeNeeded){
                spBestItinerary.put(nextLocation, mode);
            }


        }

    }


}
