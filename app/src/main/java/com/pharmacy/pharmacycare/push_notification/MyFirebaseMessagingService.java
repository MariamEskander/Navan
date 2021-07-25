package com.pharmacy.pharmacycare.push_notification;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.pharmacy_notifications.PharmacyNotificationManager;
import com.pharmacy.pharmacycare.ui.notifications.NotificationsActivity;
import com.pharmacy.pharmacycare.ui.splash.SplashActivity;
import com.pharmacy.pharmacycare.util.MyPharmacy;


import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    int n ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        MyPharmacy.getInstance(this).setNotifications(remoteMessage.getNotification().getBody());

        if (isAppOnForeground(this)) {
            PharmacyNotificationManager pharmacyNotificationManager = PharmacyNotificationManager.getInstance(this);
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(2);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.toString().contains("com.pharmacy.pharmacycare.ui.notifications")) {
                pharmacyNotificationManager.NotifyMessageNotification(remoteMessage.getNotification().getBody());
            }else {
                createNotification(remoteMessage);
            }

        }else{
            createNotification(remoteMessage);
        }


    }

    private void createNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = remoteMessage.getNotification().getBody();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

   NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(remoteMessage.getNotification().getBody(),
                    remoteMessage.getNotification().getBody(),
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(new Random().nextInt(100000)
                , notificationBuilder.build());
    }

//    private void createNotification(RemoteMessage remoteMessage) {
//        Random rand = new Random();
//        n = rand.nextInt(1000) + 1;
//        Intent intent = new Intent(this, NotificationsActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setAutoCancel(true)
//                .setSound(uri)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setVisibility(android.app.Notification.VISIBILITY_PUBLIC)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(n, notificationBuilder.build());
//    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (int i = 0;i<appProcesses.size() ; i++) {
            if (appProcesses.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcesses.get(i).processName.equals(packageName) && i ==0) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyAndroidFCMService");
                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }
                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }
}