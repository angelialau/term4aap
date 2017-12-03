package com.example.angelia.term4androidappproject.Utils;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Angelia on 29/11/17.
 */

public class ShortestPathItineraryCalculator {
    private HashMap<String, LinkedTreeMap> footHashMap;
    private HashMap<String, LinkedTreeMap>  publicTransportHashMap;
    private HashMap<String, LinkedTreeMap>  taxiHashMap;

    private LinkedHashMap<String, String> spBestItinerary; //order and mode of transport
    private Double costOfItinerary;
    private Double totalTimeNeeded;

    //for keeping track in greedy algo
    private ArrayList<String> itineraryForDebugging; //order of shortest itinerary
    private ArrayList<Double> timePath; //with corresponding travel times
    private ArrayList<Double> costPath; //with corresponding travel prices

    private static final String PRICE_KEY = "price";
    private static final String TIME_KEY = "time";
    private static final String FOOT_KEY = "foot";
    private static final String PUBLICTRANSPORT_KEY = "public transport";
    private static final String TAXI_KEY = "taxi";
    private static final String start = "Marina Bay Sands";
    private static final String TAG = "GreedyApproach";
    private static final double FREE = 0.0;

    public ShortestPathItineraryCalculator(HashMap<String, LinkedTreeMap> footHashMap,
                                           HashMap<String, LinkedTreeMap> publicTransportHashMap,
                                           HashMap<String, LinkedTreeMap>  taxiHashMap) {

        this.footHashMap = footHashMap;
        this.publicTransportHashMap = publicTransportHashMap;
        this.taxiHashMap = taxiHashMap;
        this.costOfItinerary = 0.0;
        this.totalTimeNeeded = 0.0;
        this.spBestItinerary = new LinkedHashMap<>();
        this.itineraryForDebugging = new ArrayList<>();
        this.timePath = new ArrayList<>();
        this.costPath = new ArrayList<>();
    }

    public LinkedHashMap<String, String> getSpBestItinerary() {
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
    public void spItineraryCalculator(ArrayList<String> placesToVisit, double budget){
        double shortestTimeNeeded = Math.pow(2,32);

        if(itineraryForDebugging.size()==0){
            String firstLocation = null;
            placesToVisit.remove(start); //don't double count mbs

            for (String place: placesToVisit){
                LinkedTreeMap dataOfStart = this.footHashMap.get(start);
                LinkedTreeMap priceTimeOfChoice = (LinkedTreeMap) dataOfStart.get(place);
                Double timeOfChoice = (Double) priceTimeOfChoice.get(TIME_KEY);
                if(timeOfChoice < shortestTimeNeeded){
                    shortestTimeNeeded = timeOfChoice;
                    firstLocation = place;
                }
            }

            itineraryForDebugging.add(firstLocation); //[0] = first destination
            timePath.add(shortestTimeNeeded); //[0] = time from mbs -> first location
            spBestItinerary.put(firstLocation, FOOT_KEY); //first entry
            placesToVisit.remove(firstLocation);
            totalTimeNeeded += shortestTimeNeeded;

        }

        while(placesToVisit.size()>0){
            String currentLocation = itineraryForDebugging.get(itineraryForDebugging.size()-1);
            LinkedTreeMap dataOfCurrentLocation = this.footHashMap.get(currentLocation);
            String nextLocation = "error";

            shortestTimeNeeded = Math.pow(2,32); //reinitialise!

            for(String place : placesToVisit){ //placesToVisit shouldn't have current location since it has alr been visited
                LinkedTreeMap price_timeOfNextLocation = (LinkedTreeMap) dataOfCurrentLocation.get(place);
                Double timeNeeded = (Double) price_timeOfNextLocation.get(TIME_KEY);
                if (timeNeeded<shortestTimeNeeded){
                    shortestTimeNeeded = timeNeeded;
                    nextLocation = place;
                }
            }

            spBestItinerary.put(nextLocation, FOOT_KEY); //get to nextLocation via Foot
            itineraryForDebugging.add(nextLocation);
            timePath.add(shortestTimeNeeded);
            placesToVisit.remove(nextLocation); //so that i only check remaining unvisited locations
            totalTimeNeeded+=shortestTimeNeeded;

        }

        if(placesToVisit.size()==0){ //accounting for travel back to mbs
            String lastPlace = itineraryForDebugging.get(itineraryForDebugging.size()-1);
            LinkedTreeMap dataOfLastPlace = this.footHashMap.get(lastPlace);
            LinkedTreeMap priceTimeOfLastPlace = (LinkedTreeMap) dataOfLastPlace.get(start);
            Double timeBackToStart = (Double) priceTimeOfLastPlace.get(TIME_KEY);
            spBestItinerary.put(start, FOOT_KEY);
            itineraryForDebugging.add(start);
            timePath.add(timeBackToStart); //last element is the time it takes to go back to mbs
            totalTimeNeeded += timeBackToStart;
        }

        //travelling by foot is free
        for (int i = 0; i < itineraryForDebugging.size(); i++) {
            costPath.add(FREE);
            costOfItinerary+=FREE;
        }

        Log.i(TAG, "initial itinerary: " + spBestItinerary.toString());

        //format: relax(budget, currentTotalTimeNeeded, modeOfTransport)
        relax(budget, this.publicTransportHashMap, PUBLICTRANSPORT_KEY);
        relax(budget, this.taxiHashMap, TAXI_KEY);

        Log.i(TAG, "final cost of itinerary: " + costOfItinerary);
        Log.i(TAG, "final total time taken: " + totalTimeNeeded);


    }

    /**
     * Strategy: find the burden edge ie the edge within the itinerary with the longest path, then
     * reorder the itinerary with the burden edge as index 0. Then relax the edges in this new order
     * to see if relaxation shortens the total time needed while still keeping within budget. That means its
     * okay if costOfTravel increases, so long as it remains within budget.
     *
     * @param budget
     * @param modeOfTransport: either public transport or taxi
     * @param mode: name of modeOfTransport, to check the price of travel via public transport so that we can
     *            find out whether travelling same edge via taxi is more efficient
     *
     */
    public void relax(double budget, HashMap<String, LinkedTreeMap> modeOfTransport, String mode){
        Log.i(TAG, "entered relax for mode: "+ mode);

        //reordering itineraryForDebugging so that it starts from burden edge
        int indexOfBurden = timePath.indexOf(Collections.max(timePath));
        ArrayList<String> reorderedItinerary = new ArrayList<>();
        ArrayList<Double> reorderedTimePath = new ArrayList<>();
        ArrayList<Double> reorderedCostPath = new ArrayList<>();
        for (int i = indexOfBurden; i < itineraryForDebugging.size(); i++) {
            reorderedItinerary.add(itineraryForDebugging.get(i));
            reorderedTimePath.add(timePath.get(i));
            reorderedCostPath.add(costPath.get(i));

        }
        for (int i = 0; i < indexOfBurden; i++) {
            reorderedItinerary.add(itineraryForDebugging.get(i));
            reorderedTimePath.add(timePath.get(i));
            reorderedCostPath.add(costPath.get(i));
        }

        itineraryForDebugging = reorderedItinerary;
        timePath = reorderedTimePath;
        costPath = reorderedCostPath;

        Log.i(TAG, "it4dbug after reordering: "+ itineraryForDebugging.toString());
        Log.i(TAG, "timepath after reordering: "+ timePath.toString());
        Log.i(TAG, "costpath after reordering: "+ costPath.toString());

        for (int i = 0; i < itineraryForDebugging.size(); i++) { //stop at second last element cos im investigating by pair of locations
            int indexOfCurrent; //destination
            int indexOfPrev; //origin
            String currentLocation;
            String prevLocation;
            if(i==0){
                indexOfPrev = itineraryForDebugging.size()-1; //last element
                indexOfCurrent = i;
            }
            else{
                indexOfPrev = i-1;
                indexOfCurrent = i;
            }
            prevLocation = itineraryForDebugging.get(indexOfPrev);
            currentLocation = itineraryForDebugging.get(indexOfCurrent);

            LinkedTreeMap dataOfPreviousLocation = modeOfTransport.get(prevLocation);
            LinkedTreeMap priceTimeOfCurrentLocation = (LinkedTreeMap) dataOfPreviousLocation.get(currentLocation);

            Double timeNeeded = (Double) priceTimeOfCurrentLocation.get(TIME_KEY);
            Double oldTimeNeeded = timePath.get(indexOfCurrent);
            Double tempTotalTimeNeeded = totalTimeNeeded - oldTimeNeeded + timeNeeded;

            Double newCost = (Double) priceTimeOfCurrentLocation.get(PRICE_KEY);
            Double oldCost = costPath.get(indexOfCurrent);
//            Log.i(TAG, "cost of ")
            Double tempTotalCostNeeded = costOfItinerary - oldCost + newCost;

            if(tempTotalCostNeeded<=budget && tempTotalTimeNeeded<totalTimeNeeded){
                spBestItinerary.put(currentLocation, mode);
                totalTimeNeeded = tempTotalTimeNeeded;
                costOfItinerary = tempTotalCostNeeded; //since budget may increase, and you may not be able to relax future options
                timePath.add(indexOfCurrent, timeNeeded); //replace prev time of edge with shorter option
                timePath.remove(indexOfCurrent+1);
                costPath.add(indexOfCurrent, newCost);
                costPath.remove(indexOfCurrent+1);

                Log.i(TAG, "time path after relaxation: " + timePath);
                Log.i(TAG, "cost path after relaxation: " + costPath);

                //no need to update itineraryForDebugging since that is just a list of locations to visit which does not change with relaxation
            }

        }

    }


}
