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
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
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
    List<Subject> mData;
    SubjectAdapter mSubjectAdapter;
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
                    loadSubject();
                }

            } else if (totalItemCount >= TOTAL_ITEM_SIZE) {
                if (!mSubjectAdapter.isMaxed())
                    mErv.post(new Runnable() {
                        @Override
                        public void run() {
                            mSubjectAdapter.setMaxed(true);
                        }
                    });
            }

            Log.d(TAG, "onScrolled: lastVisibleItem=" + lastVisibleItem);
            Log.d(TAG, "onScrolled: totalItemCount=" + totalItemCount);

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
        mData = new ArrayList<>();

        View emptyView = findViewById(R.id.rlyt_empty);
        mErv.setItemTypeCount(3);
        mErv.setEmptyView(emptyView);
        mErv.setHasFixedSize(true);
        mSubjectAdapter = new SubjectAdapter(this, mData);
        mErv.setAdapter(mSubjectAdapter);
        mErv.setLayoutManager(new LinearLayoutManager(this));
        mErv.addItemDecoration(new ItemOffsetDecoration(20));
        mErv.addOnScrollListener(mListener);

        ImageView emptyIcon = (ImageView) emptyView.findViewById(R.id.iv_empty);
        Glide.with(this).load("http://cherylgood.cn/images/404.gif").asGif().placeholder(R.mipmap.empty).into(emptyIcon);

        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mData.clear();
                mErv.getAdapter().notifyDataSetChanged();
                Utoast.show(MainActivity.this, "正在加载，请稍后。。。");
                loadSubject();
            }
        });
        // 开始加载数据
        loadSubject();
    }

    public void loadSubject() {
        mIsLoading = true;

        Observable<HttpResult<List<Subject>>> topMovie = NetWork.getSubjectService().getTopMovie(mData.size(), 20);
        topMovie
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
                        mData.addAll(subjects);
                        mErv.getAdapter().notifyDataSetChanged();

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
                })
        ;
    }


}
