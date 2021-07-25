package com.pharmacy.pharmacycare.ui.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.TopNews;
import com.pharmacy.pharmacycare.ui.about_us.AboutUsActivity;
import com.pharmacy.pharmacycare.ui.main.MainActivity;
import com.pharmacy.pharmacycare.ui.notifications.NotificationsActivity;
import com.pharmacy.pharmacycare.ui.refill.add.AddRefillActivity;
import com.pharmacy.pharmacycare.ui.refill.fill_rx.FillRxActivity;
import com.pharmacy.pharmacycare.ui.refill.transfer.TransferRxActivity;
import com.pharmacy.pharmacycare.ui.splash.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.info)
    LinearLayout info;

    @BindView(R.id.news)
    LinearLayout news;

    @BindView(R.id.notifications)
    LinearLayout notifications;

    @BindView(R.id.refill_rx)
    LinearLayout refill_rx;

    @BindView(R.id.new_refill)
    LinearLayout new_refill;

    @BindView(R.id.refill_transfer)
    LinearLayout refill_transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NotificationsActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
            }
        });


        refill_rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddRefillActivity.class));
            }
        });


        new_refill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FillRxActivity.class));
            }
        });


        refill_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TransferRxActivity.class));
            }
        });


        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("news", getIntent().getStringExtra("news"));
                intent.putExtra("features",getIntent().getStringExtra("features"));
                intent.putExtra("topNews",getIntent().getStringExtra("topNews"));
                startActivity(intent);
            }
        });
    }
}
