package com.lou.as.lou;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lyloou.lou.adapter.LouAdapter;
import com.lyloou.lou.view.PullToRefreshView;

import java.util.ArrayList;

public class ViewPullToRefreshViewActivity extends Activity {
    private LouAdapter<String> mPtrLvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pull_to_refresh_view);

        final PullToRefreshView ptrLv = (PullToRefreshView) findViewById(R.id.ptrlv);
        ptrLv.setAdapter(mPtrLvAdapter = new LouAdapter<String>(ptrLv, android.R.layout.simple_list_item_1) {
            @Override
            protected void assign(ViewHolder holder, String s) {
                holder.putText(android.R.id.text1, s);
            }
        });

        ArrayList<String> strs = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            strs.add("Item:" + i);
        }
        mPtrLvAdapter.initList(strs);

        ptrLv.setHeadView(R.layout.item_view_pull_to_refresh_view);
        ptrLv.setOnChangeStatusListener(new PullToRefreshView.OnChangeStatusListener() {
            @Override
            public void changeStatus(int status, int currentPaddingTop) {
                TextView tips = (TextView) ptrLv.getHeadView().findViewById(R.id.tv_scan_pull_to_refresh);
                switch (status) {
                    case PullToRefreshView.OnChangeStatusListener.STATUS_PULL:
                        tips.setText("下拉刷新");
                        break;
                    case PullToRefreshView.OnChangeStatusListener.STATUS_RELEASE:
                        tips.setText("松开以刷新");
                        break;
                    case PullToRefreshView.OnChangeStatusListener.STATUS_RUNNING:
                        tips.setText("正在加载...");
                        break;
                    case PullToRefreshView.OnChangeStatusListener.STATUS_COMPLETE:
                        tips.setText("加载完成");
                        break;
                }
            }

            @Override
            public void loadData() {
                ptrLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrLvAdapter.addItem("I'm New");

                        changeStatus(PullToRefreshView.OnChangeStatusListener.STATUS_COMPLETE, 0);
                        ptrLv.recover(true);
                    }
                }, 2000);
            }
        });
    }
}
