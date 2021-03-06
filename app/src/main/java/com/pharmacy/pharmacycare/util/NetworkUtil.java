package com.pharmacy.pharmacycare.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	 public static int TYPE_WIFI = 1;
	    public static int TYPE_MOBILE = 2;
	    public static int TYPE_NOT_CONNECTED = 0;
	     
	     
	    public static int getConnectivityStatus(Context context) {
	        ConnectivityManager cm = (ConnectivityManager) context
	                .getSystemService(Context.CONNECTIVITY_SERVICE);
	 
	        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	        if (null != activeNetwork) {
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
	                return TYPE_WIFI;
	             
	            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
	                return TYPE_MOBILE;
	        }
	        return TYPE_NOT_CONNECTED;
	    }
	     
	    public static boolean getConnectivityStatusString(Context context) {
	        int conn = NetworkUtil.getConnectivityStatus(context);
	        boolean Conected = false;
	        if (conn == NetworkUtil.TYPE_WIFI) {
	        	Conected = true;
	        } else if (conn == NetworkUtil.TYPE_MOBILE) {
	        	Conected = true;
	        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
	        	Conected = false;
	        }
	        return Conected;
	    }
	
}
