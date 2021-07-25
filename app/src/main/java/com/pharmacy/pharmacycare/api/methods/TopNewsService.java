package com.pharmacy.pharmacycare.api.methods;

import com.pharmacy.pharmacycare.api.ApiKeys;
import com.pharmacy.pharmacycare.api.ApiUrls;
import com.pharmacy.pharmacycare.model.News;
import com.pharmacy.pharmacycare.model.TopNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mariam on 3/24/2018.
 */

public interface TopNewsService {
    @GET(ApiUrls.TOP_NEWS)
    Call<TopNews> execute(@Query(ApiKeys.page) int page);
}

