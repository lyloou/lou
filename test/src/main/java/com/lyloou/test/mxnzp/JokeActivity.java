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

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.kingsoftware.KingsoftwareUtil;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class JokeActivity extends AppCompatActivity {
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Activity mContext;
    private JokeAdapter mJokeAdapter;
    private int retryTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_mxnzp);

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
        Observable<JokeResult> observable = NetWork.get(Constant.Url.Mxnzp.getUrl(), JokeApi.class).getJoke(0);
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jokeResults -> {
                            mJokeAdapter.addItems(jokeResults.getData().getList());
                            retryTimes = 0;
                            Snackbar.make(findViewById(R.id.coordinator_mxnzp), "加载成功", Snackbar.LENGTH_SHORT).show();
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
        Toolbar toolbar = findViewById(R.id.toolbar_mxnzp);
        toolbar.setTitle("笑一笑");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uscreen.setToolbarMarginTop(mContext, toolbar);

        Glide.with(this)
                .load(KingsoftwareUtil.getTodayBigImage())
                .centerCrop()
                .into(this.<ImageView>findViewById(R.id.iv_header));

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.coolapsing_toolbar_layout_mxnzp);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_mxnzp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mJokeAdapter = new JokeAdapter();
        recyclerView.setAdapter(mJokeAdapter);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(this, 16)));

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
