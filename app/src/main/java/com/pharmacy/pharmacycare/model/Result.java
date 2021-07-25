package com.pharmacy.pharmacycare.model;


/**
 * Created by Mariam on 8/15/2017.
 */

public class Result {

    private int eventType;
    private Object data;

    public Result(int eventType) {
        this.eventType = eventType;
    }

    public Result( int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public int getEvent() {
        return eventType;
    }

    public void setEvent(int error) {
        this.eventType = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
