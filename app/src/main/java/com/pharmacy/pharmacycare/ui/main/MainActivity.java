package com.pharmacy.pharmacycare.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.ui.read_more.ReadMoreActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mariam on 3/30/2018.
 */


public class MainActivity extends AppCompatActivity implements MainActivityView , GoToNewsDetails ,GoToFeaturesDetails ,GoToTopNewsDetails{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.read_more_features)
    CardView read_more_features;

    @BindView(R.id.read_more_news)
    CardView read_more_news;

    @BindView(R.id.read_more_top_news)
    CardView read_more_top_news;


    @BindView(R.id.health_news)
    LinearLayout health_news;


    @BindView(R.id.health_features)
    LinearLayout health_features;

    @BindView(R.id.top_news)
    DiscreteScrollView top_news;

    private MainActivityPresenter mainActivityPresenter;
    private TopNewsAdapter topNewsAdapter;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainActivityPresenter = new MainActivityPresenterImpl(this);
        initToolbar();
        handleClick();
    }

    private void handleClick() {

        read_more_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ReadMoreActivity.class);
                intent.putExtra("type", "news");
                startActivity(intent);
            }
        });

        read_more_features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ReadMoreActivity.class);
                intent.putExtra("type", "features");
                startActivity(intent);
            }
        });

        read_more_top_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ReadMoreActivity.class);
                intent.putExtra("type", "topNews");
                startActivity(intent);
            }
        });


    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void setNews(ArrayList<DataViewModel> dataViewModels) {
        for (int i= 0 ; i < dataViewModels.size(); i++) {
            NewsAdapter newsAdapter = new NewsAdapter(this, dataViewModels.get(i),i);
            health_news.addView(newsAdapter);
        }
    }

    @Override
    public void setFeatures(ArrayList<DataViewModel> dataViewModels) {
        for (int i= 0 ; i < dataViewModels.size(); i++) {
            FeaturesAdapter featuresAdapter = new FeaturesAdapter(this, dataViewModels.get(i),i);
            health_features.addView(featuresAdapter);
        }
    }

    @Override
    public void setTopNews(ArrayList<DataViewModel> dataViewModels) {
        topNewsAdapter = new TopNewsAdapter(this, dataViewModels);
        top_news.setAdapter(topNewsAdapter);
    }

    @Override
    public void goToNewsDetails(int position) {
        mainActivityPresenter.goToNewsDetails(position);
    }

    @Override
    public void goToFeaturesDetails(int position) {
        mainActivityPresenter.goToFeaturesDetails(position);
    }

    @Override
    public void goToDetails(int position) {
        mainActivityPresenter.goToTopNewsDetails(position);
    }
}
