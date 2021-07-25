package com.pharmacy.pharmacycare.ui.refill.transfer;

import com.google.android.gms.location.places.Place;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public interface TransferRxPresenter {
    void continueRefill(String fName, String lName, String phone, String bithday,
                        String address , String pharmacyName,
                        String pharmacyCode , String pharmacyPhone, String pharmacyAddress, String notes);
}
