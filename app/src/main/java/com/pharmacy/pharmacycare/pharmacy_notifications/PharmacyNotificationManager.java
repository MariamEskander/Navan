package com.pharmacy.pharmacycare.pharmacy_notifications;

import android.content.Context;

import java.util.Map;


/**
 * Created by Mariam on 8/15/2017.
 */

public class PharmacyNotificationManager extends BaseManager implements NotificationMessageEVENT{


	private static PharmacyNotificationManager instance;
	private Context mContext;
	

	private PharmacyNotificationManager(Context context) {
		this.mContext = context;
	}

	public static PharmacyNotificationManager getInstance(Context context) {
		if (instance == null) {
			instance = new PharmacyNotificationManager(context);
		}
		return instance;
	}

	//method triggered when network status changes
	public void NotifyMessageNotification(String b) {
		updateObservers(NOTIFICATION,b);
	}

}
