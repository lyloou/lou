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
import android.content.ClipboardManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lyloou.test.R;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class XiaoHuaActivity extends AppCompatActivity {
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Activity mContext;
    private XiaoHuaAdapter mXiaoHuaAdapter;
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
        Observable<List<XiaoHua>> observable = NetWork.getLaiFuDaoApi().getXiaoHua();
        Disposable disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(xiaoHuas -> {
                            mXiaoHuaAdapter.addItems(xiaoHuas);
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
        toolbar.setTitle("来福岛上的笑话");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.coolapsing_toolbar_layout_xiaohua);
        collapsingToolbarLayout.setExpandedTitleColor(Color.YELLOW);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_laifudao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXiaoHuaAdapter = new XiaoHuaAdapter();
        mXiaoHuaAdapter.setOnItemXiaoHuaClickListener(new XiaoHuaAdapter.OnItemXiaoHuaClickListener() {
            @Override
            public void onClick(String url) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                builder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(mContext, R.anim.slide_in_left, R.anim.slide_out_right);
                builder.setCloseButtonIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.back_white));
                builder.setShowTitle(false);

                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(url));
            }

            @Override
            public void onLongClick(String content) {
                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cmb.setText(content);
                Utoast.show(mContext, "“笑话”已经复制到剪切板");
            }
        });
        recyclerView.setAdapter(mXiaoHuaAdapter);
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
