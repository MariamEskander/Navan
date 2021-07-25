package com.pharmacy.pharmacycare.ui.refill.fill_rx;

import com.google.android.gms.location.places.Place;
import com.pharmacy.pharmacycare.model.RxModelView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public interface FillRxView {
    void invalidFName();
    void invalidLName();
    void invalidPhone();
    void invalidDate();
//    void invalidPlace();
    void invalidFile();

    void validFName();
    void validLName();
    void validPhone();
    void validDate();
//    void validPlace();
    void validFile();

    void sendRefill(String fName, String lName, String phone, String bithday,
                    String place, String allergies,String file, String notes);
}
