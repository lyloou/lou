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

package com.lyloou.test.mxnzp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.Consumer;
import com.lyloou.test.common.DoubleItemOffsetDecoration;
import com.lyloou.test.common.LouDialog;
import com.lyloou.test.common.LouProgressBar;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.gank.Ushare;
import com.lyloou.test.kingsoftware.KingsoftwareUtil;
import com.lyloou.test.util.Uscreen;

import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GirlActivity extends AppCompatActivity {
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Activity mContext;
    private GirlAdapter mGirlAdapter;
    private int mPage = 0;
    private int mTotalCount;
    private boolean mIsLoadingData = false;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = Objects.requireNonNull(layoutManager).findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            // 快到底部了，数据不多了，接着加载
            int nearBottom = totalItemCount - 4;
            if (totalItemCount < mTotalCount && lastVisibleItem >= nearBottom) {
                if (!mIsLoadingData) {
                    loadMore();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_mxnzp);

        initView();

        loadMore();
    }

    @Override
    protected void onDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void loadData(int page, Consumer<List<GirlResult.Data.Girl>> consumer) {
        mIsLoadingData = true;
        Observable<GirlResult> observable = NetWork.get(Constant.Url.Mxnzp.getUrl(), MxnzpApi.class).getGirl(page);
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeResults -> {
                    GirlResult.Data data = jokeResults.getData();
                    mTotalCount = data.getTotalCount();
                    consumer.accept(data.getList());
                    mIsLoadingData = false;
                }, throwable -> mIsLoadingData = false);
        mCompositeDisposable.add(disposable);
    }


    @SuppressLint("CheckResult")
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_mxnzp);
        toolbar.setTitle("一图");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.coolapsing_toolbar_layout_mxnzp);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        Glide.with(this)
                .load(KingsoftwareUtil.getTodayBigImage())
                .centerCrop()
                .into(this.<ImageView>findViewById(R.id.iv_header));

        RecyclerView recyclerView = findViewById(R.id.recyclerview_mxnzp);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mGirlAdapter = new GirlAdapter();
        mGirlAdapter.setOnItemGirlClickListener(new GirlAdapter.OnItemGirlClickListener() {
            @Override
            public void onClick(GirlResult.Data.Girl girl) {
                PhotoView view = new PhotoView(mContext);
                view.setMinimumScale(0.5f);
                view.setMaximumScale(3);
                view.setScale(0.8f);
                Glide.with(mContext)
                        .load(girl.getImageUrl())
                        .thumbnail(0.1f)
                        .into(view);

                LouDialog louDialog = LouDialog
                        .newInstance(mContext, view, R.style.Theme_AppCompat)
                        .setCancelable(true)
                        .setWindowAnimation(R.style.Animation_Alpha)
                        .setWH(-1, -1);

                if (Build.VERSION.SDK_INT >= 19) {
                    Window window = louDialog.getDialog().getWindow();
                    if (window != null) {
                        View decorView = window.getDecorView();
                        decorView.setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }

                }

                view.setOnClickListener(v -> {
                    if (view.getScale() <= 1) {
                        louDialog.dismiss();
                    }
                });
                louDialog.show();
            }

            @Override
            public void onLongClick(GirlResult.Data.Girl girl) {
                LouProgressBar progressTips = LouProgressBar.builder(mContext).tips("图片打包中");
                progressTips.show();
                new Thread(() -> {
                    Ushare.sharePicUrl(mContext, girl.getImageUrl());
                    progressTips.hide();
                }).start();
            }
        });
        recyclerView.setAdapter(mGirlAdapter);
        recyclerView.addOnScrollListener(mListener);
        recyclerView.addItemDecoration(new DoubleItemOffsetDecoration(Uscreen.dp2Px(this, 16)));

    }

    private void loadMore() {
        loadData(mPage++, list -> mGirlAdapter.addItems(list));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
