package com.pharmacy.pharmacycare.model;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/30/2018.
 */

public class Notifications {
    ArrayList<String> strings = new ArrayList<>();


    public Notifications(ArrayList<String> strings) {
        this.strings = strings;
    }

    public Notifications() {

    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }
}
