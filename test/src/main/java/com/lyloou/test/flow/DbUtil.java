package com.lyloou.test.flow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lyloou.test.common.Consumer;

class DbUtil {
    static long insertFlowDayToDb(Context context, FlowDay flowDay) {
        SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_DAY, flowDay.getDay());
        contentValues.put(DbHelper.COL_ITEMS, FlowItemHelper.toJsonArray(flowDay.getItems()));
        long id = sd.insert(DbHelper.TABLE_NAME, null, contentValues);
        sd.close();
        return id;
    }

    static void consumeCursorByDay(Context context, String day, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME + " where day = ?", new String[]{day});
        cursor.moveToFirst();
        consumer.accept(cursor);
        cursor.close();
        sd.close();
    }

    static void consumeCursorById(Context context, int id, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME + " where id = ?", new String[]{"" + id});
        cursor.moveToFirst();
        consumer.accept(cursor);
        cursor.close();
        sd.close();
    }

    static void consumeCursorByDay(Context context, boolean isDisabled, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select id,day,is_disabled from " + DbHelper.TABLE_NAME +
                " where is_disabled=" + (isDisabled ? 1 : 0) + " ORDER BY day desc", null);
        if (cursor.moveToFirst()) {
            do {
                consumer.accept(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sd.close();
    }

    static Consumer<FlowDay> getUpdateFlowDayConsumer(Context context) {
        return (consume) -> {
            SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.COL_DAY, consume.getDay());
            contentValues.put(DbHelper.COL_ITEMS, FlowItemHelper.toJsonArray(consume.getItems()));
            sd.update(DbHelper.TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(consume.getId())});
            sd.close();
        };
    }

    static boolean toggleDisabledFlowDay(Context context, String day, boolean delete) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_IS_DISABLED, delete ? 1 : 0);
        int update = sd.update(DbHelper.TABLE_NAME, contentValues, "day=?", new String[]{day});
        sd.close();
        return update >= 0;
    }

    static boolean deepDelete(Context context, String day, Consumer<Integer> consumer) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        int delete = sd.delete(DbHelper.TABLE_NAME, "day = ?", new String[]{day});
        consumer.accept(delete);
        sd.close();
        return delete >= 0;
    }
}
