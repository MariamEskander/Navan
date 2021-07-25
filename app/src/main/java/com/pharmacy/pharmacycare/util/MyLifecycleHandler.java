package com.pharmacy.pharmacycare.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    private static int created;
    private static int destroyed;

    private static String activityName;

    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public static String getActivityName() {
        return activityName;
    }

    public static boolean isItTheOnlyActivityOpened() {
        return created == 1 ? true : false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        created++;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        created--;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
        Log.d("activity", activity.getLocalClassName());

        activityName = activity.getLocalClassName();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        Log.w("test", "application is visible: " + (started > stopped));
    }
}