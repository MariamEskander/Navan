package com.pharmacy.pharmacycare.ui.main;

import com.pharmacy.pharmacycare.model.DataViewModel;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/24/2018.
 */

public interface MainActivityView {
    void setNews(ArrayList<DataViewModel> dataViewModels);
    void setFeatures(ArrayList<DataViewModel> dataViewModels);
    void setTopNews(ArrayList<DataViewModel> dataViewModels);
}
