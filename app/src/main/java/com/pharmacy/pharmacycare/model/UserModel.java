package com.pharmacy.pharmacycare.model;

import android.database.Cursor;

import com.pharmacy.pharmacycare.data.PharmacyContract;

/**
 * Created by Mariam on 3/30/2018.
 */

public class UserModel {
    int id = -1;
    String fName;
    String lName;
    String phone;
    String birthday;

    public UserModel(int id, String fName, String lName, String phone, String birthday) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.birthday = birthday;
    }

    public UserModel(Cursor cursor) {
        this(   cursor.getInt(cursor.getColumnIndex(PharmacyContract.UserEntry._ID)),
                cursor.getString(cursor.getColumnIndex(PharmacyContract.UserEntry.COLUMN_USER_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(PharmacyContract.UserEntry.COLUMN_USER_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(PharmacyContract.UserEntry.COLUMN_USER_PHONE)),
                cursor.getString(cursor.getColumnIndex(PharmacyContract.UserEntry.COLUMN_USER_BIRTHDAY))
             );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
