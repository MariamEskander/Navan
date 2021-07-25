package com.pharmacy.pharmacycare.ui.notifications;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.data.DataBaseUtil;
import com.pharmacy.pharmacycare.data.PharmacyDbHelper;
import com.pharmacy.pharmacycare.model.Notifications;
import com.pharmacy.pharmacycare.model.Result;
import com.pharmacy.pharmacycare.pharmacy_notifications.NotificationMessageEVENT;
import com.pharmacy.pharmacycare.pharmacy_notifications.PharmacyNotificationManager;
import com.pharmacy.pharmacycare.ui.refill.saved.SavedRefillsActivity;
import com.pharmacy.pharmacycare.util.MyPharmacy;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity implements Observer {
    @BindView(R.id.notifications_rv)
    RecyclerView notifications_rv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title)
    TextView title;


    NotificationsAdapter notificationsAdapter ;
    private PharmacyNotificationManager notificationObserver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        notificationObserver = PharmacyNotificationManager.getInstance(NotificationsActivity.this);
        if (notificationObserver != null)
            notificationObserver.addObserver(this);


        initToolbar();
        notificationsAdapter = new NotificationsAdapter(this,
                MyPharmacy.getInstance(NotificationsActivity.this).getNotifications().getStrings());
        notifications_rv.setAdapter(notificationsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // (Context context, int spanCount)
        notifications_rv.setLayoutManager(layoutManager);
        notifications_rv.setItemAnimator(new DefaultItemAnimator());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(getString(R.string.notifications));
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (((Result) arg).getEvent()) {
            case NotificationMessageEVENT.NOTIFICATION:
                final String s = (String) ((Result) arg).getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addNotifications(s);
                    }
                });
                break;
        }

    }

    private void addNotifications(String s) {
        try {
            Uri notifi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notifi);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationsAdapter = new NotificationsAdapter(this,
                MyPharmacy.getInstance(NotificationsActivity.this).getNotifications().getStrings());
        notifications_rv.setAdapter(notificationsAdapter);
        notifications_rv.scrollToPosition(notificationsAdapter.getItemCount()-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationObserver != null)
            notificationObserver.unregister(this);
    }

}

