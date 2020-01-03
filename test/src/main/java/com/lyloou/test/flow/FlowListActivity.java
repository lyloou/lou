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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.TitleViewPagerAdapter;
import com.lyloou.test.common.webview.NormalWebViewActivity;
import com.lyloou.test.flow.net.FlowApi;
import com.lyloou.test.flow.net.FlowReq;
import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Ulist;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;
import com.lyloou.test.util.dialog.Udialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2019.09.25 10:34
 * <p>
 * Description: 时间流列表
 */
public class FlowListActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    public static final String IMG_TOP_BG = "https://ws1.sinaimg.cn/large/610dc034gy1fhupzs0awwj20u00u0tcf.jpg";
    private Activity mContext;
    private Adapter mActiveAdapter;
    private Adapter mArchiveddapter;
    private CompositeDisposable mDisposable;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow_list);
        mDisposable = new CompositeDisposable();
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FlowDay flowDay = (FlowDay) intent.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
        updateFlowDayWhenBackHere(flowDay);
    }

    private void initView() {
        initTopPart();
        showViewpager();
    }

    private void showViewpager() {

        Part part1 = new Part(mContext, "进行中", getData(false));
        Part part2 = new Part(mContext, "已归档", getData(true));
        mActiveAdapter = part1.getAdapter();
        mArchiveddapter = part2.getAdapter();

        initListener(part1.getAdapter());
        initListener(part2.getAdapter());

        TitleViewPagerAdapter pagerAdapter = new TitleViewPagerAdapter();
        pagerAdapter.addView(part1.getTitle(), part1.getView());
        pagerAdapter.addView(part2.getTitle(), part2.getView());

        mViewPager = findViewById(R.id.vp_flow);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablyt_flow);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private Set<FlowDay> getData(boolean isArchived) {
        Set<FlowDay> sets = new TreeSet<>((o1, o2) -> -o1.getDay().compareTo(o2.getDay()));

        DbUtil.consumeCursorByDay(this, isArchived, cursor -> {
            FlowDay flowDay = new FlowDay();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            int isSynced = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_SYNCED));
            int isDisabled = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IS_DISABLED));

            flowDay.setId(id);
            flowDay.setDay(day);
            flowDay.setArchived(isArchived);
            flowDay.setSynced(isSynced == Constant.TRUE);
            flowDay.setDisabled(isDisabled == Constant.TRUE);
            sets.add(flowDay);
        });
        return sets;
    }


    private void initListener(Adapter adapter) {
        adapter.setListener(new Listener() {
            @Override
            public void onItemClicked(FlowDay flowDay) {
                toFlowActivity(flowDay.getDay());
            }

            @Override
            public void onItemLongClicked(FlowDay flowDay) {
                doOnItemLongClicked(flowDay);
            }
        });
    }

    private void doOnItemLongClicked(FlowDay flowDay) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("操作");
        if (flowDay.isArchived()) {
            builder.setItems(ArchivedType.toStrArray(), (dialog, which) -> {
                switch (ArchivedType.indexOf(which)) {
                    case COPY:
                        ToolUtil.doCopy(mContext, TransferUtil.getFlowDayByDay(mContext, flowDay.getDay()), false);
                        break;
                    case DELETE:
                        delete(flowDay);
                        break;
                    case UNDER_ARCHIVE:
                        archive(flowDay, false);
                    case SYNC:
                        sync(flowDay);
                    default:
                }
            });
        } else {
            builder.setItems(ActiveType.toStrArray(), (dialog, which) -> {
                switch (ActiveType.indexOf(which)) {
                    case ARCHIVE:
                        archive(flowDay, true);
                        break;
                    case COPY:
                        ToolUtil.doCopy(mContext, TransferUtil.getFlowDayByDay(mContext, flowDay.getDay()), false);
                        break;
                    case COPY_TO_WPS:
                        ToolUtil.doCopy(mContext, TransferUtil.getFlowDayByDay(mContext, flowDay.getDay()), true);
                        archive(flowDay, true);
                        break;
                    case DELETE:
                        delete(flowDay);
                    case SYNC:
                        sync(flowDay);
                    default:
                }
            });
        }
        builder.create().show();

    }


    private void sync(FlowDay flowDay) {
        if (flowDay.isSynced()) {
            Utoast.show(mContext, "已同步");
            return;
        }

        FlowReq flowReq = TransferUtil.transferToFlowReq(mContext, flowDay);
        netSync(flowReq, () -> {
            // 更新数据库
            if (DbUtil.updateSynced(mContext, flowDay.getDay(), true)) {
                // 更新界面
                flowDay.setSynced(true);
                if (flowDay.isArchived()) {
                    mArchiveddapter.notifyDataSetChanged();
                } else {
                    mActiveAdapter.notifyDataSetChanged();
                }
                Utoast.show(mContext, String.format("同步%s成功", flowDay.getDay()));
            }
        });
    }

    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    enum ArchivedType {
        SYNC("同步"),
        COPY("复制"),
        UNDER_ARCHIVE("撤销归档"),
        DELETE("删除"),
        ;
        String title;

        ArchivedType(String title) {
            this.title = title;
        }

        public static ArchivedType indexOf(int index) {
            return ArchivedType.values()[index];
        }

        public static String[] toStrArray() {
            ArchivedType[] values = ArchivedType.values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].title;
            }
            return result;
        }
    }

    enum ActiveType {
        SYNC("同步"),
        COPY("复制"),
        ARCHIVE("归档"),
        COPY_TO_WPS("一键复制"),
        DELETE("删除"),
        ;

        String title;

        ActiveType(String title) {
            this.title = title;
        }

        public static ActiveType indexOf(int index) {
            return ActiveType.values()[index];
        }

        public static String[] toStrArray() {
            ActiveType[] values = ActiveType.values();
            String[] result = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                result[i] = values[i].title;
            }
            return result;
        }
    }

    private void delete(FlowDay flowDay) {
        Udialog.AlertOneItem.builder(mContext).message("确定删除?").consumer(result -> {
            if (result) {
                FlowReq flowReq = TransferUtil.transferToFlowReq(mContext, flowDay);
                flowReq.setDisabled(true);
                netSync(flowReq, () -> {
                    // 从数据库中删除
                    if (DbUtil.delete(mContext, flowDay.getDay())) {
                        // 更新列表
                        if (flowDay.isArchived()) {
                            mArchiveddapter.remove(flowDay);
                            mArchiveddapter.notifyDataSetChanged();
                        } else {
                            mActiveAdapter.remove(flowDay);
                            mActiveAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        }).show();

    }

    public static final int MAX_SYNC_NUMBER = 10;

    private void netBatchSync(List<FlowReq> flowReqList, Runnable runnable) {
        List<List<FlowReq>> partition = Ulist.partition(flowReqList, MAX_SYNC_NUMBER);
        List<Disposable> disposableList = new ArrayList<>();
        for (List<FlowReq> flowReqs : partition) {

            Disposable disposable = NetWork.get(Constant.Url.FlowApi.getUrl(), FlowApi.class)
                    .batchSync(flowReqs)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(commonResult -> {
                        if (commonResult.getErr_code() == 0) {
                            runnable.run();
                        } else {
                            String flowReqDays = getFlowReqDays(flowReqs);
                            Log.i("TTAG", "syncList: " + String.format("同步%s失败", flowReqDays));
                            Utoast.show(mContext, String.format("同步%s失败, err_code:%s", flowReqDays, commonResult.getErr_code()));
                        }
                    }, throwable -> {
                        String flowReqDays = getFlowReqDays(flowReqs);
                        Log.e("TTAG", "syncList: " + String.format("同步%s失败", flowReqDays), throwable);
                        Utoast.show(mContext, String.format("同步%s失败, 原因：%s", flowReqDays, throwable.getMessage()));
                    });
            disposableList.add(disposable);
        }

        mDisposable.addAll(disposableList.toArray(new Disposable[0]));
    }

    private String getFlowReqDays(List<FlowReq> flowReqs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < flowReqs.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(flowReqs.get(i).getDay());
        }
        sb.append("]");
        return sb.toString().trim();
    }

    private void netSync(FlowReq flowReq, Runnable runnable) {
        mDisposable.add(NetWork.get(Constant.Url.FlowApi.getUrl(), FlowApi.class)
                .sync(flowReq)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonResult -> {
                    if (commonResult.getErr_code() == 0) {
                        runnable.run();
                    } else {
                        Utoast.show(mContext, String.format("同步%s失败, err_code:%s", flowReq.getDay(), commonResult.getErr_code()));
                    }
                }, throwable -> {
                    Utoast.show(mContext, String.format("同步%s失败, 原因：%s", flowReq.getDay(), throwable.getMessage()));
                }));
    }

    private void archive(FlowDay flowDay, boolean archived) {
        FlowReq flowReq = TransferUtil.transferToFlowReq(mContext, flowDay);
        flowReq.setArchived(archived);
        netSync(flowReq, () -> {
            if (DbUtil.updateArchived(mContext, flowDay.getDay(), archived)) {
                flowDay.setArchived(archived);
                if (archived) {
                    mActiveAdapter.remove(flowDay);
                    mActiveAdapter.notifyDataSetChanged();
                    mArchiveddapter.add(flowDay);
                    mArchiveddapter.notifyDataSetChanged();
                } else {
                    mArchiveddapter.remove(flowDay);
                    mArchiveddapter.notifyDataSetChanged();
                    mActiveAdapter.add(flowDay);
                    mActiveAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initTopPart() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Uscreen.setToolbarMarginTop(this, toolbar);
        Glide.with(getApplicationContext())
                .load(IMG_TOP_BG)
                .centerCrop()
                .into((ImageView) findViewById(R.id.iv_header));

        TextView tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText("大写的时间");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
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
            case R.id.menu_today_flow_time:
                toFlowActivity();
                break;
            case R.id.menu_sync_current_tab:
                syncList(false);
                break;
            case R.id.menu_sync_all_tab:
                syncList(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 同步数据
     *
     * @param all 是否是全部
     */
    private void syncList(boolean all) {
        DbUtil.consumeCursorByArchived(mContext, all, mViewPager.getCurrentItem(), cursor -> {
            final int count = cursor.getCount();
            if (count == 0) {
                return;
            }

            List<FlowDay> flowDays = TransferUtil.transferCursorToFlowDays(cursor);
            List<FlowReq> flowReqList = TransferUtil.transferToFlowReqs(mContext, flowDays);
            netBatchSync(flowReqList, () -> {
                boolean isArchivedDataChanged = false;
                boolean isActiveDataChanged = false;
                for (FlowDay flowDay : flowDays) {
                    // 更新数据库
                    if (DbUtil.updateSynced(mContext, flowDay.getDay(), true)) {
                        flowDay.setSynced(true);
                        updateFlowDayWhenBackHere(flowDay);
                        // 更新界面
                        if (flowDay.isArchived()) {
                            isArchivedDataChanged = true;
                        } else {
                            isActiveDataChanged = true;
                        }
                    }
                }
                Log.i("TTAG", "syncList: " + String.format("同步%s成功", getFlowReqDays(flowReqList)));

                if (count == MAX_SYNC_NUMBER) {
                    syncList(all);
                    return;
                }

                if (isArchivedDataChanged) {
                    mArchiveddapter.notifyDataSetChanged();
                }
                if (isActiveDataChanged) {
                    mActiveAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    public void toFlowActivity(String day) {
        Intent intent = new Intent(mContext, FlowActivity.class);
        intent.putExtra(FlowActivity.EXTRA_DAY, day);
        mContext.startActivityForResult(intent, REQUEST_CODE);
    }

    private void toFlowActivity() {
        Intent intent = new Intent(mContext, FlowActivity.class);
        mContext.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            FlowDay flowDay = (FlowDay) data.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
            updateFlowDayWhenBackHere(flowDay);
        }
    }

    private void updateFlowDayWhenBackHere(FlowDay flowDay) {
        if (flowDay == null) {
            return;
        }
        FlowDay sameFlowDay = getSameFlowDay(flowDay);

        // 新加的
        if (sameFlowDay == null) {
            mActiveAdapter.getList().add(flowDay);
            mActiveAdapter.notifyDataSetChanged();
            return;
        }

        sameFlowDay.setSynced(flowDay.isSynced());
        if (sameFlowDay.isArchived()) {
            mArchiveddapter.notifyDataSetChanged();
        } else {
            mActiveAdapter.notifyDataSetChanged();
        }
    }


    private FlowDay getSameFlowDay(FlowDay flowDay) {
        for (FlowDay day : mActiveAdapter.getList()) {
            if (flowDay != null && flowDay.getDay().equals(day.getDay())) {
                return day;
            }
        }

        for (FlowDay day : mArchiveddapter.getList()) {
            if (flowDay != null && flowDay.getDay().equals(day.getDay())) {
                return day;
            }
        }
        return null;
    }
}
