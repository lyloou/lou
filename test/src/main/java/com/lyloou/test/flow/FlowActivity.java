package com.lyloou.test.flow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.lyloou.test.util.Usystem;
import com.lyloou.test.util.Utime;
import com.lyloou.test.util.Uview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlowActivity extends AppCompatActivity {
    private Activity mContext;
    private FlowAdapter mAdapter;
    private FlowDay mFlowDay;
    private List<FlowItem> mFlowItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow);
        initData();
        initView();
    }

    private void initData() {
        int id = 1;
        consumeCursorById(id, cursor -> {
            String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            mFlowDay = new FlowDay();
            mFlowDay.setId(id);
            mFlowDay.setDay(day);
            mFlowDay.setItems(mFlowItems);
            mFlowItems.addAll(FlowItemHelper.fromJson(items));
        });


    }

    private void initView() {
        initTopPart();
        EmptyRecyclerView recyclerView = initRecycleView();
        initFabBottom(recyclerView);
        Uview.registerHideSoftKeyboardListener(this, getRootView(this));
        initAttachView();
    }

    private void initAttachView() {
        View tvAddItem = findViewById(R.id.tv_add_item);
        tvAddItem.setOnClickListener(v -> {
            addNewItem();
        });
    }

    private void addNewItem() {
        FlowItem newItem = new FlowItem();
        int[] startArr = Utime.getValidTime(null);
        String currentTime = Utime.getTimeString(startArr[0], startArr[1]);

        if (mFlowItems.size() > 0) {
            FlowItem item = mFlowItems.get(0);
            // 当前时间已经存在，则不在新建
            if (currentTime.equals(item.getTimeStart())) {
                showTips("该时间点已经有了一个哦");
                return;
            }
            if (!TextUtils.isEmpty(item.getTimeEnd())) {
                currentTime = item.getTimeEnd();
            }
        }

        newItem.setTimeStart(currentTime);
        mFlowItems.add(0, newItem);
        updateDbAndUI();
    }

    private void showTips(String text) {
        Snackbar.make(getRootView(mContext), text, Snackbar.LENGTH_SHORT).show();
    }

    @NonNull
    private EmptyRecyclerView initRecycleView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.erv_flow);
        mAdapter = new FlowAdapter(this);
        mAdapter.setList(mFlowItems);
        mAdapter.setOnItemListener(getOnItemListener());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new TimeLineItemDecoration());
        return recyclerView;
    }

    private Runnable updateDbTask = () -> {
        SQLiteDatabase sd = new DbHelper(this).getWritableDatabase();
        sd.update(DbHelper.TABLE_NAME, getContentValues(), "id=?", new String[]{String.valueOf(mFlowDay.getId())});
        sd.close();
    };

    private Handler mHandler = new Handler();

    private void delayUpdate() {
        mHandler.removeCallbacks(updateDbTask);
        mHandler.postDelayed(updateDbTask, 500);
    }

    private FlowAdapter.OnItemListener getOnItemListener() {

        return new FlowAdapter.OnItemListener() {
            @Override
            public void onLongClickItem(FlowItem item) {
                removeItem(item);
            }

            @Override
            public void onClickTimeStart(FlowItem item) {
                // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeStart(Utime.getTimeString(hourOfDay, minute));
                    sortItems(mFlowItems);
                    updateDbAndUI();
                };
                showTimePicker(listener, Utime.getValidTime(item.getTimeStart()));
            }

            @Override
            public void onClickTimeEnd(FlowItem item) {
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeEnd(Utime.getTimeString(hourOfDay, minute));
                    sortItems(mFlowItems);
                    updateDbAndUI();
                };
                showTimePicker(listener, Utime.getValidTime(item.getTimeEnd()));
            }

            @Override
            public void onTextChanged(FlowItem item, CharSequence s) {
                item.setContent(String.valueOf(s));
                updateDb();
            }
        };
    }

    private void removeItem(FlowItem item) {
        new AlertDialog.Builder(mContext)
                .setMessage("确认删除时间段：\n"
                        .concat(Utime.getFormatTime(item.getTimeStart()))
                        .concat(item.getTimeSep())
                        .concat(Utime.getFormatTime(item.getTimeEnd())))
                .setPositiveButton("是的", (dialog, which) -> {
                    mFlowItems.remove(item);
                    updateDbAndUI();
                })
                .setNegativeButton("再想想", (dialog, which) -> {
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void updateDb(boolean... now) {
        if (now.length > 0 && now[0]) {
            updateDbTask.run();
            return;
        }
        delayUpdate();
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
    }

    private void updateDbAndUI() {
        updateUI();
        updateDb();
    }

    private void showTimePicker(TimePickerDialog.OnTimeSetListener listener, int[] time) {
        if (time == null || time.length != 2) {
            Toast.makeText(this, "程序异常", Toast.LENGTH_LONG).show();
            return;
        }
        new TimePickerDialog(mContext, 0, listener, time[0], time[1], true).show();
    }

    @NonNull
    private List<FlowItem> getFlowItems() {
        List<FlowItem> items = new ArrayList<>();
        FlowItem e1 = new FlowItem();
        e1.setTimeStart("08:20");
        e1.setTimeSep("~");
        e1.setTimeEnd("12:00");
        e1.setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
        items.add(e1);

        e1 = new FlowItem();
        e1.setTimeStart("07:20");
        e1.setTimeSep("~");
        e1.setTimeEnd("12:00");
        e1.setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
        items.add(e1);

        e1 = new FlowItem();
        e1.setTimeStart("12:20");
        e1.setTimeSep("~");
        e1.setTimeEnd("12:00");
        e1.setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
        items.add(e1);

        e1 = new FlowItem();
        e1.setTimeStart("13:20");
        e1.setTimeSep("~");
        e1.setTimeEnd("15:00");
        e1.setContent("多么美好的一天 大太阳晒伤我的脸 我担心干旱持续好久水库会缺水 多么美好的一天 又接近末日一点点 我独自坚强稀释寂寞无聊的时间");
        items.add(e1);

        sortItems(items);


        return items;
    }

    private void sortItems(List<FlowItem> items) {
        Collections.sort(items, (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return -1;
            }
            return o2.getTimeStart().compareTo(o1.getTimeStart());
        });
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

        String content = FlowItemHelper.toPrettyText(mFlowDay.getItems());

        switch (item.getItemId()) {
            case R.id.menu_copy:
                Usystem.copyString(mContext, content);
                showTips("复制成功");
                break;
            case R.id.menu_share:
                Usystem.shareText(mContext, mFlowDay.getDay(), content);
            case R.id.menu_save:
                updateDb(true);
                break;
            case R.id.menu_recover:
                consumeCursorById(mFlowDay.getId(), cursor -> {
                    String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
                    mFlowItems.clear();
                    mFlowItems.addAll(FlowItemHelper.fromJson(items));
                    updateUI();
                });
                break;
            case R.id.menu_clear:
                mFlowItems.clear();
                updateUI();
                break;
            case R.id.menu_deep:
                mFlowItems.clear();
                updateUI();
                updateDb(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void consumeCursorByDay(String day, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(this).getWritableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME + " where day = ?", new String[]{day});
        cursor.moveToFirst();
        consumer.accept(cursor);
        cursor.close();
        sd.close();
    }

    private void consumeCursorById(int id, Consumer<Cursor> consumer) {
        SQLiteDatabase sd = new DbHelper(this).getWritableDatabase();
        Cursor cursor = sd.rawQuery("select * from " + DbHelper.TABLE_NAME + " where id = ?", new String[]{"" + id});
        cursor.moveToFirst();
        consumer.accept(cursor);
        cursor.close();
        sd.close();
    }

    @NonNull
    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_DAY, mFlowDay.getDay());
        contentValues.put(DbHelper.COL_ITEMS, FlowItemHelper.toJson(mFlowDay.getItems()));
        return contentValues;
    }

    private View getRootView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }
}
