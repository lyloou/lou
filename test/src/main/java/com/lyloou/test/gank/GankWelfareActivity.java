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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class GankWelfareActivity extends AppCompatActivity {
    private static final String TAG = "GankWelfareActivity";
    private final List<ActiveDay> mActiveDays = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;
    private ActiveDayAdapter mActiveDayAdapter;
    private Activity mContext;
    private boolean mIsLoading = false;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            int max_size = mActiveDays.size();
            if (totalItemCount < max_size && lastVisibleItem >= totalItemCount / 2 - 4) {
                if (mIsLoading) {
                    Log.i(TAG, "onScrolled: 加载中...");
                } else {
                    Log.i(TAG, "onScrolled: 加载更多了.");
                    loadMore();
                }

            }
        }
    };
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_gank_welfare);

        initView();
    }

    private void initView() {
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) findViewById(R.id.erv_gank_welfare);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_gank_welfare);
        ImageView ivEmpty = (ImageView) findViewById(R.id.iv_empty);
        RelativeLayout rlytEmpty = (RelativeLayout) findViewById(R.id.rlyt_empty);

        mActiveDayAdapter = new ActiveDayAdapter(this);
        mActiveDayAdapter.setTitle("福利岛");
//        mActiveDayAdapter.setTitle("一大波福利来袭");
        mActiveDayAdapter.setOnItemClickListener(new ActiveDayAdapter.OnItemClickListener() {
            @Override
            public void onClick(int realPosition, ActiveDay activeDay) {
                activeDay.setChecked(!activeDay.isChecked());

                mActiveDayAdapter.notifyItemChanged(realPosition);
            }
        });
        recyclerView.setAdapter(mActiveDayAdapter);
        recyclerView.setItemTypeCount(mActiveDayAdapter.getItemTypeCount());
        recyclerView.setEmptyView(rlytEmpty);
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
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(mContext, 16)));
        recyclerView.addOnScrollListener(mListener);

        Glide.with(this).load(R.drawable.loading).asGif().placeholder(R.mipmap.empty).into(ivEmpty);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActiveDayAdapter.setMaxed(false);
                    }
                });
                mActiveDayAdapter.clearAll();
                mActiveDays.clear();
                loadDatas();
            }
        });

        // 开始加载数据
        loadDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActiveDayAdapter.getListSize() == 0) {
            loadDatas();
        }
    }

    @Override
    protected void onDestroy() {
        unSubscribe();
        super.onDestroy();
    }

    public void loadDatas() {

        // 通过取消上次加载，来防止出现加载数据混淆的问题（）
        unSubscribe();

        mDisposable = NetWork.getGankApi().getActiveDays()

                .map(new Function<ActiveDayResult, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull ActiveDayResult activeDayResult) throws Exception {
                        if (activeDayResult.isError()) {
                            return new ArrayList<>();
                        }
                        return activeDayResult.getResults();
                    }
                })
                .map(new Function<List<String>, List<ActiveDay>>() {
                    @Override
                    public List<ActiveDay> apply(@NonNull List<String> list) throws Exception {
                        List<ActiveDay> activeDays = new ArrayList<ActiveDay>();
                        for (String day : list) {
                            activeDays.add(new ActiveDay(day));
                        }
                        return activeDays;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ActiveDay>>() {
                    @Override
                    public void accept(@NonNull List<ActiveDay> activeDays) throws Exception {
                        mActiveDays.addAll(activeDays);
                        loadMore();

                        mRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mRefreshLayout.setRefreshing(false);

                        Utoast.show(mContext, "网络异常:" + throwable.getMessage());
                    }
                });
    }

    private void loadMore() {
        int maxSize = mActiveDays.size();
        int listSize = mActiveDayAdapter.getListSize();
        if (maxSize == listSize) {
            return;
        }

        mIsLoading = true;
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int loadedSize = Math.min(maxSize, listSize + 20);
                mActiveDayAdapter.clearAll();
                mActiveDayAdapter.addAll(mActiveDays.subList(0, loadedSize));
                if(maxSize == loadedSize){
                    if (!mActiveDayAdapter.isMaxed()) {
                        mActiveDayAdapter.setMaxed(true);
                        mActiveDayAdapter.notifyDataSetChanged();
                    }
                }
                System.out.println("=============>" + loadedSize);
                mIsLoading = false;
            }
        }, 1600);

    }

    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}