package com.pharmacy.pharmacycare.ui.refill.transfer;

import com.google.android.gms.location.places.Place;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public class TransferRxPresenterImpl implements TransferRxPresenter {
    private TransferRxView addRefillView;

    public TransferRxPresenterImpl(TransferRxView addRefillView) {
        this.addRefillView = addRefillView;
    }


    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void continueRefill(String fName, String lName, String phone, String bithday, String address,
                               String pharmacyName, String pharmacyCode, String pharmacyPhone, String pharmacyAddress, String notes) {
        if (fName.equals("")) addRefillView.invalidFName();
        else addRefillView.validFName();

        if (lName.equals("")) addRefillView.invalidLName();
        else addRefillView.validLName();

        if (phone.equals("") || phone.replaceAll("-","").trim().length()!= 10 ||
                !isNumeric(phone.replaceAll("-","").trim())) addRefillView.invalidPhone();
        else addRefillView.validPhone();

        if (bithday.equals("")) addRefillView.invalidDate();
        else addRefillView.validDate();

        if (pharmacyName.equals("")) addRefillView.invalidPharmacyName();
        else addRefillView.validPharmacyName();


        if (pharmacyName.equals("")) addRefillView.invalidPharmacyCode();
        else addRefillView.validPharmacyCode();


        if (pharmacyName.equals("")) addRefillView.invalidPharmacyPhone();
        else addRefillView.invalidPharmacyPhone();
        if (!fName.equals("") && !lName.equals("") && !phone.equals("") && !bithday.equals("") &&
                !pharmacyName.equals("") && !pharmacyCode.equals("") && !pharmacyPhone.equals("") ){
            addRefillView.sendRefill( fName,  lName,  phone,  bithday,address,
                     pharmacyName, pharmacyCode, pharmacyPhone, pharmacyAddress,  notes);
        }
    }
}
