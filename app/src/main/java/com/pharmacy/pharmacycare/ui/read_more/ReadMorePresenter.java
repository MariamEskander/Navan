package com.pharmacy.pharmacycare.ui.read_more;

/**
 * Created by Mariam.Narouz on 1/22/2018.
 */

public interface ReadMorePresenter {
    void getData(int offset);
    void swipeToGetData();
    void goToDetails(int position);
}
