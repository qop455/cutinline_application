package com.nctu.cutinline;

import android.provider.BaseColumns;

/**
 * Created by jason on 2016/5/15.
 */
public final class BranchReaderContract {
    private static final String TAG = "BranchReaderContract";
    /**
     * Create table
     */
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BranchEntry.TABLE_NAME + " (" +
                    BranchEntry._ID + " INTEGER PRIMARY KEY," +
                    BranchEntry.COLUMN_NAME_BRANCH_ID + INTEGER_TYPE + COMMA_SEP +
                    BranchEntry.COLUMN_NAME_BRANCH_NAME + TEXT_TYPE + COMMA_SEP +
                    BranchEntry.COLUMN_NAME_BRANCH_CITY + TEXT_TYPE + COMMA_SEP +
                    BranchEntry.COLUMN_NAME_BRANCH_DISTRICT + TEXT_TYPE + COMMA_SEP +
                    BranchEntry.COLUMN_NAME_BRANCH_VILLAGE + TEXT_TYPE + COMMA_SEP +
                    BranchEntry.COLUMN_NAME_BRANCH_ADDRESS + TEXT_TYPE +
                    " )";
    /**
     * Drop table
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BranchEntry.TABLE_NAME;

    /**
     * To prevent someone from accidentally instantiating the contract class,
     * give it an empty constructor.
     */
    public BranchReaderContract() {
    }

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }

    public static String getSqlDeleteEntries() {
        return SQL_DELETE_ENTRIES;
    }

    /**
     * Inner class that defines the table contents
     */
    public static abstract class BranchEntry implements BaseColumns {
        public static final String TABLE_NAME = "branch";
        public static final String COLUMN_NAME_BRANCH_ID = "branch_id";
        public static final String COLUMN_NAME_BRANCH_NAME = "branch_name";
        public static final String COLUMN_NAME_BRANCH_CITY = "branch_city";
        public static final String COLUMN_NAME_BRANCH_DISTRICT = "branch_district";
        public static final String COLUMN_NAME_BRANCH_VILLAGE = "branch_village";
        public static final String COLUMN_NAME_BRANCH_ADDRESS = "branch_address";
    }
}