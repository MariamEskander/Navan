package com.pharmacy.pharmacycare.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;


/**
 * Created by mhemdan on 11/21/15.
 */
public class PharmacyCareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        Locale.setDefault(Locale.ENGLISH);

        init();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void init() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}
