package com.pharmacy.pharmacycare.ui.splash;

import android.content.Context;

import com.pharmacy.pharmacycare.R;
import com.pharmacy.pharmacycare.api.ApiCallBack;
import com.pharmacy.pharmacycare.api.ApiClient;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.NewsAndFeaturesData;
import com.pharmacy.pharmacycare.model.TopNews;
import com.pharmacy.pharmacycare.util.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by Mariam on 3/24/2018.
 */

public class SplashPresenterImpl implements SplashPresenter, ApiCallBack {
    private SplashView splashView;
    private ApiClient apiClient;
    private boolean getNewsDone = false, getFeaturesDone = false , getTopNewsDone = false;
    private ArrayList<NewsAndFeaturesData> newsData, featuresData, topNewsData;
    private int done = 0;


    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
        apiClient = ApiClient.getInstance();
        newsData = new ArrayList<>();
        featuresData = new ArrayList<>();
        topNewsData = new ArrayList<>();
    }

    @Override
    public void getNews() {
        if (!getNewsDone) {
            if (!NetworkUtil.getConnectivityStatusString((Context) splashView)) {
                splashView.showError(((Context) splashView).getString(R.string.fail_to_connect_to_internet));
                getNewsDone = false;
            } else {
                getNewsDone = true;
                apiClient.getNews(1, this);

            }
        }
    }

    @Override
    public void getFeatures() {
        if (!getFeaturesDone) {
            if (!NetworkUtil.getConnectivityStatusString((Context) splashView)) {
                splashView.showError(((Context) splashView).getString(R.string.fail_to_connect_to_internet));
                getFeaturesDone=false;
            } else {
                getFeaturesDone = true;
                apiClient.getFeatures(1, this);
            }
        }
    }

    @Override
    public void getTopNews() {
        if (!getTopNewsDone) {
            if (!NetworkUtil.getConnectivityStatusString((Context) splashView)) {
                splashView.showError(((Context) splashView).getString(R.string.fail_to_connect_to_internet));
                getTopNewsDone = false;
            } else {
                getTopNewsDone = true;
                apiClient.getTopNews(1, this);

            }
        }
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof News) {
            News news = (News) object;
            done++;
            for (int i = 0; i < news.getArticles().size(); i++) {
                newsData.add(news.getArticles().get(i));
                if (i == 1) break;
            }
        } else if (object instanceof Features) {
            done++;
            Features features = (Features) object;
            for (int i = 0; i < features.getArticles().size(); i++) {
                featuresData.add(features.getArticles().get(i));
                if (i == 1) break;
            }
        } else if (object instanceof TopNews) {
            done++;
            TopNews topNews = (TopNews) object;
            for (int i = 0; i < topNews.getArticles().size(); i++) {
                topNewsData.add(topNews.getArticles().get(i));
                if (i == 2) break;
            }
        }
        if (getFeaturesDone && getNewsDone && getTopNewsDone&& done == 3){
            splashView.getDataDone(newsData,featuresData,topNewsData);
        }

    }

    @Override
    public void onFailure(Object object) {
        if (object instanceof News) {
            getNewsDone = false;
            getNews();
        } else if(object instanceof Features){
            getFeaturesDone = false;
            getFeatures();
        }else if(object instanceof TopNews){
            getTopNewsDone = false;
            getTopNews();
        }
    }

    @Override
    public void onInvalidCredentials() {

    }
}
