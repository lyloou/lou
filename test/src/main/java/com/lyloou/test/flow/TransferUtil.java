package com.lyloou.test.flow;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lyloou.test.common.Constant;
import com.lyloou.test.flow.net.FlowReq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class TransferUtil {

    static void sortItems(List<FlowItem> items) {
        Collections.sort(items, (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return -1;
            }
            String o1TimeStart = o1.getTimeStart();
            String o2TimeStart = o2.getTimeStart();
            if (o1TimeStart == null || o2TimeStart == null) {
                return -1;
            }
            return o2TimeStart.compareTo(o1TimeStart);
        });
    }


    static FlowDay getFlowDayByDay(Context context, String day) {
        FlowDay flowDay = new FlowDay();
        DbUtil.consumeCursorByDay(context, day, cursor -> {
            int count = cursor.getCount();
            if (count == 0) {
                return;
            }
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
            int isArchived = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_ARCHIVED));
            int isSynced = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_SYNCED));
            int isDisabled = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_DISABLED));
            List<FlowItem> flowItems = new LinkedList<>(FlowItemHelper.fromJsonArray(items));
            sortItems(flowItems);

            flowDay.setId(id);
            flowDay.setDay(day);
            flowDay.setItems(flowItems);
            flowDay.setArchived(isArchived == Constant.TRUE);
            flowDay.setSynced(isSynced == Constant.TRUE);
            flowDay.setDisabled(isDisabled == Constant.TRUE);
        });
        if (flowDay.getId() == null || flowDay.getId() == 0) {
            return null;
        }
        return flowDay;
    }

    static FlowReq transferToFlowReq(Context context, FlowDay flowDay) {
        flowDay = getFlowDayByDay(context, flowDay.getDay());
        FlowReq req = new FlowReq();
        req.setArchived(flowDay.isArchived());
        req.setDay(flowDay.getDay());
        req.setItem(FlowItemHelper.toJsonArray(flowDay.getItems()));
        req.setDisabled(flowDay.isDisabled());
        Log.e(TAG, "transferToFlowReq: +" + req.getDay() + ":" + req.getItem());
        return req;
    }

    static List<FlowReq> transferToFlowReqs(Context context, List<FlowDay> flowDays) {
        List<FlowReq> flowReqs = new ArrayList<>();
        for (FlowDay day : flowDays) {
            flowReqs.add(transferToFlowReq(context, day));
        }
        return flowReqs;
    }

    private static final String TAG = TransferUtil.class.getSimpleName();


    static List<FlowDay> transferCursorToFlowDays(Cursor cursor) {
        List<FlowDay> flowDays = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                flowDays.add(transferCursorToFlowDay(cursor));
            } while (cursor.moveToNext());
        }
        return flowDays;
    }

    static FlowDay transferCursorToFlowDay(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
        String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
        String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
        int isArchived = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_ARCHIVED));
        int isSynced = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_SYNCED));
        int isDisabled = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_DISABLED));
        FlowDay flowDay = new FlowDay();
        List<FlowItem> flowItems = new ArrayList<>(FlowItemHelper.fromJsonArray(items));
        flowDay.setId(id);
        flowDay.setDay(day);
        flowDay.setItems(flowItems);
        flowDay.setArchived(isArchived == Constant.TRUE);
        flowDay.setSynced(isSynced == Constant.TRUE);
        flowDay.setDisabled(isDisabled == Constant.TRUE);
        sortItems(flowItems);
        return flowDay;
    }
}
