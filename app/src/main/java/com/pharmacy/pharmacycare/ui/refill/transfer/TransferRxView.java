package com.pharmacy.pharmacycare.ui.refill.transfer;

import com.google.android.gms.location.places.Place;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public interface TransferRxView {
    void invalidFName();
    void invalidLName();
    void invalidPhone();
    void invalidDate();
    void invalidPharmacyName();
    void invalidPharmacyPhone();
    void invalidPharmacyCode();

    void validFName();
    void validLName();
    void validPhone();
    void validDate();
    void validPharmacyName();
    void validPharmacyPhone();
    void validPharmacyCode();

    void sendRefill(String fName, String lName, String phone, String bithday,
                    String address , String pharmacyName,
            String pharmacyCode , String pharmacyPhone, String pharmacyAddress, String notes);
}
