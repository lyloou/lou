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

package com.lyloou.test.laifudao;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.lyloou.test.R;
import com.lyloou.test.common.DoubleItemOffsetDecoration;
import com.lyloou.test.common.LouDialog;
import com.lyloou.test.common.LouDialogProgressTips;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.gank.Ushare;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TuPianActivity extends AppCompatActivity {
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Activity mContext;
    private TuPianAdapter mTuPianAdapter;
    private int retryTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_laifudao);

        initView();

        loadData();
    }

    @Override
    protected void onDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void loadData() {

        Observable<List<TuPian>> observable = NetWork.getLaiFuDaoApi().getTuPian();
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<List<TuPian>>() {
                    @Override
                    public void accept(@NonNull List<TuPian> tuPien) throws Exception {
                        // 模拟网络延迟
                        SystemClock.sleep(1000);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tupian -> {
                            // 减少一个元素，构成奇数个数的列表
                            tupian.remove(tupian.size() - 1);
                            mTuPianAdapter.addItems(tupian);
                            retryTimes = 0;
                            Snackbar.make(findViewById(R.id.coordinator_xiaohua), "加载成功", Snackbar.LENGTH_SHORT).show();
                        }
                        , throwable -> {
                            Utoast.show(mContext, "加载失败：" + throwable.getMessage() + "\n 重新尝试：" + retryTimes);
                            if (retryTimes > 20) {
                                Utoast.show(mContext, "网络真的不行了，你等会儿再来吧");
                                return;
                            }
                            retryTimes++;
                            loadData();
                        });
        mCompositeDisposable.add(disposable);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_laifudao);
        toolbar.setTitle("来福岛上的笑话图片");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(mContext, toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coolapsing_toolbar_layout_xiaohua);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        NetWork.getKingsoftwareApi()
                .getDaily("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daily -> Glide
                                .with(mContext)
                                .load(daily.getPicture2())
                                .centerCrop()
                                .into(ivHeader)
                        , Throwable::printStackTrace);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_laifudao);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTuPianAdapter = new TuPianAdapter();
        mTuPianAdapter.setOnItemTuPianClickListener(new TuPianAdapter.OnItemTuPianClickListener() {
            @Override
            public void onClick(TuPian tuPian) {
                PhotoView view = new PhotoView(mContext);
                view.setMinimumScale(0.5f);
                view.setMaximumScale(3);
                view.setScale(0.8f);
                Glide.with(mContext)
                        .load(tuPian.getThumburl())
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

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (view.getScale() <= 1) {
                            louDialog.dismiss();
                        }
                    }
                });
                louDialog.show();
            }

            @Override
            public void onLongClick(TuPian tuPian) {
                LouDialogProgressTips progressTips = LouDialogProgressTips.getInstance(mContext);
                progressTips.show("图片准备中");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Ushare.clearImageDir(mContext);
                        List<String> paths = new ArrayList<String>();
                        String imageFilePathFromImageUrl = Ushare.getImageFilePathFromImageUrl(mContext, tuPian.getThumburl());
                        paths.add(imageFilePathFromImageUrl);
                        Ushare.sharePicsToWechat(mContext, "", paths, Ushare.SHARE_TYPE_FRIEND, getTipsRunnable());
                        progressTips.hide();
                    }
                }).start();
            }
        });
        recyclerView.setAdapter(mTuPianAdapter);
        recyclerView.addItemDecoration(new DoubleItemOffsetDecoration(Uscreen.dp2Px(this, 16)));

    }

    @android.support.annotation.NonNull
    private Runnable getTipsRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utoast.show(mContext, "没有安装微信");
                    }
                });
            }
        };
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
