package com.lyloou.test.flow;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utime;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlowActivity extends AppCompatActivity {
    private Context mContext;
    private FlowAdapter mAdapter;
    private FlowDay mFlowDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow);
        initData();
        initView();
    }

    private void initData() {
        mFlowDay = new FlowDay();
        mFlowDay.setDay("20190911");
        mFlowDay.setItems(getFlowItems());
    }

    private void initView() {
        initTopPart();
        EmptyRecyclerView recyclerView = initRecycleView();
        initFabBottom(recyclerView);
    }

    @NonNull
    private EmptyRecyclerView initRecycleView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.erv_flow);
        mAdapter = new FlowAdapter(this);
        mAdapter.setList(mFlowDay.getItems());
        mAdapter.setOnItemListener(getOnItemListener());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new TimeLineItemDecoration());
        return recyclerView;
    }

    private FlowAdapter.OnItemListener getOnItemListener() {

        return new FlowAdapter.OnItemListener() {
            @Override
            public void onClickTimeStart(FlowItem item) {
                // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeStart(Utime.getTimeString(hourOfDay, minute));
                    notifyDataChanged();
                };
                showTimePicker(listener, Utime.getValidTime(item.getTimeStart()));
            }

            @Override
            public void onClickTimeEnd(FlowItem item) {
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeEnd(Utime.getTimeString(hourOfDay, minute));
                    notifyDataChanged();
                };
                showTimePicker(listener, Utime.getValidTime(item.getTimeEnd()));
            }
        };
    }

    private void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    private void showTimePicker(TimePickerDialog.OnTimeSetListener listener, int[] time) {
        if (time == null || time.length != 2) {
            Toast.makeText(this, "程序异常", Toast.LENGTH_LONG).show();
            return;
        }
        new TimePickerDialog(mContext, 0, listener, time[0], time[1], true).show();
    }

    @NonNull
    private ArrayList<FlowItem> getFlowItems() {
        ArrayList<FlowItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FlowItem e1 = new FlowItem();
            e1.setTimeStart("10:20");
            e1.setTimeSep("~");
            e1.setTimeEnd("12:00");
            e1.setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
            items.add(e1);
        }
        return items;
    }

    @SuppressLint("CheckResult")
    private void initTopPart() {
        String day = Utime.transferDay(mFlowDay.getDay());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(day);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = findViewById(R.id.iv_header);
        TextView tvHeader = findViewById(R.id.tv_header);
        NetWork.getKingsoftwareApi()
                .getDaily(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            Glide.with(ivHeader.getContext().getApplicationContext())
                                    .load(daily.getPicture2())
                                    .centerCrop()
                                    .into(ivHeader);
                            tvHeader.setText(daily.getContent());
                            tvHeader.setTag(daily.getNote());
                            tvHeader.setVisibility(View.VISIBLE);
                        }
                        , Throwable::printStackTrace);

        tvHeader.setOnClickListener(view -> {
            Object tag = tvHeader.getTag();
            if (tag instanceof String) {
                String newStr = (String) tag;
                String oldStr = tvHeader.getText().toString();
                tvHeader.setText(newStr);
                tvHeader.setTag(oldStr);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    private void initFabBottom(EmptyRecyclerView recyclerView) {
        View fabBottom = findViewById(R.id.fab_bottom);
        fabBottom.setOnClickListener(view -> recyclerView.smoothScrollToPosition(0));
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int totalScrollRange = appBarLayout1.getTotalScrollRange();
            float offset = verticalOffset * 1.0f / totalScrollRange;
            fabBottom.setScaleX(offset);
            fabBottom.setScaleY(offset);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SQLiteDatabase sd = new DbHelper(this).getWritableDatabase();
        switch (item.getItemId()) {
            case R.id.menu_export:
                break;
            case R.id.menu_save:
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbHelper.COL_DAY, mFlowDay.getDay());
                contentValues.put(DbHelper.COL_ITEMS, FlowDayHelper.toJson(mFlowDay.getItems()));
                sd.insert(DbHelper.TABLE_NAME, null, contentValues);
                sd.close();
                break;
            case R.id.menu_recover:
                Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME + " where day = ?", new String[]{mFlowDay.getDay()});
                cursor.moveToFirst();
                String content = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
                cursor.close();
                mFlowDay.setItems(FlowDayHelper.fromJson(content));
                mAdapter.setList(mFlowDay.getItems());
                notifyDataChanged();
                break;
            case R.id.menu_clear:
                mAdapter.clearAll();
                notifyDataChanged();
                break;
            case R.id.menu_clear_deep:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
