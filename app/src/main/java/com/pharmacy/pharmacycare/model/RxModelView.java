package com.pharmacy.pharmacycare.model;

import android.database.Cursor;

import com.pharmacy.pharmacycare.data.PharmacyContract;

/**
 * Created by Mariam on 3/27/2018.
 */

public class RxModelView {
    String rx="";
    String medicationName="";


    public RxModelView() {
    }

    public RxModelView(String rx, String medicationName) {
        this.rx = rx;
        this.medicationName = medicationName;
    }

    public RxModelView(Cursor cursor) {
        this(cursor.getString(cursor.getColumnIndex(PharmacyContract.RxEntry.COLUMN_RX_NUMBER)),
                cursor.getString(cursor.getColumnIndex(PharmacyContract.RxEntry.COLUMN_MEDICATION_NAME))
        );
    }

    public String getRx() {
        return rx;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
}
