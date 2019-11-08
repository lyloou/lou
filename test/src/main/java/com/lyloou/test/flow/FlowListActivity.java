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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.TitleViewPagerAdapter;
import com.lyloou.test.common.webview.NormalWebViewActivity;
import com.lyloou.test.util.Uapp;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;

import java.util.Set;
import java.util.TreeSet;

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
    private Adapter mDisableddapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_flow_list);
        initView();
    }


    private void initView() {
        initTopPart();
        showViewpager();
    }

    private void showViewpager() {

        Part part1 = new Part(mContext, "日前的", getData(false));
        Part part2 = new Part(mContext, "回收站", getData(true));
        mActiveAdapter = part1.getAdapter();
        mDisableddapter = part2.getAdapter();

        initListener(part1.getAdapter());
        initListener(part2.getAdapter());

        TitleViewPagerAdapter pagerAdapter = new TitleViewPagerAdapter();
        pagerAdapter.addView(part1.getTitle(), part1.getView());
        pagerAdapter.addView(part2.getTitle(), part2.getView());

        ViewPager viewPager = findViewById(R.id.vp_flow);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tablyt_flow);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private Set<FlowDay> getData(boolean isDisabled) {
        Set<FlowDay> sets = new TreeSet<>((o1, o2) -> -o1.getDay().compareTo(o2.getDay()));

        DbUtil.consumeCursorByDay(this, isDisabled, cursor -> {
            FlowDay fd = new FlowDay();
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_ID));
            String day = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DAY));
            fd.setId(id);
            fd.setDay(day);
            sets.add(fd);
        });
        return sets;
    }


    private void initListener(Adapter adapter) {
        adapter.setListener(new Listener() {
            @Override
            public void onItemClicked(FlowDay flowDay) {
                FlowActivity.start(mContext, flowDay.getDay());
            }

            @Override
            public void onItemLongClicked(FlowDay flowDay) {
                new AlertDialog.Builder(mContext)
                        .setTitle("操作")
                        .setItems(OperateType.toStrArray(), (dialog, which) -> {
                            switch (OperateType.indexOf(which)) {
                                case DELETE:
                                    if (delete(flowDay)) {
                                        Utoast.show(mContext, "已删除");
                                    }
                                    break;
                                case ONLY_COPY:
                                    FlowUtil.doCopy(mContext, flowDay, false);
                                    break;
                                case COPY_TO_WPS:
                                    FlowUtil.doCopy(mContext, flowDay, true);
                                    delete(flowDay);
                                    break;
                                case RECOVER:
                                    unDelete(flowDay);
                                default:
                            }
                        })
                        .create()
                        .show();
            }
        });
    }


    enum OperateType {
        ONLY_COPY("复制"),
        RECOVER("恢复"),
        COPY_TO_WPS("一键复制"),
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

    private boolean delete(FlowDay flowDay) {
        if (DbUtil.toggleDisabledFlowDay(mContext, flowDay.getDay(), true)) {
            mActiveAdapter.remove(flowDay);
            mActiveAdapter.notifyDataSetChanged();
            mDisableddapter.add(flowDay);
            mDisableddapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private boolean unDelete(FlowDay flowDay) {
        if (DbUtil.toggleDisabledFlowDay(mContext, flowDay.getDay(), false)) {
            mActiveAdapter.add(flowDay);
            mActiveAdapter.notifyDataSetChanged();
            mDisableddapter.remove(flowDay);
            mDisableddapter.notifyDataSetChanged();
            return true;
        }
        return false;
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
                mActiveAdapter.getList().add(flowDay);
                mActiveAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNotContain(FlowDay flowDay) {
        for (FlowDay day : mActiveAdapter.getList()) {
            if (flowDay != null && flowDay.getDay().equals(day.getDay())) {
                return false;
            }
        }
        return true;
    }
}
