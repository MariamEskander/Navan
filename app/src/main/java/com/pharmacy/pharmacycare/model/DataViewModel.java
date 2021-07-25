package com.pharmacy.pharmacycare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariam on 3/24/2018.
 */

public class DataViewModel {

    private String title;
    private String fieldPhoto;
    private String fieldSubTitle;
    private String created;

    public DataViewModel(String title, String fieldPhoto, String fieldSubTitle, String created) {
        this.title = title;
        this.fieldPhoto = fieldPhoto;
        this.fieldSubTitle = fieldSubTitle;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFieldPhoto() {
        return fieldPhoto;
    }

    public void setFieldPhoto(String fieldPhoto) {
        this.fieldPhoto = fieldPhoto;
    }

    public String getFieldSubTitle() {
        return fieldSubTitle;
    }

    public void setFieldSubTitle(String fieldSubTitle) {
        this.fieldSubTitle = fieldSubTitle;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


}
