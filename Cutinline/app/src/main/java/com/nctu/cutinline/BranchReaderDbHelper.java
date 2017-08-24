package com.nctu.cutinline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jason on 2016/5/15.
 */
public class BranchReaderDbHelper extends SQLiteOpenHelper {
    /**
     * If you change the database schema, you must increment the database version.
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CutInLine.db";
    private static final String TAG = "BranchReaderDbHelper";

    public BranchReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BranchReaderContract.getSqlCreateEntries());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * This database is only a cache for online data, so its upgrade policy is
         * to simply to discard the data and start over
         */
        db.execSQL(BranchReaderContract.getSqlDeleteEntries());
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertBranchData(Branch branch) {
        /**
         * Gets the data repository in write mode
         */
        SQLiteDatabase db = this.getWritableDatabase();

        /**
         * Create a new map of values, where column names are the keys
         */
        ContentValues values = new ContentValues();
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_ID, branch.getBranch_id());
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_NAME, branch.getBranch_name());
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_CITY, branch.getBranch_city());
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_DISTRICT, branch.getBranch_district());
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_VILLAGE, branch.getBranch_village());
        values.put(BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_ADDRESS, branch.getBranch_address());

        /**
         * Insert the new row, returning the primary key value of the new row
         */
        long newRowId;
        newRowId = db.insert(
                BranchReaderContract.BranchEntry.TABLE_NAME, null,
                values);
        Log.d(TAG, "New row id is " + newRowId + ".");

    }

    public int getBranchCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + BranchReaderContract.BranchEntry.TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Branch getBranchItem(int branch_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Branch branch = new Branch();
        try {
            String QUERY = "SELECT * FROM " + BranchReaderContract.BranchEntry.TABLE_NAME +
                    " WHERE " + BranchReaderContract.BranchEntry.COLUMN_NAME_BRANCH_ID + " = " + String.valueOf(branch_id);
            Log.d(TAG,QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            Log.d(TAG,String.valueOf(cursor.getCount()));
            cursor.moveToNext();
            branch = new Branch(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            db.close();
            return branch;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch;
    }

    public ArrayList<Branch> getAllBranch() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Branch> arrayList = null;
        try {
            arrayList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + BranchReaderContract.BranchEntry.TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Branch branch = new Branch(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    arrayList.add(branch);
                }
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}