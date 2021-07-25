package com.pharmacy.pharmacycare.ui.main;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.NewsAndFeaturesData;
import com.pharmacy.pharmacycare.model.TopNews;
import com.pharmacy.pharmacycare.ui.details_page.DetailsActivity;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/24/2018.
 */

public class MainActivityPresenterImpl implements MainActivityPresenter{
    private final ArrayList<NewsAndFeaturesData> newsArrayList;
    private final ArrayList<NewsAndFeaturesData> featuresArrayList;
    private final ArrayList<NewsAndFeaturesData> topNewsArrayList;
    private MainActivityView mainActivityView;
    private ArrayList<DataViewModel> newsArray,featuresArray,topNewsArray;


    public MainActivityPresenterImpl(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
        String news = ((Activity) mainActivityView).getIntent().getStringExtra("news");
         newsArrayList = (ArrayList<NewsAndFeaturesData>) new Gson().fromJson(news,News.class).getArticles();

        String features = ((Activity) mainActivityView).getIntent().getStringExtra("features");
          featuresArrayList = (ArrayList<NewsAndFeaturesData>) new Gson().fromJson(features,Features.class).getArticles();

        String topNews = ((Activity) mainActivityView).getIntent().getStringExtra("topNews");
          topNewsArrayList = (ArrayList<NewsAndFeaturesData>) new Gson().fromJson(topNews,TopNews.class).getArticles();


        setData(newsArrayList,featuresArrayList,topNewsArrayList);
        mainActivityView.setNews(newsArray);
        mainActivityView.setFeatures(featuresArray);
        mainActivityView.setTopNews(topNewsArray);

    }

    private void setData(ArrayList<NewsAndFeaturesData> newsArrayList, ArrayList<NewsAndFeaturesData> featuresArrayList
            ,ArrayList<NewsAndFeaturesData> topNewsArrayList) {
        newsArray = new ArrayList<>();
        featuresArray = new ArrayList<>();
        topNewsArray = new ArrayList<>();
        NewsAndFeaturesData data ;
        for (int i = 0 ; i< newsArrayList.size() ; i++ ) {
            data = newsArrayList.get(i);
            newsArray.add(new DataViewModel(data.getTitle(),data.getUrlToImage(),data.getDescription(),data.getPublishedAt()));
        }
        for (int i = 0 ; i< featuresArrayList.size() ; i++ ) {
            data = featuresArrayList.get(i);
            featuresArray.add(new DataViewModel(data.getTitle(),data.getUrlToImage(),data.getDescription(),data.getPublishedAt()));
        }
        for (int i = 0 ; i< topNewsArrayList.size() ; i++ ) {
            data = topNewsArrayList.get(i);
            topNewsArray.add(new DataViewModel(data.getTitle(),data.getUrlToImage(),data.getDescription(),data.getPublishedAt()));
        }
    }

    @Override
    public void goToTopNewsDetails(int position) {
        Intent intent = new Intent((Activity) mainActivityView, DetailsActivity.class);
        intent.putExtra("type","topNews");
        intent.putExtra("url",topNewsArrayList.get(position).getUrl());
        ((Activity) mainActivityView).startActivity(intent);
    }

    @Override
    public void goToNewsDetails(int position) {
        Intent intent = new Intent((Activity) mainActivityView, DetailsActivity.class);
        intent.putExtra("type","news");
        intent.putExtra("url",newsArrayList.get(position).getUrl());
        ((Activity) mainActivityView).startActivity(intent);
    }

    @Override
    public void goToFeaturesDetails(int position) {
        Intent intent = new Intent((Activity) mainActivityView, DetailsActivity.class);
        intent.putExtra("type","features");
        intent.putExtra("url",featuresArrayList.get(position).getUrl());
        ((Activity) mainActivityView).startActivity(intent);
    }
}
