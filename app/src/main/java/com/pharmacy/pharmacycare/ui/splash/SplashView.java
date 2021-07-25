package com.pharmacy.pharmacycare.ui.splash;

import com.pharmacy.pharmacycare.model.NewsAndFeaturesData;
import com.pharmacy.pharmacycare.util.BaseView;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/24/2018.
 */

public interface SplashView extends BaseView{
    void getDataDone(ArrayList<NewsAndFeaturesData> newsData, ArrayList<NewsAndFeaturesData> featuresData
    , ArrayList<NewsAndFeaturesData> topNews);
}
