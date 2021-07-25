package com.pharmacy.pharmacycare.ui.refill.add;

import com.google.android.gms.location.places.Place;
import com.pharmacy.pharmacycare.model.RxModelView;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 3/27/2018.
 */

public class AddRefillPresenterImpl implements AddRefillPresenter {
    private AddRefillView addRefillView;
    private ArrayList<RxModelView> rxModelViews = new ArrayList<>();

    public AddRefillPresenterImpl(AddRefillView addRefillView) {
        this.addRefillView = addRefillView;
    }

    @Override
    public void continueRefill(String fName, String lName, String phone,
                               String place, String rx_number, String medication_name, ArrayList<RxModelView> items,  String notes) {
        if (fName.equals("")) addRefillView.invalidFName();
        else addRefillView.validFName();

        if (lName.equals("")) addRefillView.invalidLName();
        else addRefillView.validLName();

        if (phone.equals("") || phone.replaceAll("-","").trim().length()!= 10 ||
                !isNumeric(phone.replaceAll("-","").trim())) addRefillView.invalidPhone();
        else addRefillView.validPhone();

//        if (place == null) addRefillView.invalidPlace();
//        else addRefillView.validPlace();

        if ((items == null || items.size()== 0 ) && rx_number.equals("") && medication_name.equals("")) addRefillView.invalidRx();
        else {
            rxModelViews = new ArrayList<>();
            if (items!= null && items.size() > 0)
            for (int i = 0 ; i< items.size() ;i++){
                if (!items.get(i).getMedicationName().equals("") || !items.get(i).getRx().equals("")){
                    rxModelViews.add(items.get(i));
                }
            }
            if (rxModelViews.size() == 0  && rx_number.equals("") && medication_name.equals("")) addRefillView.invalidRx();
            else addRefillView.validRx();


            if (!rx_number.equals("") || !medication_name.equals("")) rxModelViews.add(new RxModelView(rx_number,medication_name));

        }


        if (!fName.equals("") && !lName.equals("") && !phone.equals("") && place != null &&
                !(medication_name.equals("") && medication_name.equals("") && rxModelViews.size() == 0 )){
               addRefillView.sendRefill( fName,  lName,  phone,  place, rxModelViews,  notes);
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
