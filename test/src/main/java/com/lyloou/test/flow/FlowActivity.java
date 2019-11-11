package com.lyloou.test.flow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.gyf.immersionbar.ImmersionBar;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.glide.PaletteBitmap;
import com.lyloou.test.common.glide.PaletteBitmapTranscoder;
import com.lyloou.test.kingsoftware.KingsoftwareAPI;
import com.lyloou.test.kingsoftware.KingsoftwareUtil;
import com.lyloou.test.util.Udialog;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Usystem;
import com.lyloou.test.util.Utime;
import com.lyloou.test.util.Uview;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.lyloou.test.flow.TransferUtil.sortItems;

public class FlowActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "id";
    private static final String EXTRA_DAY = "day";
    public static final String ACTION_REFRESH = "refresh";
    private Activity mContext;
    private FlowAdapter mAdapter;
    private FlowDay mFlowDay;
    private AppBarLayout mAppBarLayout;
    private TextView mTvHeader;
    private static final int COLOR_BLUE = Color.parseColor("#009edc");
    private static final String TAG = FlowActivity.class.getSimpleName();

    // 每次都是插入到第一个，用 LinkedList 效率应该会更好（https://snailclimb.top/JavaGuide/#/java/collection/Java集合框架常见面试题?id=arraylist-与-linkedlist-区别）
    private List<FlowItem> mFlowItems = new LinkedList<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            switch (intent.getAction()) {
                case ACTION_REFRESH:
                    reloadDataAndUI();
                    break;
            }
        }
    };

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, FlowActivity.class);
        intent.putExtra(EXTRA_ID, id);
        context.startActivity(intent);
    }

    public static void start(Context context, String day) {
        Intent intent = new Intent(context, FlowActivity.class);
        intent.putExtra(EXTRA_DAY, day);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow);
        initData();
        initView();
        initReceiver();
    }

    private void initReceiver() {
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, getIntentFilter());
    }

    private void sendRefreshBroadcastReceiver() {
        Intent intent = new Intent();
        intent.setAction(ACTION_REFRESH);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    private boolean isFront = false;

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REFRESH);
        return intentFilter;
    }

    private void reloadDataAndUI() {
        if (isFront) {
            return;
        }
        reloadAllDataAndUI();
    }

    private void reloadAllDataAndUI() {
        DbUtil.consumeCursorById(this, mFlowDay.getId(), cursor -> {
            String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
            mFlowItems.clear();
            mFlowItems.addAll(FlowItemHelper.fromJsonArray(items));
            updateUI();
        });
    }

    private void initData() {
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            DbUtil.consumeCursorById(this, id, this::fillData);
            return;
        }

        String day = getIntent().getStringExtra(EXTRA_DAY);
        if (!TextUtils.isEmpty(day)) {
            DbUtil.consumeCursorByDay(this, day, this::fillData);
            return;
        }

        // new FlowDay with today
        String today = Utime.today();
        DbUtil.consumeCursorByDay(this, today, cursor -> {
            int count = cursor.getCount();
            if (count == 0) {
                FlowDay flowDay = new FlowDay();
                flowDay.setDay(today);
                long lId = DbUtil.insertFlowDayToDb(this, flowDay);
                flowDay.setId((int) lId);
                mFlowDay = flowDay;
                mFlowDay.setItems(mFlowItems);
            } else {
                fillData(cursor);
            }
        });

    }

    private void fillData(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
        String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
        String items = cursor.getString(cursor.getColumnIndex(DbHelper.COL_ITEMS));
        mFlowItems.addAll(FlowItemHelper.fromJsonArray(items));
        mFlowDay = new FlowDay();
        mFlowDay.setId(id);
        mFlowDay.setDay(day);
        mFlowDay.setItems(mFlowItems);
        sortItems(mFlowItems);
    }

    private void initView() {
        initTopPart();
        initRecycleView();
        initAppBarLayout();
        initAttachView();
    }

    private void initAttachView() {
        findViewById(R.id.tv_add_item).setOnClickListener(v -> addNewItem());
        findViewById(R.id.tv_to_list).setOnClickListener(v -> toList());
    }

    private void toList() {
        Intent intent = new Intent(this, FlowListActivity.class);
        startActivity(intent);
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
        Snackbar.make(Uview.getRootView(mContext), text, Snackbar.LENGTH_SHORT).show();
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
        DbUtil.getUpdateFlowDayConsumer(mContext).accept(mFlowDay);
        sendRefreshBroadcastReceiver();
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
                String message = "确认删除时间段：\n"
                        .concat(Utime.getFormatTime(item.getTimeStart()))
                        .concat(item.getTimeSep())
                        .concat(Utime.getFormatTime(item.getTimeEnd()));
                Udialog.alert(mContext, message, ok -> {
                    if (ok) {
                        mFlowItems.remove(item);
                        updateDbAndUI();
                    }
                });
            }

            @Override
            public void onClickTimeStart(FlowItem item) {
                // 原文链接：https://blog.csdn.net/qq_17009881/article/details/75371406
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeStart(Utime.getTimeString(hourOfDay, minute));
                    sortItems(mFlowItems);
                    updateDbAndUI();
                };
                Udialog.showTimePicker(mContext, listener, Utime.getValidTime(item.getTimeStart()));
            }

            @Override
            public void onClickTimeEnd(FlowItem item) {
                TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                    item.setTimeEnd(Utime.getTimeString(hourOfDay, minute));
                    sortItems(mFlowItems);
                    updateDbAndUI();
                };
                Udialog.showTimePicker(mContext, listener, Utime.getValidTime(item.getTimeEnd()));
            }

            @Override
            public void onLongClickTimeStart(FlowItem item) {
                Udialog.alert(mContext, "清空开始时间", result -> {
                    if (result) {
                        item.setTimeStart(null);
                        updateDbAndUI();
                    }
                });
            }

            @Override
            public void onLongClickTimeEnd(FlowItem item) {
                Udialog.alert(mContext, "清空结束时间", result -> {
                    if (result) {
                        item.setTimeEnd(null);
                        updateDbAndUI();
                    }
                });
            }

            @Override
            public void onTextChanged(FlowItem item, CharSequence s) {
                item.setContent(String.valueOf(s));
                updateDb();
            }

            @Override
            public void onEditTextFocused(boolean hasFocus, FlowItem item) {
                if (hasFocus) {
                    mTvHeader.setVisibility(View.GONE);
                    mAppBarLayout.setExpanded(false, true);
                }
            }
        };
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


    @SuppressLint("CheckResult")
    private void initTopPart() {
        String day = Utime.transferTwoToOne(mFlowDay.getDay());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(day);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView ivHeader = findViewById(R.id.iv_header);
        Glide.with(mContext)
                .load(KingsoftwareUtil.getBigImage(mFlowDay.getDay()))
                .asBitmap()
                .transcode(new PaletteBitmapTranscoder(mContext), PaletteBitmap.class)
                .fitCenter()
                .into(new ImageViewTarget<PaletteBitmap>(ivHeader) {
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        // [Converting bitmap to drawable in Android - Stack Overflow](https://stackoverflow.com/questions/23107090/converting-bitmap-to-drawable-in-android)
                        super.view.setBackground(new BitmapDrawable(getResources(), resource.bitmap));
                        Palette p = resource.palette;
                        int color = p.getMutedColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                        resetThemeColor(color);
                    }
                });
        mTvHeader = findViewById(R.id.tv_header);
        NetWork.get(Constant.Url.Kingsoftware.getUrl(), KingsoftwareAPI.class)
                .getDaily(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                            ivHeader.setTag(daily.getFenxiang_img());
                            Uscreen.setWallpaperByImageView(ivHeader, COLOR_BLUE, false);
                            mTvHeader.setText(daily.getContent());
                            mTvHeader.setTag(daily.getNote());
                            mTvHeader.setVisibility(View.VISIBLE);
                        }
                        , Throwable::printStackTrace);

        mTvHeader.setOnClickListener(view -> {
            Object tag = mTvHeader.getTag();
            if (tag instanceof String) {
                String newStr = (String) tag;
                String oldStr = mTvHeader.getText().toString();
                mTvHeader.setText(newStr);
                mTvHeader.setTag(oldStr);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    // [Change int color opacity in java/android - Stack Overflow](https://stackoverflow.com/questions/28483497/change-int-color-opacity-in-java-android)
    private int getTransparentColor(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        // Set alpha based on your logic, here I'm making it 25% of it's initial value.
        alpha *= 0.55;

        return Color.argb(alpha, red, green, blue);
    }

    private void resetThemeColor(int color) {
        int transparentColor = getTransparentColor(color);

        findViewById(R.id.tv_add_item).setBackgroundColor(color);
        findViewById(R.id.tv_to_list).setBackgroundColor(color);
        findViewById(R.id.tv_header).setBackgroundColor(transparentColor);
        findViewById(R.id.v_sep_1).setBackgroundColor(transparentColor);
        findViewById(R.id.v_sep_2).setBackgroundColor(transparentColor);

        // https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android
        // [Android Material Design - How to change background color of Toolbar after CollapsingToolbarLayout is collapsed - Stack Overflow](https://stackoverflow.com/questions/30619598/android-material-design-how-to-change-background-color-of-toolbar-after-collap)
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        ImmersionBar.with(mContext)
                .statusBarColor(hexColor)
                .navigationBarColor(hexColor)
                .init();
    }

    private void initAppBarLayout() {
        mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) == mAppBarLayout.getTotalScrollRange()) {
                //Collapsed
                if (mTvHeader.getVisibility() == View.GONE) {
                    return;
                }

                mTvHeader.animate().alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mTvHeader.setVisibility(View.GONE);
                            }
                        })
                        .start();
            } else {
                //Expanded
                if (mTvHeader.getVisibility() == View.VISIBLE) {
                    return;
                }
                mTvHeader.setVisibility(View.VISIBLE);
                mTvHeader.animate().alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                            }
                        })
                        .start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flow, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                ToolUtil.doCopy(mContext, mFlowDay, false);
                break;
            case R.id.menu_share:
                String content = FlowItemHelper.toPrettyText(mFlowDay.getItems());
                String day = mFlowDay.getDay();
                Usystem.shareText(mContext, day, content);
            case R.id.menu_save:
                updateDb(true);
                break;
            case R.id.menu_recover:
                reloadAllDataAndUI();
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


    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(Intent.ACTION_ATTACH_DATA, mFlowDay);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
}
