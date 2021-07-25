package com.pharmacy.pharmacycare.ui.refill.add;

import com.google.android.gms.location.places.Place;
import com.pharmacy.pharmacycare.model.RxModelView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public interface AddRefillPresenter {
    void continueRefill(String fName, String lName, String phone,
                        String place, String rx_number, String medication_name, ArrayList<RxModelView> items
            , String notes);
}
