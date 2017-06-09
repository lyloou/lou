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

package com.lyloou.douban;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private static final int TOTAL_ITEM_SIZE = 60;
    @Bind(R.id.erv)
    EmptyRecyclerView mErv;
    @Bind(R.id.srl)
    SwipeRefreshLayout mSrl;
    boolean mIsLoading = false;
    SubjectAdapter mSubjectAdapter;
    @Bind(R.id.iv_empty)
    ImageView mIvEmpty;
    @Bind(R.id.rlyt_empty)
    RelativeLayout mRlytEmpty;
    Subscription mSubscription;
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
                if (!mSubjectAdapter.isMaxed())
                    mErv.post(new Runnable() {
                        @Override
                        public void run() {
                            // 控制footer的显示情况
                            mSubjectAdapter.setMaxed(true);
                        }
                    });
            }

            Log.d(TAG, "lastVisibleItem=" + lastVisibleItem);
            Log.d(TAG, "totalItemCount=" + totalItemCount);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSubjectAdapter = new SubjectAdapter(this);
        mErv.setAdapter(mSubjectAdapter);
        mErv.setItemTypeCount(mSubjectAdapter.getItemTypeCount());
        mErv.setEmptyView(mRlytEmpty);
        mErv.setHasFixedSize(true);
        mErv.setLayoutManager(new LinearLayoutManager(this));
        mErv.addItemDecoration(new ItemOffsetDecoration(20));
        mErv.addOnScrollListener(mListener);

        Glide.with(this).load("http://cherylgood.cn/images/404.gif").asGif().placeholder(R.mipmap.empty).into(mIvEmpty);

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utoast.show(MainActivity.this, "正在加载，请稍后。。。");

                mErv.post(new Runnable() {
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

    public void loadDatas() {

        // 解决加载混淆的问题
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mIsLoading = true;
        Observable<HttpResult<List<Subject>>> topMovie = NetWork.getSubjectService().getTopMovie(mSubjectAdapter.getListSize(), 20);
        mSubscription = topMovie
                .map(new Func1<HttpResult<List<Subject>>, List<Subject>>() {
                    @Override
                    public List<Subject> call(HttpResult<List<Subject>> listHttpResult) {
                        return listHttpResult.getSubjects();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Subject>>() {
                    @Override
                    public void call(List<Subject> subjects) {
                        mSubjectAdapter.addAll(subjects);
                        mSrl.setRefreshing(false);
                        mIsLoading = false;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "call: error", throwable);
                        mSrl.setRefreshing(false);
                        mIsLoading = false;

                        Utoast.show(MainActivity.this, "网络异常:" + throwable.getMessage());
                    }
                });
    }


}
