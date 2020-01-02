package com.lyloou.test.flow;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetForGridLayoutManagerDecoration;
import com.lyloou.test.util.Uscreen;

import java.util.Set;

public class Part {

    private Adapter mAdapter;
    private View mView;
    private String mTitle;

    Part(Context context, String title, Set<FlowDay> mFlowDays) {
        mTitle = title;
        mView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        RecyclerView recyclerView = mView.findViewById(R.id.rv_list);
        mAdapter = new Adapter(context, mFlowDays);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.addItemDecoration(new ItemOffsetForGridLayoutManagerDecoration(Uscreen.dp2Px(context, 16)));
    }

    public String getTitle() {
        return mTitle;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public View getView() {
        return mView;
    }
}
