package com.lyloou.test.flow;

import android.content.Context;

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
            List<FlowItem> mFlowItems = new LinkedList<>();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
            mFlowItems.addAll(FlowItemHelper.fromJsonArray(items));
            sortItems(mFlowItems);

            flowDay.setId(id);
            flowDay.setDay(day);
            flowDay.setItems(mFlowItems);
        });
        if (flowDay.getId() == null || flowDay.getId() == 0) {
            return null;
        }
        return flowDay;
    }
}
