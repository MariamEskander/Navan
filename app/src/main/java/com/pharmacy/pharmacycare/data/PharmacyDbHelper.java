package com.pharmacy.pharmacycare.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import com.pharmacy.pharmacycare.util.MyPharmacy;


/**
 * Created by Mariam on 10/11/2017.
 */

public class PharmacyDbHelper {

    private static final String DATABASE_NAME = "pharmacy.db";
    private static final int DATABASE_VERSION = 1;

    public static PharmacyDbHelper instance;
    private static SQLiteDatabase perfectNeedsDb;
    private static DbOpenHelper DbHelper;

    /**
     * singleton ileadsDB.
     *
     * @param context
     * @return CrewCrew database instance.
     */
    public static PharmacyDbHelper GetFor(Context context) {
        if (instance == null)
            instance = new PharmacyDbHelper(context);
        return instance;
    }

    public PharmacyDbHelper(Context _context) {
        synchronized (DATABASE_NAME) {
            if (DbHelper == null)
                DbHelper = new DbOpenHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    }


    public void closeDatabase() {
        if (perfectNeedsDb != null && perfectNeedsDb.isOpen()) {
            perfectNeedsDb.close();
        }
    }

    public boolean open() {
        /** Other thread in loop still one thread close db. */
        while (!MyPharmacy.lc.tryLock()) {
            //Log.d("Open DB method in side while loop.", "Open DB method in side while loop Thread Id: " + Thread.currentThread().getId());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
        try {
            //Log.d("Before DB Open", "Before DB Open Thread Id: " + Thread.currentThread().getId());
            perfectNeedsDb = null;
            perfectNeedsDb = DbHelper.getWritableDatabase();
            MyPharmacy.dbcount++;
            //Log.d("After DB Open", "After DB Open Thread Id: " + Thread.currentThread().getId() + " DB Counter :" + GlobalVariables.dbcount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void close() {
        try {
            //Log.d("Before DB Close", "Before DB Close Thread Id: " + Thread.currentThread().getId());
            if (perfectNeedsDb != null)
                perfectNeedsDb.close();
            MyPharmacy.dbcount--;
            /** unlock after close db. */
            MyPharmacy.lc.unlock();
            //Log.d("After DB Close", "After DB Close Thread Id: " + Thread.currentThread().getId() + " DB Counter :" + GlobalVariables.dbcount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * insert a record into particular table
     *
     * @param tablename
     * @param values
     * @returnthe row ID of the newly inserted row, or -1 if an error occurred
     */
    public synchronized long insert(String tablename, ContentValues values) {
        Log.i(tablename, "insert");
        return perfectNeedsDb.insert(tablename, null, values);
    }

    /**
     * try to insert a record into particular table. <br/>
     * throw SQLiteConstraintException if it happened. ignore other expections.
     *
     * @param tablename
     * @param values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     * @throws SQLiteConstraintException
     */
    public synchronized long insertOrThrow(String tablename, ContentValues values) throws SQLiteConstraintException {
        try {
            return perfectNeedsDb.insertOrThrow(tablename, null, values);
        } catch (SQLiteConstraintException e) {
            throw e;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * physically delete a record in a particular table according to a specified
     * id number
     *
     * @param tablename
     * @param idField   id field name
     * @param id
     * @return whether the deletion done correctly.
     */
    public synchronized boolean removeRecordByID(String tablename, String idField, long id) {
        return perfectNeedsDb.delete(tablename, String.format("%s=%d", idField, id), null) > 0;
    }

    /**
     * physically delete record(s) in particular table according to a query.
     *
     * @param tablename
     * @param whereClause
     * @return whether the deletion done correctly.
     */
    public synchronized boolean removeRecord(String tablename, String whereClause, String[] whereValue) {
        return perfectNeedsDb.delete(tablename, whereClause, whereValue) > 0;
    }

    public synchronized int removeRecordsNotIn(String sql, String whereClause) {
        return perfectNeedsDb.rawQuery(sql, null).getCount();
    }

    public synchronized boolean removedData(String tablename) {
        return perfectNeedsDb.delete(tablename, null, null) > 0;
    }


    /**
     * execute sql statement without throw any exception.
     *
     * @param sql
     */
    public synchronized void execSQL(String sql) {
        try {
            perfectNeedsDb.execSQL(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean execSQLWithException(String sql, Context paraContext, int count_current, Handler mHandler, int notErrorsListR) {
        try {
            perfectNeedsDb.execSQL(sql);
            //Log.d("ws",String.format("execSQLWithException - iLeadsDb.execSQL Successful = %s", sql));
            return true;
        } catch (SQLiteException sqlexcp) {
            String notErrorsList[] = paraContext.getResources().getStringArray(notErrorsListR);
            boolean not_error_result = false;
            if (count_current < notErrorsList.length) {
                String notAnError = notErrorsList[count_current];
                //ArrayList<String> notErrorArraylist=new ArrayList<String>();
                // check to see if we have more than one notAnError statement in notAnError
                String notErrorArraylist[] = notAnError.split(";");
                for (String not_errorItem : notErrorArraylist) {
                    //Log.d("ws",String.format("execSQLWithException - not_errorItem = %s", not_errorItem));
                    //Log.d("ws",String.format("execSQLWithException - sqlexcp.toString() = %s", sqlexcp.toString()));
                    if (not_errorItem.length() > 0) {
                        // check if we have another SQL statement to execute in response to the error.
                        // no error string in notErrorsList may contains a statement to be execute
                        // to recover the error next time the same statement is executed
                        // for example Copying an entire table's data into another one that
                        // had an index on some of its columns may be interrupted. when we try to restart
                        // the copy again, the copy will fail because some of the rows are already
                        // in the table. we may choose to delete the entire table so that we try to start
                        // copying again it works.
                        String not_errorParts[] = not_errorItem.split("::");
                        String not_error = not_errorParts[0];
                        //Log.d("ws",String.format("execSQLWithException - not_errorParts[0] = %s", not_errorParts[0]));
                        if (sqlexcp.toString().toLowerCase().contains(not_error.toLowerCase())) {
                            //Log.d("ws",String.format("execSQLWithException - not_error = %s", not_error));
                            if (not_errorParts.length > 1) {
                                //Log.d("ws",String.format("execSQLWithException - calling execSQL with = %s", not_errorParts[1]));
                                perfectNeedsDb.execSQL(not_errorParts[1]);
                            } else {
                                not_error_result = true;
                            }
                            break;
                        }
                    }
                }
            }
            if (not_error_result) {
                return true;
            } else {
                //Log.d("ws",String.format("execSQLWithException - UPGRADE_ILEADS_DB_FAIL"));
//					SplashActivity.call_main_screen = false;
//					msg = mHandler.obtainMessage(SplashActivity.UPGRADE_ILEADS_DB_FAIL);
//					Bundle bundle = new Bundle();
//					bundle.putString(paraContext.getString(R.string.Upgrade_Fail_Msg), sqlexcp.toString());
//					msg.setData(bundle);
//					mHandler.sendMessage(msg);
                return false;
            }
        }
    }

    public synchronized Cursor execQuerySQL(String sql, String[] selectionArgs) {
        return perfectNeedsDb.rawQuery(sql, selectionArgs);
    }

    /**
     * update values for a record with particular id number in a table without
     * throw any exception
     *
     * @param tablename
     * @param values
     * @param idField   id field name
     * @param index     record unique id.
     * @return whether the update operation done correctly.
     */
    public synchronized boolean updateRecord(String tablename, ContentValues values, String idField, long index) {
        try {
            return perfectNeedsDb.update(tablename, values, String.format("%s=%d", idField, index), null) > 0;
        } catch (Exception ex) {
            ex.toString();
            return false;
        }
    }

    /**
     * update values for sql query specified record in a table.
     *
     * @param tablename
     * @param values
     * @param where     where clause
     * @return whether the update operation done correctly.
     */
    public synchronized boolean updateRecord(String tablename, ContentValues values, String where, String[] whereArgs) {
        Log.i(tablename, "update");
        return perfectNeedsDb.update(tablename, values, where, whereArgs) > 0;
    }

    public synchronized boolean updateRecord(String tablename, ContentValues values) {
        return perfectNeedsDb.update(tablename, values, null, null) > 0;
    }


    public synchronized long insertOrUpdate(String tablename, ContentValues values, String where, String[] whereArgs) {
        if (!updateRecord(tablename, values, where, whereArgs)) {
            return insert(tablename, values);
        }
        return -1;
    }

    /**
     * get all records in a particular table.
     *
     * @param tablename
     * @return a cursor object pointed to all records in a particular table.
     */
    public synchronized Cursor getAllRecord(String tablename) {
        return perfectNeedsDb.query(tablename, null, null, null, null, null, null);
    }

    /**
     * get all records in a particular table with order.
     *
     * @param tablename
     * @return a cursor object pointed to all records in a particular table according to order if there was one.
     */
    public synchronized Cursor getAllRecordWithOrder(String tablename, String orderedBy) {
        return perfectNeedsDb.query(tablename, null, null, null, null, null, orderedBy);
    }

    public String exportTableData(String tablename) {
        StringBuilder string = new StringBuilder();
        string.append(tablename + "\n");
        Cursor c = perfectNeedsDb.query(tablename, null, null, null, null, null, null);
        for (String col : c.getColumnNames()) {
            string.append("\"");
            string.append(col);
            string.append("\",");
        }
        string.append("\n");
        while (c.moveToNext()) {
            for (int j = 0; j < c.getColumnCount(); j++) {
                if (j != 0)
                    string.append(",");
                string.append("\"");
                string.append(c.getString(j));
                string.append("\"");
            }
            string.append("\n");
        }
        string.append("\n\n");
        c.close();
        return string.toString();
    }

    /**
     * get records in a particular table according to sql query
     *
     * @param tablename
     * @param selection sql query for where clause
     * @return a cursor object pointed to the record(s) selected by sql query.
     */
    public synchronized Cursor getRecordBySelection(String tablename, String selection) {
        return perfectNeedsDb.query(tablename, null, selection, null, null, null, null);
    }

    public synchronized Cursor getRecordBySelection(String tablename, String selection, String selectionArgs) {
        return perfectNeedsDb.query(tablename, null, selection, new String[]{selectionArgs}, null, null, null);
    }

    public static class DbOpenHelper extends SQLiteOpenHelper {

        DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String SQL_CREATE_USER_TABLE = "CREATE TABLE " +
                    PharmacyContract.UserEntry.TABLE_NAME + " ( " +
                    PharmacyContract.UserEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    PharmacyContract.UserEntry.COLUMN_TITLE + "  TEXT NOT NULL , " +
                    PharmacyContract.UserEntry.COLUMN_USER_FIRST_NAME + "  TEXT NOT NULL , " +
                    PharmacyContract.UserEntry.COLUMN_USER_LAST_NAME + " TEXT NOT NULL , " +
                    PharmacyContract.UserEntry.COLUMN_USER_PHONE + " TEXT NOT NULL , " +
                    PharmacyContract.UserEntry.COLUMN_USER_BIRTHDAY + " TEXT NOT NULL " + " );";


            final String SQL_CREATE_RX_TABLE = "CREATE TABLE " +
                    PharmacyContract.RxEntry.TABLE_NAME + " ( " +
                    PharmacyContract.RxEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    PharmacyContract.RxEntry.COLUMN_RX_ID + "  INTEGER , " +
                    PharmacyContract.RxEntry.COLUMN_RX_NUMBER + "  TEXT , " +
                    PharmacyContract.RxEntry.COLUMN_MEDICATION_NAME + " TEXT ,"+
                    " FOREIGN KEY " + " ( " +PharmacyContract.RxEntry.COLUMN_RX_ID +" ) "+ " REFERENCES " +
                         PharmacyContract.UserEntry.TABLE_NAME + " ( " + PharmacyContract.UserEntry._ID +" ) " +" );";



            try {
                sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_RX_TABLE);
            } catch (Exception ex) {
                System.err.print(ex.toString());
            }

        }


        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PharmacyContract.UserEntry.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PharmacyContract.RxEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
