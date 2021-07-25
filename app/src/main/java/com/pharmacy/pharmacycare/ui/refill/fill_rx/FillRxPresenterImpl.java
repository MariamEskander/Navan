package com.pharmacy.pharmacycare.ui.refill.fill_rx;

import com.google.android.gms.location.places.Place;
import com.pharmacy.pharmacycare.model.RxModelView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public class FillRxPresenterImpl implements FillRxPresenter {
    private FillRxView addRefillView;

    public FillRxPresenterImpl(FillRxView addRefillView) {
        this.addRefillView = addRefillView;
    }

    @Override
    public void continueRefill(String fName, String lName, String phone, String bithday, String place,
                               String allergies,String file, String notes) {
        if (fName.equals("")) addRefillView.invalidFName();
        else addRefillView.validFName();

        if (lName.equals("")) addRefillView.invalidLName();
        else addRefillView.validLName();

        if (phone.equals("") || phone.replaceAll("-","").trim().length()!= 10 ||
                !isNumeric(phone.replaceAll("-","").trim())) addRefillView.invalidPhone();
        else addRefillView.validPhone();

        if (bithday.equals("")) addRefillView.invalidDate();
        else addRefillView.validDate();

//        if (place == null) addRefillView.invalidPlace();
//        else addRefillView.validPlace();

        if (file == null || file.equals("")) addRefillView.invalidFile();
        else addRefillView.validFile();

        if (!fName.equals("") && !lName.equals("") && !phone.equals("") && !file.equals("") &&!bithday.equals("") && place != null){
               addRefillView.sendRefill( fName,  lName,  phone,  bithday,  place,allergies,file,  notes);
        }
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
}
