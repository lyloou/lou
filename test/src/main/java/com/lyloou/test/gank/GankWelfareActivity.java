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

package com.lyloou.test.gank;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.common.Constant;
import com.lyloou.test.common.Consumer;
import com.lyloou.test.common.DoubleItemWithOneHeaderOffsetDecoration;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.LouProgressBar;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.webview.WebContentActivity;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Usystem;
import com.lyloou.test.util.Utime;
import com.lyloou.test.util.Utoast;
import com.lyloou.test.util.Uview;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GankWelfareActivity extends AppCompatActivity {
    private static final String TAG = GankWelfareActivity.class.getSimpleName();
    public static final int NUMBER = 20;
    private int mPage = 1;
    private LinearLayout mLlytBottom;
    private SwipeRefreshLayout mRefreshLayout;
    private WelfareAdapter mWelfareAdapter;
    private Activity mContext;
    private boolean mIsLoadingData = false;
    private LouProgressBar mProgressBar;
    private Disposable mDisposable;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = Objects.requireNonNull(layoutManager).findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            // 快到底部了，数据不多了，接着加载
            int nearBottom = totalItemCount - 8;
            if (!mWelfareAdapter.isMaxed() && lastVisibleItem >= nearBottom) {
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
        setContentView(R.layout.activity_gank_welfare);
        initView();
        initData();
    }

    private List<Welfare> getCheckedWelfares() {
        List<Welfare> list = new ArrayList<>();
        if (mWelfareAdapter != null) {
            List<Welfare> welfareListList = mWelfareAdapter.getList();
            for (Welfare welfare : welfareListList) {
                if (welfare.isSelected()) {
                    list.add(welfare);
                }
            }

        }
        return list;
    }

    private void toWelfare(String activeDay) {
        if (activeDay == null) {
            Toast.makeText(mContext, "格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] split = activeDay.split("-");
        if (split.length == 3) {
            String year = split[0];
            String month = split[1];
            String day = split[2];
            mProgressBar.show();
            mDisposable = NetWork.get(Constant.Url.Gank.getUrl(), GankApi.class).getGankContent(year, month, day)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(gankContentResult -> !gankContentResult.isError())
                    .map(GankContentResult::getResults)
                    .subscribe(gankContents -> {
                        mProgressBar.hide();
                        String content = getOneGankContent(gankContents);
                        if (TextUtils.isEmpty(content)) {
                            Utoast.show(mContext, "欣赏美图就好");
                            return;
                        }
                        WebContentActivity.newInstance(mContext, content);
                    }, throwable -> {
                        throwable.printStackTrace();
                        mProgressBar.hide();
                        Utoast.show(mContext, "欣赏美图就好");
                    });
        } else {
            Toast.makeText(mContext, "格式不对：" + activeDay, Toast.LENGTH_SHORT).show();
        }
    }

    private String getOneGankContent(List<GankContentResult.GankContent> gankContents) {
        if (gankContents.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (GankContentResult.GankContent gankContent : gankContents) {
            String content = gankContent.getContent();
            if (TextUtils.isEmpty(content)) {
                continue;
            }
            sb.append(content);
        }
        return sb.toString();
    }

    private void initView() {
        initTopView();
        mProgressBar = LouProgressBar.builder(mContext).tips("正在加载...");
        mRefreshLayout = findViewById(R.id.srl_gank_welfare);
        mRefreshLayout.setOnRefreshListener(this::reloadData);
        mLlytBottom = findViewById(R.id.llyt_bottom);
        initRecyclerView();
    }

    private void initRecyclerView() {
        EmptyRecyclerView recyclerView = findViewById(R.id.erv_gank_welfare);
        mWelfareAdapter = new WelfareAdapter(this);
        mWelfareAdapter.setTitle("福利岛");
        mWelfareAdapter.setOnItemClickListener(getOnItemClickListener());
        recyclerView.setAdapter(mWelfareAdapter);
        recyclerView.setItemTypeCount(mWelfareAdapter.getItemTypeCount());
        recyclerView.setEmptyView(findViewById(R.id.rlyt_empty));
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isOneLine = position == 0
                        || position >= recyclerView.getAdapter().getItemCount() - 1;
                return isOneLine ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DoubleItemWithOneHeaderOffsetDecoration(Uscreen.dp2Px(mContext, 16)));
        recyclerView.addOnScrollListener(mListener);
    }

    private WelfareAdapter.OnItemClickListener getOnItemClickListener() {
        return new WelfareAdapter.OnItemClickListener() {
            @Override
            public void onClick(int realPosition, Welfare welfare) {

                int mode = mWelfareAdapter.getMode();
                if (mode == 1) {
                    welfare.setSelected(!welfare.isSelected());
                    mWelfareAdapter.notifyItemChanged(realPosition);
                    List<Welfare> checkedWelfares = getCheckedWelfares();
                    initBottomViewWithData(mLlytBottom, checkedWelfares);

                } else if (mode == 0) {
                    toWelfare(Utime.transferThreeToOne(welfare.getPublishedAt()));
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public boolean onLongClick(Welfare welfare) {
                if (!TextUtils.isEmpty(welfare.getUrl())) {
                    doOnLongClickListener(welfare);
                } else {
                    Utoast.show(mContext, "URL不存在");
                }
                return false;
            }
        };
    }

    enum OperateType {
        SHARE("分享"),
        COPY_LINK("复制链接"),
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

    private void doOnLongClickListener(Welfare welfare) {
        new AlertDialog.Builder(mContext)
                .setTitle("操作")
                .setItems(OperateType.toStrArray(), (dialog, which) -> {
                    switch (OperateType.indexOf(which)) {
                        case SHARE:
                            LouProgressBar progressTips = LouProgressBar.builder(mContext).tips("正在加载...");
                            progressTips.show();
                            //noinspection ResultOfMethodCallIgnored
                            Observable.fromCallable(() -> welfare)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(s -> {
                                        Ushare.sharePicUrl(mContext, welfare.getUrl());
                                        progressTips.hide();
                                    }, throwable -> progressTips.hide());
                            break;
                        case COPY_LINK:
                            Usystem.copyString(mContext, welfare.getUrl());
                            break;
                        default:
                    }
                })
                .create()
                .show();
    }

    private void initData() {
        // 开始加载数据
        reloadData();
    }


    private void initTopView() {
        Toolbar toolbar = findViewById(R.id.toolbar_gank);
        toolbar.setTitle("看，有流星");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Uview.initStatusBar(this, R.color.colorAccent);
    }

    private void initBottomViewWithData(LinearLayout view, final List<Welfare> list) {
        int size = list.size();
        if (size > 0) {
            mLlytBottom.setVisibility(View.VISIBLE);
        } else {
            mLlytBottom.setVisibility(View.GONE);
            return;
        }

        TextView tvCount = view.findViewById(R.id.tv_gank_bottom_count);
        TextView tvFriend = view.findViewById(R.id.tv_share);
        TextView tvTimeline = view.findViewById(R.id.tv_share2);

        String caption = String.valueOf(list.size());
        tvCount.setText(caption);

        LouProgressBar progressTips = LouProgressBar.builder(mContext).tips("福利准备中");
        View.OnClickListener onClickListener = view1 -> {
            progressTips.show();
            new Thread(() -> {
                Ushare.clearImageDir(mContext);
                List<String> paths = new ArrayList<>();
                for (Welfare day : list) {
                    String welfareUrl = Ushare.loadWelfareUrl(day.getDesc());
                    if (TextUtils.isEmpty(welfareUrl)) {
                        continue;
                    }
                    String imageFilePathFromImageUrl = Ushare.getImageFilePathFromImageUrl(mContext, welfareUrl);
                    paths.add(imageFilePathFromImageUrl);
                }
                Ushare.sharePicUrls(mContext, paths);
                progressTips.hide();
            }).start();

        };
        tvFriend.setOnClickListener(onClickListener);
        tvTimeline.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    public void loadData(int number, int page, Consumer<List<Welfare>> consumer) {
        mIsLoadingData = true;

        // 通过取消上次加载，来防止出现加载数据混淆的问题（）
        unSubscribe();
        mDisposable = NetWork.get(Constant.Url.Gank.getUrl(), GankApi.class)
                .getWelfares(number, page)
                .map((Function<WelfareResult, List<Welfare>>) welfareResultResult -> {
                    if (welfareResultResult.isError()) {
                        return new ArrayList<>();
                    }
                    return welfareResultResult.getResults();
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    consumer.accept(t);
                    mIsLoadingData = false;
                }, throwable -> {
                    throwable.printStackTrace();
                    mIsLoadingData = false;
                    Utoast.show(mContext, "网络异常:" + throwable.getMessage());
                });
    }

    private void reloadData() {
        mLlytBottom.setVisibility(View.GONE);
        mWelfareAdapter.clearAll();
        mWelfareAdapter.getList().clear();
        mPage = 1;
        loadData(NUMBER, mPage, welfareList -> {
            if (welfareList.size() < NUMBER) {
                mWelfareAdapter.setMaxed(true);
            }
            mWelfareAdapter.getList().addAll(welfareList);
            mWelfareAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        });
    }

    private void loadMore() {
        loadData(NUMBER, ++mPage, welfareList -> {
            if (welfareList.size() < NUMBER) {
                mWelfareAdapter.setMaxed(true);
            }
            int lastSize = mWelfareAdapter.getList().size();
            mWelfareAdapter.getList().addAll(welfareList);
            mWelfareAdapter.notifyItemRangeInserted(lastSize + 1, welfareList.size());
        });
    }


    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mProgressBar.isShowing()) {
            unSubscribe();
            mProgressBar.hide();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_normal:
                if (mWelfareAdapter.getMode() == 0) {
                    return true;
                }
                mWelfareAdapter.setMode(0);
                mWelfareAdapter.clearSelected();
                mWelfareAdapter.notifyDataSetChanged();
                initBottomViewWithData(mLlytBottom, getCheckedWelfares());
                break;
            case R.id.menu_multiple:
                if (mWelfareAdapter.getMode() == 1) {
                    return true;
                }
                mWelfareAdapter.setMode(1);
                mWelfareAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
