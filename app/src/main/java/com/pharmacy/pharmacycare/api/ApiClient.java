package com.pharmacy.pharmacycare.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pharmacy.pharmacycare.api.methods.FeaturesService;
import com.pharmacy.pharmacycare.api.methods.NewsService;
import com.pharmacy.pharmacycare.api.methods.TopNewsService;
import com.pharmacy.pharmacycare.model.Features;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.TopNews;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mahmoudhedya on 7/19/17.
 */
public class ApiClient {

    private static ApiClient apiClient;
    private static Retrofit retrofit = null;

    public static ApiClient getInstance() {
        if (apiClient != null) {
            return apiClient;
        } else {
            apiClient = new ApiClient();
            return apiClient;
        }
    }

    private void prepareRetrofit() {
        okhttp3.OkHttpClient.Builder httpClientBuilder = new okhttp3.OkHttpClient.Builder();
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(logging);

        okhttp3.OkHttpClient httpClient = httpClientBuilder.addInterceptor(new okhttp3.Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder().build();
                return chain.proceed(request);
            }
        }).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiUrls.BASE_URL).client(httpClient).build();
    }

    private void prepareRetrofit(final String token) {
        okhttp3.OkHttpClient.Builder httpClientBuilder = new okhttp3.OkHttpClient.Builder();
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.addInterceptor(logging);

        okhttp3.OkHttpClient httpClient = httpClientBuilder.addInterceptor(new okhttp3.Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder()
                        .addHeader(ApiKeys.authorization
                                , "Bearer " + token).build();

                return chain.proceed(request);
            }
        }).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(ApiUrls.BASE_URL).client(httpClient).build();
    }

    /**
     * top news request
     */
    public void getTopNews( int offset, final ApiCallBack apiCallBack) {
        prepareRetrofit();
        TopNewsService getNews = retrofit.create(TopNewsService.class);
        Call<TopNews> newsCall = getNews.execute(offset);
        //asynchronous call
        newsCall.enqueue(new Callback<TopNews>() {
            @Override
            public void onResponse(Call<TopNews> call, Response<TopNews> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("ok"))
                        apiCallBack.onSuccess(response.body());
                    else apiCallBack.onFailure(call);
                } else {
                    apiCallBack.onFailure(call);
                }
            }

            @Override
            public void onFailure(Call<TopNews> call, Throwable t) {
                apiCallBack.onFailure(call);
            }
        });
    }



    /**
     * news request
     */
    public void getNews( int offset, final ApiCallBack apiCallBack) {
         prepareRetrofit();
        NewsService getNews = retrofit.create(NewsService.class);
        Call<News> newsCall = getNews.execute(offset);
        //asynchronous call
        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("ok"))
                        apiCallBack.onSuccess(response.body());
                    else apiCallBack.onFailure(call);
                } else {
                      apiCallBack.onFailure(call);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                apiCallBack.onFailure(call);
            }
        });
    }

    /**
     * features request
     */
    public void getFeatures( int offset, final ApiCallBack apiCallBack) {
        prepareRetrofit();
        FeaturesService getFeatures = retrofit.create(FeaturesService.class);
        Call<Features> newsCall = getFeatures.execute(offset);
        //asynchronous call
        newsCall.enqueue(new Callback<Features>() {
            @Override
            public void onResponse(Call<Features> call, Response<Features> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("ok"))
                    apiCallBack.onSuccess(response.body());
                    else apiCallBack.onFailure(call);
                } else {
                    apiCallBack.onFailure(call);
                }
            }

            @Override
            public void onFailure(Call<Features> call, Throwable t) {
                apiCallBack.onFailure(call);
            }
        });
    }
}

