package com.pharmacy.pharmacycare.api;

/**
 * Created by mahmoudhedya on 7/19/17.
 */
public interface ApiCallBack {
    void onSuccess(Object object);
    void onFailure(Object object);
    void onInvalidCredentials();
}