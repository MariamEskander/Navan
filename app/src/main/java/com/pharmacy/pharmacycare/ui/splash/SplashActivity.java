package com.pharmacy.pharmacycare.ui.splash;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.data.PharmacyContract;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.NewsAndFeaturesData;
import com.pharmacy.pharmacycare.model.TopNews;
import com.pharmacy.pharmacycare.ui.home.HomeActivity;
import com.pharmacy.pharmacycare.ui.main.MainActivity;
import com.pharmacy.pharmacycare.util.MyPharmacy;
import com.pharmacy.pharmacycare.util.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SplashActivity extends AppCompatActivity implements SplashView, NetworkChangeReceiver.OnNetworkStateChange {

    private Snackbar snackbar;
    private SplashPresenter splashPresenter;

    @Override
    protected void onResume() {
        super.onResume();
        splashPresenter.getFeatures();
        splashPresenter.getNews();
        splashPresenter.getTopNews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MyPharmacy.lc = new ReentrantLock();
        snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        NetworkChangeReceiver.setOnMessageListener(this);
        splashPresenter = new SplashPresenterImpl(this);
        splashPresenter.getFeatures();
        splashPresenter.getNews();
        splashPresenter.getTopNews();


    }


    @Override
    public void getDataDone(ArrayList<NewsAndFeaturesData> newsData, ArrayList<NewsAndFeaturesData> featuresData
            , ArrayList<NewsAndFeaturesData> topNewsData) {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("news", new Gson().toJson(new News(newsData)));
        intent.putExtra("features", new Gson().toJson(new Features(featuresData)));
        intent.putExtra("topNews", new Gson().toJson(new TopNews(topNewsData)));
        startActivity(intent);
        finish();
    }

    @Override
    public void showError(String error_msg) {
        showMessage(error_msg);
    }

    @Override
    public void showServerError() {

    }


    @Override
    public void onNetWorkStateChanged(boolean connected) {
        if (connected) {
            splashPresenter.getFeatures();
            splashPresenter.getNews();
            splashPresenter.getTopNews();
        }
    }

    public void showMessage(String s) {
        try {
            snackbar.setText(s);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#ffed5565"));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setBackgroundColor(Color.parseColor("#ffed5565"));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (!snackbar.isShown())
                snackbar.show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
