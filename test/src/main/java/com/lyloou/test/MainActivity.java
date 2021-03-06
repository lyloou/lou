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

package com.lyloou.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.lyloou.test.bus.notification.LongRunningService;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.CrashHandler;
import com.lyloou.test.common.ImageHelper;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.glide.PaletteBitmap;
import com.lyloou.test.common.glide.PaletteBitmapTranscoder;
import com.lyloou.test.kingsoftware.KingsoftwareAPI;
import com.lyloou.test.util.Uactivity;
import com.lyloou.test.util.Uanimation;
import com.lyloou.test.util.Ucolor;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Uservice;
import com.lyloou.test.util.Usp;
import com.lyloou.test.util.Uview;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.28 11:57
 * <p>
 * Description:
 */
public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private TextView mTvHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // toNext();

        setContentView(R.layout.activity_main);
        CrashHandler.getInstance().init(this.getApplicationContext());
        initView();
        initAlarm();
    }

    private void initAlarm() {
        if (Usp.init(this).getBoolean(Constant.Key.BACKGROUND_SERVER.str(), false)) {
            Uservice.start(this, LongRunningService.class);
        }
    }

    private void initView() {
        initTopPart();

        RecyclerView recyclerView = findViewById(R.id.rv_main);
        Map<String, Class> stringClassMap = Uactivity.getActivitiesMapFromManifest(this, this.getPackageName());

        recyclerView.setAdapter(new MainAdapter(stringClassMap));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));
    }

    @SuppressLint("CheckResult")
    private void initTopPart() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(this, toolbar);

        ImageView ivHeader = findViewById(R.id.iv_header);
        mTvHeader = findViewById(R.id.tv_header);
        Glide.with(mContext)
                .load(ImageHelper.getTodayBigImage())
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
        //noinspection ResultOfMethodCallIgnored
        NetWork.get(Constant.Url.Kingsoftware.getUrl(), KingsoftwareAPI.class)
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> {
                    mTvHeader.setText(daily.getContent());
                    mTvHeader.setTag(daily.getNote());
                    mTvHeader.setVisibility(View.VISIBLE);
                }, Throwable::printStackTrace);

        View fab = findViewById(R.id.fab);
        fab.startAnimation(Uanimation.getRotateAnimation(3600));
        fab.setOnClickListener(view -> {
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
        Uview.toggleViewVisibleWhenAppBarLayoutScrollChanged(findViewById(R.id.app_bar), mTvHeader);
    }

    private void resetThemeColor(int color) {
        int transparentColor = Ucolor.getTransparentColor(color);
        findViewById(R.id.tv_header).setBackgroundColor(transparentColor);
        this.<FloatingActionButton>findViewById(R.id.fab).setBackgroundTintList(ColorStateList.valueOf(color));
    }


    static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
        final Map<String, Class> stringClassMap;
        Object[] labels;

        MainAdapter(Map<String, Class> stringClassMap) {
            this.stringClassMap = stringClassMap;
            labels = stringClassMap.keySet().toArray();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_one, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String label = (String) labels[position];
            holder.tvTitle.setText(label);
            holder.view.setOnClickListener(v ->
                    Uactivity.start(v.getContext(), stringClassMap.get(label)));
        }

        @Override
        public int getItemCount() {
            return labels.length;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private void toNext() {
        if (MyApplication.sSkipWelcome) {
            return;
        }
        MyApplication.sSkipWelcome = true;

        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
