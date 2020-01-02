package com.lyloou.test.flow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

class DbHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 3;
    final static String DB_NAME = "flow_time.db";
    final static String TABLE_NAME = "flow_time";
    final static String COL_ID = "id";
    final static String COL_DAY = "day";
    final static String COL_ITEMS = "items";
    final static String COL_IS_ARCHIVED = "is_archived";
    final static String COL_IS_SYNCED = "is_synced";
    final static String COL_IS_DISABLED = "is_disabled";

    private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            COL_ID + "  INTEGER NOT NULL, " +
            COL_DAY + "  TEXT(20) NOT NULL, " +
            COL_ITEMS + " TEXT(0), " +
            COL_IS_ARCHIVED + " INTEGER DEFAULT 0, " +
            COL_IS_SYNCED + " INTEGER DEFAULT 0, " +
            COL_IS_DISABLED + " INTEGER DEFAULT 0, " +
            "  PRIMARY KEY (id) " +
            ");";

    private final static String CREATE_INDEX = "" +
            "CREATE INDEX idx_day " +
            "ON " + TABLE_NAME + " ( " +
            "  " + COL_DAY + " COLLATE BINARY DESC " +
            ");";

    DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_INDEX);
                db.execSQL("alter table " + TABLE_NAME + " add column " + COL_IS_DISABLED + " integer DEFAULT 0 ");
            case 2:
                db.execSQL("alter table " + TABLE_NAME + " add column " + COL_IS_ARCHIVED + " integer DEFAULT 0 ");
                db.execSQL("alter table " + TABLE_NAME + " add column " + COL_IS_SYNCED + " integer DEFAULT 0 ");
            default:
        }
    }
}
