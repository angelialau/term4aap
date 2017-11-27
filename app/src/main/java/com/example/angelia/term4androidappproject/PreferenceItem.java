package com.example.angelia.term4androidappproject;

/**
 * Created by NPStudent on 22/11/2017.
 */

public class PreferenceItem {
    private String placePref;
    private String routePref;

    PreferenceItem(){
        this.placePref = null;
        this.routePref = null;
    }

    public void setPlacePref(String placePref){
        this.placePref = placePref;
    }

    public String getPlacePref(){
        return this.placePref;
    }
}
