package com.pharmacy.pharmacycare.data;

import android.provider.BaseColumns;

/**
 * Created by Mariam on 10/11/2017.
 */

public class PharmacyContract {


    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user_entry";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_FIRST_NAME = "fname";
        public static final String COLUMN_USER_LAST_NAME = "lname";
        public static final String COLUMN_USER_PHONE = "phone";
        public static final String COLUMN_USER_BIRTHDAY = "birthday";
    }


    public static final class RxEntry implements BaseColumns{
        public static final String TABLE_NAME = "rx_entry";
        public static final String COLUMN_RX_ID = "user_number";
        public static final String COLUMN_RX_NUMBER = "rx_number";
        public static final String COLUMN_MEDICATION_NAME = "medication_name";
    }

}
