package com.pharmacy.pharmacycare.api;

/**
 * Created by mahmoudhedya on 7/19/17.
 */
public interface ApiUrls {
    String BASE_URL = "https://newsapi.org/v2/"; // stage

    String TOP_NEWS = "top-headlines?category=health&country=ca&apiKey=8e53013eaa304666ba90513a5bd343ad&pageSize=10";
    String NEWS = "everything?q=health&apiKey=8e53013eaa304666ba90513a5bd343ad&pageSize=10";
    String FEATURES = "everything?q=fitness&apiKey=8e53013eaa304666ba90513a5bd343ad&pageSize=10";
}