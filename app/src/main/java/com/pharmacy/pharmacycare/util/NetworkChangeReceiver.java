package com.pharmacy.pharmacycare.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {
 
	static OnNetworkStateChange ONListener;
	
    @Override
    public void onReceive(final Context context, final Intent intent) {
 
        boolean status = NetworkUtil.getConnectivityStatusString(context);
        if (ONListener != null) {
        	ONListener.onNetWorkStateChanged(status);
		}
    }
    
    public static void setOnMessageListener(final OnNetworkStateChange OnListener) {
    	ONListener = OnListener;
    }

    
    public interface OnNetworkStateChange {
        /**
         * Notifies listeners about the new screen. Runs after the animation completed.
         *
         */
        void onNetWorkStateChanged(boolean connected);
    }
}