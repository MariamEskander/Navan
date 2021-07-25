package com.pharmacy.pharmacycare.data;

/**
 * Created by Mariam on 10/11/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.pharmacy.pharmacycare.model.RefillModel;
import com.pharmacy.pharmacycare.model.RxModelView;
import com.pharmacy.pharmacycare.model.UserModel;

import java.util.ArrayList;


public class DataBaseUtil {

    private static ContentValues cv2;

    public static void insertRefill(PharmacyDbHelper db, RefillModel refillModel) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(PharmacyContract.UserEntry.COLUMN_TITLE, refillModel.getTitle());
            cv.put(PharmacyContract.UserEntry.COLUMN_USER_FIRST_NAME, refillModel.getUserModel().getfName());
            cv.put(PharmacyContract.UserEntry.COLUMN_USER_LAST_NAME, refillModel.getUserModel().getlName());
            cv.put(PharmacyContract.UserEntry.COLUMN_USER_BIRTHDAY, refillModel.getUserModel().getBirthday());
            cv.put(PharmacyContract.UserEntry.COLUMN_USER_PHONE, refillModel.getUserModel().getPhone());

            db.open();
            int i = (int) db.insert(PharmacyContract.UserEntry.TABLE_NAME, cv);

            for (int j =0 ; j< refillModel.getRxModelViews().size();j++){
                cv2 = new ContentValues();
                cv2.put(PharmacyContract.RxEntry.COLUMN_RX_ID, i);
                cv2.put(PharmacyContract.RxEntry.COLUMN_RX_NUMBER, refillModel.getRxModelViews().get(j).getRx());
                cv2.put(PharmacyContract.RxEntry.COLUMN_MEDICATION_NAME,  refillModel.getRxModelViews().get(j).getMedicationName());
                db.insert(PharmacyContract.RxEntry.TABLE_NAME, cv2);
            }
            db.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            Log.i("Database", "insertOrUpdateUser Error");
        }
    }

    public static ArrayList<RefillModel> getRefills(PharmacyDbHelper db) {
        Cursor cursor = null;
        Cursor cursor1 = null;
        ArrayList<RefillModel> refills = null;
        ArrayList<RxModelView> rxModelViews;
        db.open();
        try {
            cursor = db.getAllRecord(PharmacyContract.UserEntry.TABLE_NAME);
            if (cursor != null && cursor.getCount() > 0) {
                refills = new ArrayList<>();
                while (cursor.moveToNext()) {
                    UserModel userModel = new UserModel(cursor);
                    rxModelViews = new ArrayList<>();
                    cursor1 = db.getRecordBySelection(PharmacyContract.RxEntry.TABLE_NAME,
                            PharmacyContract.RxEntry.COLUMN_RX_ID + " == " + userModel.getId());
                    if (cursor1 != null && cursor1.getCount() > 0) {
                        while (cursor1.moveToNext()) {
                            RxModelView rxModelView = new RxModelView(cursor1);
                            rxModelViews.add(rxModelView);
                        }
                    }
                        refills.add(new RefillModel(
                                cursor.getString(cursor.getColumnIndex(PharmacyContract.UserEntry.COLUMN_TITLE)),
                                userModel,rxModelViews));
                }
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
            Log.i("database", "get all");
        } finally {
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return refills;
    }


}