package com.pharmacy.pharmacycare.ui.refill.add;

import com.google.android.gms.location.places.Place;
import com.pharmacy.pharmacycare.model.RxModelView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public interface AddRefillView {
    void invalidFName();
    void invalidLName();
    void invalidPhone();
//    void invalidPlace();
    void invalidRx();

    void validFName();
    void validLName();
    void validPhone();
//    void validPlace();
    void validRx();

    void sendRefill(String fName, String lName, String phone
            , String place, ArrayList<RxModelView> rxModelViews, String notes);
}
