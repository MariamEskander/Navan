package com.pharmacy.pharmacycare.ui.read_more;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.api.ApiCallBack;
import com.pharmacy.pharmacycare.api.ApiClient;
import com.pharmacy.pharmacycare.model.DataViewModel;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.NewsAndFeaturesData;
import com.pharmacy.pharmacycare.model.TopNews;
import com.pharmacy.pharmacycare.ui.details_page.DetailsActivity;
import com.pharmacy.pharmacycare.util.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by Mariam.Narouz on 1/22/2018.
 */

public class ReadMorePresenterImpl implements ReadMorePresenter, ApiCallBack {
    private ReadMoreView readMoreView;
    private ApiClient apiClient;
    private ArrayList<NewsAndFeaturesData> dataArrayList = new ArrayList<>();
    private ArrayList<DataViewModel> dataViewModels;
    private int size, offset = 1;
    private boolean swipe = false, isLoading = false;
    private String type;

    public ReadMorePresenterImpl(ReadMoreView readMoreView) {
        this.readMoreView = readMoreView;
        apiClient = ApiClient.getInstance();
        type = ((Activity) readMoreView).getIntent().getStringExtra("type");
    }


    public void swipeToGetData() {
        if (isLoading) {
            readMoreView.hideSwipe();
        } else {
            swipe = true;
            getData(1);
        }
    }

    @Override
    public void getData(int offset) {
        Log.i("offset getReadLater", "offset => " + offset);
        if (!isLoading) {
            if (!NetworkUtil.getConnectivityStatusString((Context) readMoreView)) {
                if (swipe) readMoreView.hideSwipe();
                else readMoreView.hidePDialog();
                readMoreView.showError(((Context) readMoreView).getString(R.string.fail_to_connect_to_internet));
            } else {
                this.isLoading = true;
                if (!swipe) readMoreView.showPDialog();
                switch (type) {
                    case "news":
                        apiClient.getNews(offset, this);
                        break;
                    case "topNews":
                        apiClient.getTopNews(offset, this);
                        break;
                    case "features":
                        apiClient.getFeatures(offset, this);
                        break;
                    default:
                        apiClient.getNews(offset, this);
                        break;
                }
            }
        }
    }


    @Override
    public void goToDetails(int position) {
        Intent intent = new Intent((Activity) readMoreView, DetailsActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("url",dataArrayList.get(position).getUrl());
        ((Activity) readMoreView).startActivity(intent);
    }


    @Override
    public synchronized void onSuccess(Object object) {
        if (readMoreView != null) {
            if (swipe) {
                this.offset = 1;
                dataArrayList.clear();
                readMoreView.hideSwipe();
                swipe = false;
                readMoreView.resetView();
            } else
                readMoreView.hidePDialog();

            if (object instanceof News) {
                News news = (News) object;
                if (news.getArticles() != null) {
                    dataViewModels = new ArrayList<>();
                    dataArrayList.addAll(news.getArticles());
                    size = news.getArticles().size();
                    this.offset++;
                    Log.i("offset onsuccess", "offset => " + offset + " " + size);
                    readMoreView.offset(this.offset);
                    readMoreView.setFirstOpen(false);
                    readMoreView.setLoadMore(true);
                    for (int i = 0; i < size; i++) {
                        NewsAndFeaturesData data = news.getArticles().get(i);
                        dataViewModels.add(new DataViewModel(data.getTitle(), data.getUrlToImage(), data.getDescription(), data.getPublishedAt()));
                    }
                    readMoreView.setData(dataViewModels);
                } else {
                    readMoreView.setLoadMore(false);
                }
            } else if (object instanceof Features) {
                Features features = (Features) object;
                if (features.getArticles() != null) {
                    dataViewModels = new ArrayList<>();
                    dataArrayList.addAll(features.getArticles());
                    size = features.getArticles().size();
                    this.offset++;
                    Log.i("offset onsuccess", "offset => " + offset + " " + size);
                    readMoreView.offset(this.offset);
                    readMoreView.setFirstOpen(false);
                    readMoreView.setLoadMore(true);
                    for (int i = 0; i < size; i++) {
                        NewsAndFeaturesData data = features.getArticles().get(i);
                        dataViewModels.add(new DataViewModel(data.getTitle(), data.getUrlToImage(), data.getDescription(), data.getPublishedAt()));
                    }
                    readMoreView.setData(dataViewModels);
                } else {
                    readMoreView.setLoadMore(false);
                }
            }else if (object instanceof TopNews) {
                TopNews topNews = (TopNews) object;
                if (topNews.getArticles() != null) {
                    dataViewModels = new ArrayList<>();
                    dataArrayList.addAll(topNews.getArticles());
                    size = topNews.getArticles().size();
                    this.offset++;
                    Log.i("offset onsuccess", "offset => " + offset + " " + size);
                    readMoreView.offset(this.offset);
                    readMoreView.setFirstOpen(false);
                    readMoreView.setLoadMore(true);
                    for (int i = 0; i < size; i++) {
                        NewsAndFeaturesData data = topNews.getArticles().get(i);
                        dataViewModels.add(new DataViewModel(data.getTitle(), data.getUrlToImage(), data.getDescription(), data.getPublishedAt()));
                    }
                    readMoreView.setData(dataViewModels);
                } else {
                    readMoreView.setLoadMore(false);
                }
            }
            this.isLoading = false;
        }
    }

    @Override
    public void onFailure(Object object) {
        if (swipe) {
            swipe = false;
            readMoreView.hideSwipe();
        }
        readMoreView.showServerError();
        this.isLoading = false;
    }

    @Override
    public void onInvalidCredentials() {

    }

}
