package com.lyloou.test.flow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "flow_time.db";
    public final static String TABLE_NAME = "flow_time";
    public final static String COL_ID = "id";
    public final static String COL_DAY = "day";
    public final static String COL_ITEMS = "items";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + "  INTEGER NOT NULL, " +
                COL_DAY + "  TEXT(20) NOT NULL, " +
                COL_ITEMS + " TEXT(0), " +
                "  PRIMARY KEY (id) " +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
