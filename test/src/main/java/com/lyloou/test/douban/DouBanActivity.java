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

package com.lyloou.test.douban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.common.EmptyRecyclerView;
import com.lyloou.test.common.ItemOffsetDecoration;
import com.lyloou.test.common.NetWork;
import com.lyloou.test.common.webview.WebActivity;
import com.lyloou.test.util.Uscreen;
import com.lyloou.test.util.Utoast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DouBanActivity extends AppCompatActivity {


    private static final String TAG = "DouBanActivity";
    private static final int TOTAL_ITEM_SIZE = 250;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsLoading = false;
    private SubjectAdapter mSubjectAdapter;
    private Disposable mDisposable;
    private Activity mContext;
    private RecyclerView.OnScrollListener mListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();

            if (totalItemCount < TOTAL_ITEM_SIZE && lastVisibleItem >= totalItemCount - 4) {
                // 注意：要限制请求，否则请求太多次数，导致服务器崩溃或者服务器拒绝请求（罪过，罪过）。
                if (mIsLoading) {
                    Log.i(TAG, "加载中...");
                } else {
                    Log.i(TAG, "加载更多了.");
                    loadDatas();
                }

            } else if (totalItemCount >= TOTAL_ITEM_SIZE) {
                if (!mSubjectAdapter.isMaxed()) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 控制footer的显示情况
                            mSubjectAdapter.setMaxed(true);
                        }
                    });
                }
            }

            Log.d(TAG, "lastVisibleItem=" + lastVisibleItem);
            Log.d(TAG, "totalItemCount=" + totalItemCount);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_douban);
        initView();
    }

    private void initView() {
        EmptyRecyclerView recyclerView = (EmptyRecyclerView) findViewById(R.id.erv_douban);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_douban);

        mSubjectAdapter = new SubjectAdapter(this);
        mSubjectAdapter.setOnItemClickListener(new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onClick(Subject subject) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.EXTRA_DATA_URL, subject.getAlt());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mSubjectAdapter);
        recyclerView.setItemTypeCount(mSubjectAdapter.getItemTypeCount());
        recyclerView.setEmptyView(findViewById(R.id.rlyt_empty));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(mContext, 16)));
        recyclerView.addOnScrollListener(mListener);


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSubjectAdapter.setMaxed(false);
                    }
                });
                mSubjectAdapter.clearAll();
                loadDatas();
            }
        });

        // 开始加载数据
        loadDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSubjectAdapter.getListSize() == 0) {
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

        mIsLoading = true;
        mDisposable = NetWork.getDouBanApi()
                .getTopMovie(mSubjectAdapter.getListSize(), 20)
                .map(new Function<HttpResult<List<Subject>>, List<Subject>>() {
                    @Override
                    public List<Subject> apply(@NonNull HttpResult<List<Subject>> listHttpResult) throws Exception {
                        mSubjectAdapter.setTitle(listHttpResult.getTitle());
                        return listHttpResult.getSubjects();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Subject>>() {
                    @Override
                    public void accept(@NonNull List<Subject> subjects) throws Exception {
                        mSubjectAdapter.addAll(subjects);
                        mRefreshLayout.setRefreshing(false);
                        mIsLoading = false;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "call: error", throwable);
                        mRefreshLayout.setRefreshing(false);
                        mIsLoading = false;

                        Utoast.show(DouBanActivity.this, "网络异常:" + throwable.getMessage());
                    }
                });
    }

    private void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


}
