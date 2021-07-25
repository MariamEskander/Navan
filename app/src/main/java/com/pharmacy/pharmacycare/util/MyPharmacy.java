package com.pharmacy.pharmacycare.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pharmacy.pharmacycare.data.PharmacyDbHelper;
import com.pharmacy.pharmacycare.model.Notifications;
import com.pharmacy.pharmacycare.ui.refill.refill_action.RefillActionActivity;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * Created by Mariam on 3/30/2018.
 */
public class MyPharmacy {

    private static MyPharmacy mInstance = null;
    public static Lock lc = null;
    public static int dbcount = 0;
    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;
    Notifications notifications;

    Context context;


    private MyPharmacy(Context context) {
        this.context = context;
        myPrefs = context.getSharedPreferences("pharmacy_care", 0);
        prefsEditor = myPrefs.edit();

    }

    public static MyPharmacy getInstance(Context context) {
        if (mInstance == null)
            mInstance = new MyPharmacy(context);
        return mInstance;
    }

    public Notifications getNotifications() {
        if (notifications == null) {
            String user_st = myPrefs.getString("notifications", null);
            if (user_st != null) {
                notifications = new Gson().fromJson(user_st, Notifications.class);
            }else notifications = new Notifications();
        }
        return notifications;
    }

    public void setNotifications(String notification) {
        getNotifications().getStrings().add(notification);
        prefsEditor.putString("notifications", new Gson().toJson(notifications));
        prefsEditor.apply();
    }



//    private void clearUser() {
//        user = null;
//        loginType = null;
//        prefsEditor.clear();
//        prefsEditor.apply();
//
//        //  ((Activity) context).finishAffinity();
//        Intent i = new Intent(context, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);
//
//    }


}