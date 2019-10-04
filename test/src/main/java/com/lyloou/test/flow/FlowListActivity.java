/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.flow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.webview.WebActivity;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Udialog;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Uview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2019.09.25 10:34
 * <p>
 * Description: 时间流列表
 */
public class FlowListActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    private List<FlowDay> mFlowDays = new ArrayList<>();
    private Activity mContext;
    private Adapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow_list);

        initData();
        initView();
    }


    private void initData() {
        consumeCursorByDay(cursor -> {
            FlowDay fd = new FlowDay();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            fd.setId(id);
            fd.setDay(day);
            mFlowDays.add(fd);
        });
        Collections.sort(mFlowDays, (o1, o2) -> -o1.getDay().compareTo(o2.getDay()));
    }

    private void consumeCursorByDay(Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(this).getReadableDatabase();
        Cursor cursor = sd.rawQuery("select id,day from " + DbHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                consumer.accept(cursor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sd.close();
    }

    private void initView() {
        initTopPart();

        RecyclerView recyclerView = findViewById(R.id.rv_main);

        mAdapter = new Adapter(this, mFlowDays);
        mAdapter.setListener(new Listener() {
            @Override
            public void onItemClicked(FlowDay flowDay) {
                FlowActivity.start(mContext, flowDay.getDay());
            }

            @Override
            public void onItemLongClicked(FlowDay flowDay) {
                showDeleteAlert(flowDay);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    private void showDeleteAlert(FlowDay flowDay) {
        String message = "确认删除：\n".concat(flowDay.getDay());
        Udialog.alert(mContext, message, ok -> {
            if (ok) {
                consumeCursorByDayForDelete(flowDay.getDay(), count -> mAdapter.remove(flowDay));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initTopPart() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(this, toolbar);

        TextView tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText("大写的时间");

        View fab = findViewById(R.id.fab);
        fab.startAnimation(Uanimation.getRotateAnimation(3600));
        fab.setOnClickListener(view -> {
            toFlowActivity();
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    interface Listener {
        void onItemClicked(FlowDay flowDay);

        void onItemLongClicked(FlowDay flowDay);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        final List<FlowDay> list;
        final Context context;
        Listener mListener;

        public void setListener(Listener mListener) {
            this.mListener = mListener;
        }

        Adapter(Context context, List<FlowDay> list) {
            this.context = context;
            this.list = list;
        }

        public void remove(FlowDay flowDay) {
            if (list == null) {
                return;
            }
            list.remove(flowDay);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flow_list, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FlowDay flowDay = list.get(position);
            holder.tvTitle.setText(flowDay.getDay());
            holder.view.setOnClickListener(v -> {
                if (mListener == null) {
                    return;
                }
                mListener.onItemClicked(flowDay);
            });
            holder.view.setOnLongClickListener(view -> {
                if (mListener == null) {
                    return false;
                }
                mListener.onItemLongClicked(flowDay);
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View view;

            ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }
    }

    private void consumeCursorByDayForDelete(String day, Consumer<Integer> consumer) {
        SQLiteDatabase sd = new DbHelper(this).getReadableDatabase();
        int delete = sd.delete(DbHelper.TABLE_NAME, "day = ?", new String[]{day});
        consumer.accept(delete);
        sd.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flow_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_shortcut:
                Uapp.addShortCutCompat(this, FlowActivity.class.getCanonicalName(), "flow_time_day", R.mipmap.lyloou, R.string.flow_time_day);
                Snackbar snackbar = Snackbar.make(Uview.getRootView(this), "已添加到桌面", Snackbar.LENGTH_LONG);
                snackbar.setAction("了解详情",
                        v -> WebActivity.newInstance(this, "https://kf.qq.com/touch/sappfaq/180705A3IB3Y1807056fMr6V.html", "want_to_know_why"));
                snackbar.show();
                break;
            case R.id.menu_today_flow_time:
                toFlowActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toFlowActivity() {
        Intent intent = new Intent(mContext, FlowActivity.class);
        mContext.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            FlowDay flowDay = (FlowDay) data.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
            if (isNotContain(flowDay)) {
                mFlowDays.add(flowDay);
                mAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNotContain(FlowDay flowDay) {
        for (FlowDay day : mFlowDays) {
            if (flowDay != null && flowDay.getDay().equals(day.getDay())) {
                return false;
            }
        }
        return true;
    }
}
