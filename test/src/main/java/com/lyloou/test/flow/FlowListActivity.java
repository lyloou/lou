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
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.webview.NormalWebViewActivity;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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
    Stack<FlowDay> mFlowDayStack = new Stack<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow_list);

        initData();
        initView();
    }


    private void initData() {
        DbUtil.consumeCursorByDay(this, cursor -> {
            FlowDay fd = new FlowDay();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            fd.setId(id);
            fd.setDay(day);
            mFlowDays.add(fd);
        });
        Collections.sort(mFlowDays, (o1, o2) -> -o1.getDay().compareTo(o2.getDay()));
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
                doOnItemLongClicked(flowDay);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }


    enum OperateType {
        ONLY_COPY("复制"),
        COPY_TO_WPS("复制&转到便签"),
        DELETE("删除此项"),
        ;
        String title;

        OperateType(String title) {
            this.title = title;
        }

        public static OperateType indexOf(int index) {
            return OperateType.values()[index];
        }

        public static String[] toStrArray() {
            OperateType[] values = OperateType.values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].title;
            }
            return result;
        }
    }

    private void doOnItemLongClicked(FlowDay flowDay) {
        new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setItems(OperateType.toStrArray(), (dialog, which) -> {
                    switch (OperateType.indexOf(which)) {
                        case DELETE:
                            if (DbUtil.toggleDisabledFlowDay(mContext, flowDay.getDay(), true)) {
                                mFlowDayStack.add(flowDay);
                                mAdapter.remove(flowDay);
                                mAdapter.notifyDataSetChanged();
                                Utoast.show(mContext, "已删除");
                            }
                            break;
                        case ONLY_COPY:
                            FlowUtil.doCopy(mContext, flowDay, false);
                            break;
                        case COPY_TO_WPS:
                            FlowUtil.doCopy(mContext, flowDay, true);
                            break;
                        default:
                    }
                })
                .create()
                .show();

    }

    private void initTopPart() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Uscreen.setToolbarMarginTop(this, toolbar);
        Glide.with(getApplicationContext())
                .load("https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg")
                .centerCrop()
                .into((ImageView) findViewById(R.id.iv_header));

        TextView tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText("大写的时间");

        View fab = findViewById(R.id.fab);
        fab.startAnimation(Uanimation.getRotateAnimation(3600));
        fab.setOnClickListener(view -> toFlowActivity());

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

        public void add(FlowDay flowDay) {
            if (list == null) {
                return;
            }
            list.add(flowDay);
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
                tvTitle = itemView.findViewById(R.id.tv_one);
            }
        }
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
                Uapp.addShortCutCompat(this, FlowActivity.class.getCanonicalName(), "flow_time_day", R.mipmap.lyloou, this.getResources().getString(R.string.flow_time_day));
                Snackbar snackbar = Snackbar.make(Uview.getRootView(this), "已添加到桌面", Snackbar.LENGTH_LONG);
                snackbar.setAction("了解详情",
                        v -> NormalWebViewActivity.newInstance(mContext, "https://kf.qq.com/touch/sappfaq/180705A3IB3Y1807056fMr6V.html"));
                snackbar.show();
                break;
            case R.id.menu_recover_last_one:
                recoverLastOne();
                break;
            case R.id.menu_today_flow_time:
                toFlowActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void recoverLastOne() {
        if (mFlowDayStack.empty()) {
            Utoast.show(mContext, "栈里已经没有了");
            return;
        }
        FlowDay flowDay = mFlowDayStack.pop();
        mFlowDays.add(flowDay);
        DbUtil.toggleDisabledFlowDay(mContext, flowDay.getDay(), false);
        Collections.sort(mFlowDays, (o1, o2) -> -o1.getDay().compareTo(o2.getDay()));
        mAdapter.notifyDataSetChanged();
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
