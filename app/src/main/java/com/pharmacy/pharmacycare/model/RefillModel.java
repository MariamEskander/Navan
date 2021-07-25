package com.pharmacy.pharmacycare.model;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/30/2018.
 */

public class RefillModel {
    UserModel userModel;
    ArrayList<RxModelView> rxModelViews;
    String title;

    public RefillModel(String title ,UserModel userModel, ArrayList<RxModelView> rxModelViews) {
        this.title = title;
        this.userModel = userModel;
        this.rxModelViews = rxModelViews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ArrayList<RxModelView> getRxModelViews() {
        return rxModelViews;
    }

    public void setRxModelViews(ArrayList<RxModelView> rxModelViews) {
        this.rxModelViews = rxModelViews;
    }
}
