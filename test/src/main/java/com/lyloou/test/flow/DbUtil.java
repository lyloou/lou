package com.lyloou.test.flow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lyloou.test.common.Constant;
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

    static void consumeCursorByDay(Context context, boolean isArchived, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(context).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME +
                " where is_archived=" + (isArchived ? Constant.TRUE : Constant.FALSE) + " ORDER BY day desc", null);
        if (cursor.moveToFirst()) {
            do {
                consumer.accept(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sd.close();
    }

    /**
     * 获取更新 items 的 consumer
     *
     * @param context 上下文
     * @return consumer
     */
    static Consumer<FlowDay> getUpdateItemsConsumer(Context context) {
        return (flowDay) -> {
            SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.COL_DAY, flowDay.getDay());
            contentValues.put(DbHelper.COL_ITEMS, FlowItemHelper.toJsonArray(flowDay.getItems()));
            contentValues.put(DbHelper.COL_IS_SYNCED, Constant.FALSE);
            sd.update(DbHelper.TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(flowDay.getId())});
            flowDay.setSynced(false);
            sd.close();
        };
    }

    static boolean updateArchived(Context context, String day, boolean archived) {
        SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_IS_ARCHIVED, archived ? Constant.TRUE : Constant.FALSE);
        int update = sd.update(DbHelper.TABLE_NAME, contentValues, "day=?", new String[]{day});
        sd.close();
        return update >= 0;
    }

    static boolean updateSynced(Context context, String day, boolean synced) {
        SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_IS_SYNCED, synced ? Constant.TRUE : Constant.FALSE);
        int update = sd.update(DbHelper.TABLE_NAME, contentValues, "day=?", new String[]{day});
        sd.close();
        return update >= 0;
    }

    static boolean delete(Context context, String day) {
        SQLiteDatabase sd = new DbHelper(context).getWritableDatabase();
        int delete = sd.delete(DbHelper.TABLE_NAME, "day = ?", new String[]{day});
        sd.close();
        return delete >= 0;
    }
}
